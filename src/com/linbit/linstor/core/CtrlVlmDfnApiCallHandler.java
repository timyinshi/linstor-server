package com.linbit.linstor.core;

import com.linbit.ExhaustedPoolException;
import com.linbit.ImplementationError;
import com.linbit.InvalidNameException;
import com.linbit.TransactionMgr;
import com.linbit.ValueInUseException;
import com.linbit.ValueOutOfRangeException;
import com.linbit.drbd.md.MdException;
import com.linbit.drbd.md.MetaData;
import com.linbit.linstor.LinStorDataAlreadyExistsException;
import com.linbit.linstor.LinStorException;
import com.linbit.linstor.MinorNumber;
import com.linbit.linstor.Resource;
import com.linbit.linstor.ResourceDefinition;
import com.linbit.linstor.ResourceDefinitionData;
import com.linbit.linstor.Volume;
import com.linbit.linstor.VolumeDefinition;
import com.linbit.linstor.VolumeDefinition.VlmDfnApi;
import com.linbit.linstor.VolumeDefinition.VlmDfnFlags;
import com.linbit.linstor.VolumeDefinitionData;
import com.linbit.linstor.VolumeDefinitionDataFactory;
import com.linbit.linstor.VolumeNumber;
import com.linbit.linstor.annotation.ApiContext;
import com.linbit.linstor.api.ApiCallRc;
import com.linbit.linstor.api.ApiCallRcImpl;
import com.linbit.linstor.api.ApiCallRcImpl.ApiCallRcEntry;
import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.interfaces.serializer.CtrlStltSerializer;
import com.linbit.linstor.dbcp.DbConnectionPool;
import com.linbit.linstor.logging.ErrorReporter;
import com.linbit.linstor.netcom.Peer;
import com.linbit.linstor.numberpool.DynamicNumberPool;
import com.linbit.linstor.numberpool.MinorNrPool;
import com.linbit.linstor.propscon.Props;
import com.linbit.linstor.security.AccessContext;
import com.linbit.linstor.security.AccessDeniedException;
import com.linbit.linstor.security.AccessType;
import com.linbit.linstor.security.ControllerSecurityModule;
import com.linbit.linstor.security.ObjectProtection;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

class CtrlVlmDfnApiCallHandler extends AbsApiCallHandler
{
    private final ThreadLocal<String> currentRscNameStr = new ThreadLocal<>();
    private final ThreadLocal<VlmDfnApi> currentVlmDfnApi = new ThreadLocal<>();
    private final ThreadLocal<Integer> currentVlmNr = new ThreadLocal<>();
    private final CoreModule.ResourceDefinitionMap rscDfnMap;
    private final ObjectProtection rscDfnMapProt;
    private final DynamicNumberPool minorNrPool;
    private final String defaultStorPoolName;
    private final VolumeDefinitionDataFactory volumeDefinitionDataFactory;

    @Inject
    CtrlVlmDfnApiCallHandler(
        ErrorReporter errorReporterRef,
        DbConnectionPool dbConnectionPoolRef,
        CtrlStltSerializer interComSerializer,
        @ApiContext AccessContext apiCtx,
        CoreModule.ResourceDefinitionMap rscDfnMapRef,
        @Named(ControllerSecurityModule.RSC_DFN_MAP_PROT) ObjectProtection rscDfnMapProtRef,
        @MinorNrPool DynamicNumberPool minorNrPoolRef,
        @Named(ConfigModule.CONFIG_STOR_POOL_NAME) String defaultStorPoolNameRef,
        CtrlObjectFactories objectFactories,
        VolumeDefinitionDataFactory volumeDefinitionDataFactoryRef
    )
    {
        super(
            errorReporterRef,
            dbConnectionPoolRef,
            apiCtx,
            ApiConsts.MASK_VLM_DFN,
            interComSerializer,
            objectFactories
        );
        super.setNullOnAutoClose(
            currentRscNameStr,
            currentVlmDfnApi,
            currentVlmNr
        );

        rscDfnMap = rscDfnMapRef;
        rscDfnMapProt = rscDfnMapProtRef;
        minorNrPool = minorNrPoolRef;
        defaultStorPoolName = defaultStorPoolNameRef;
        volumeDefinitionDataFactory = volumeDefinitionDataFactoryRef;
    }

    private void updateCurrentKeyNumber(final String key, Integer number)
    {
        String intStringOrNull = null;
        if (number != null)
        {
            intStringOrNull = number.toString();
            currentVariables.get().put(key, intStringOrNull);
        }
        else
        {
            currentVariables.get().remove(key);
        }
    }

    ApiCallRc createVolumeDefinitions(
        AccessContext accCtx,
        Peer client,
        String rscNameStr,
        List<VlmDfnApi> vlmDfnApiList
    )
    {
        ApiCallRcImpl apiCallRc = new ApiCallRcImpl();

        try (
            AbsApiCallHandler basicallyThis = setContext(
                accCtx,
                client,
                ApiCallType.CREATE,
                apiCallRc,
                null, // create new transMgr
                rscNameStr,
                null // no vlmNr
            )
        )
        {
            if (vlmDfnApiList.isEmpty())
            {
                addAnswer(
                    "Volume definition list to create is empty.",
                    null,
                    "Volume definition list that should be added to the resource is empty.",
                    null,
                    ApiConsts.MASK_WARN
                );
                throw new ApiCallHandlerFailedException();
            }

            ResourceDefinition rscDfn = loadRscDfn(rscNameStr, true);

            Iterator<Resource> iterateResource = getRscIterator(rscDfn);
            List<Resource> rscList = new ArrayList<>();
            while (iterateResource.hasNext())
            {
                rscList.add(iterateResource.next());
            }

            List<VolumeDefinition> vlmDfnsCreated = new ArrayList<>();
            for (VolumeDefinition.VlmDfnApi vlmDfnApi : vlmDfnApiList)
            {
                {
                    currentVlmDfnApi.set(vlmDfnApi);

                    updateCurrentKeyNumber(ApiConsts.KEY_VLM_NR, vlmDfnApi.getVolumeNr());
                    updateCurrentKeyNumber(ApiConsts.KEY_MINOR_NR, vlmDfnApi.getMinorNr());
                }
                VolumeNumber volNr = null;
                MinorNumber minorNr = null;

                volNr = getOrGenerateVlmNr(
                    vlmDfnApi,
                    rscDfn,
                    apiCtx
                );
                updateCurrentKeyNumber(ApiConsts.KEY_VLM_NR, volNr.value);
                currentVlmNr.set(volNr.value); // set currentVlmNr for exception error reporting

                minorNr = getOrGenerateMinorNr(vlmDfnApi);
                updateCurrentKeyNumber(ApiConsts.KEY_MINOR_NR, minorNr.value);

                long size = vlmDfnApi.getSize();

                VlmDfnFlags[] vlmDfnInitFlags = null;

                VolumeDefinitionData vlmDfn = createVlmDfnData(
                    accCtx,
                    rscDfn,
                    volNr,
                    minorNr,
                    size,
                    vlmDfnInitFlags
                );
                getVlmDfnProps(vlmDfn).map().putAll(vlmDfnApi.getProps());

                vlmDfnsCreated.add(vlmDfn);
            }
            currentObjRefs.get().remove(ApiConsts.KEY_VLM_NR);
            currentVariables.get().remove(ApiConsts.KEY_VLM_NR);
            currentVariables.get().remove(ApiConsts.KEY_MINOR_NR);

            for (Resource rsc : rscList)
            {
                adjustRscVolumes(rsc);
            }

            commit();

            rscDfnMap.put(rscDfn.getName(), rscDfn);

            for (VolumeDefinition vlmDfn : vlmDfnsCreated)
            {
                apiCallRc.addEntry(createVlmDfnCrtSuccessEntry(vlmDfn, rscNameStr));
            }
            updateSatellites(rscDfn);
        }
        catch (ApiCallHandlerFailedException ignore)
        {
            // a report and a corresponding api-response already created. nothing to do here
        }
        catch (Exception | ImplementationError exc)
        {
            reportStatic(
                exc,
                ApiCallType.CREATE,
                "a volume definition of resource definition '" + rscNameStr + "'",
                getObjRefs(rscNameStr, null),
                getVariables(rscNameStr, null),
                apiCallRc,
                accCtx,
                client
            );
        }

        return apiCallRc;
    }

    ApiCallRc modifyVlmDfn(
        AccessContext accCtx,
        Peer client,
        UUID vlmDfnUuid,
        String rscName,
        int vlmNr,
        Long size,
        Integer minorNr,
        Map<String, String> overrideProps,
        Set<String> deletePropKeys
    )
    {
        ApiCallRcImpl apiCallRc = new ApiCallRcImpl();

        try (AbsApiCallHandler basicallyThis = setContext(
                accCtx,
                client,
                ApiCallType.MODIFY,
                apiCallRc,
                null, // create new transMgr
                rscName,
                vlmNr
            )
        )
        {
            VolumeDefinitionData vlmDfn = loadVlmDfn(rscName, vlmNr);

            if (vlmDfnUuid != null && !vlmDfnUuid.equals(vlmDfn.getUuid()))
            {
                throw asExc(
                    null,
                    "UUID check failed. Given UUID: " + vlmDfnUuid + ". Persisted UUID: " + vlmDfn.getUuid(),
                    ApiConsts.FAIL_UUID_VLM_DFN
                );
            }
            Props props = getVlmDfnProps(vlmDfn);
            Map<String, String> propsMap = props.map();

            propsMap.putAll(overrideProps);

            for (String delKey : deletePropKeys)
            {
                propsMap.remove(delKey);
            }

            if (size != null)
            {
                long vlmDfnSize = getVlmDfnSize(vlmDfn);
                if (size >= vlmDfnSize)
                {
                    setVlmDfnSize(vlmDfn, size);
                }
                else
                {
                    if (!hasDeployedVolumes(vlmDfn))
                    {
                        setVlmDfnSize(vlmDfn, size);
                    }
                    else
                    {
                        throw asExc(
                            null,
                            "Deployed volumes can only grow in size, not shrink.",
                            null,
                            null,
                            "If you want to shrink the volume definition, you have to remove all volumes of " +
                            "this volume definition first.",
                            ApiConsts.FAIL_INVLD_VLM_SIZE
                        );
                    }
                }
            }

            if (minorNr != null)
            {
                setMinorNr(vlmDfn, minorNr);
            }

            commit();

            reportSuccess(vlmDfn.getUuid());

            updateSatellites(vlmDfn.getResourceDefinition());
        }
        catch (ApiCallHandlerFailedException ignore)
        {
            // a report and a corresponding api-response already created. nothing to do here
        }
        catch (Exception | ImplementationError exc)
        {
            reportStatic(
                exc,
                ApiCallType.MODIFY,
                getObjectDescriptionInline(rscName, vlmNr),
                getObjRefs(rscName, vlmNr),
                getVariables(rscName, vlmNr),
                apiCallRc,
                accCtx,
                client
            );
        }

        return apiCallRc;
    }

    ApiCallRc deleteVolumeDefinition(
        AccessContext accCtx,
        Peer client,
        String rscName,
        int vlmNr
    )
    {
        ApiCallRcImpl apiCallRc = new ApiCallRcImpl();

        try (
            AbsApiCallHandler basicallyThis = setContext(
                accCtx,
                client,
                ApiCallType.DELETE,
                apiCallRc,
                null, // create new transMgr
                rscName,
                vlmNr
            );
        )
        {
            VolumeDefinitionData vlmDfn = loadVlmDfn(rscName, vlmNr);
            UUID vlmDfnUuid = vlmDfn.getUuid();
            ResourceDefinition rscDfn = vlmDfn.getResourceDefinition();

            // mark volumes to delete or check if all a 'CLEAN'
            Iterator<Volume> itVolumes = vlmDfn.iterateVolumes(accCtx);
            boolean allVlmClean = true;
            while (itVolumes.hasNext())
            {
                Volume vlm = itVolumes.next();
                if (vlm.getFlags().isUnset(accCtx, Volume.VlmFlags.CLEAN))
                {
                    vlm.markDeleted(accCtx);
                    allVlmClean = false;
                }
            }

            String deleteAction;
            if (allVlmClean)
            {
                vlmDfn.delete(accCtx);
                deleteAction = " was deleted.";
            }
            else
            {
                vlmDfn.markDeleted(accCtx);
                deleteAction = " marked for deletion.";
            }

            commit();

            updateSatellites(rscDfn);

            reportSuccess(
                getObjectDescriptionInlineFirstLetterCaps() + deleteAction,
                getObjectDescriptionInlineFirstLetterCaps() + " UUID is:"  + vlmDfnUuid
            );
        }
        catch (ApiCallHandlerFailedException ignore)
        {
            // a report and a corresponding api-response already created. nothing to do here
        }
        catch (Exception | ImplementationError exc)
        {
            reportStatic(
                exc,
                ApiCallType.DELETE,
                getObjectDescriptionInline(rscName, vlmNr),
                getObjRefs(rscName, vlmNr),
                getVariables(rscName, vlmNr),
                apiCallRc,
                accCtx,
                client
            );
        }

        return apiCallRc;
    }

    private void ensureRscMapProtAccess(AccessContext accCtx)
    {
        try
        {
            rscDfnMapProt.requireAccess(accCtx, AccessType.CHANGE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "change any existing resource definition",
                ApiConsts.FAIL_ACC_DENIED_RSC_DFN
            );
        }
    }

    private VolumeDefinitionData loadVlmDfn(String rscName, int vlmNr)
    {
        ResourceDefinitionData rscDfn = loadRscDfn(rscName, true);
        VolumeDefinitionData vlmDfn;
        try
        {
            vlmDfn = volumeDefinitionDataFactory.getInstance(
                currentAccCtx.get(),
                rscDfn,
                new VolumeNumber(vlmNr),
                null, // minor
                null, // volSize
                null, // initFlags
                currentTransMgr.get(),
                false,
                false
            );

            if (vlmDfn == null)
            {
                throw asExc(
                    null, // throwable
                    "Volume definition '" + rscName + "' with volume number '" + vlmNr + "' not found.",
                    "The specified volume definition '" + rscName +
                        "' with volume number '" + vlmNr + "' could not be found in the database", // cause
                    null, // details
                    "Create a volume definition with the name '" + rscName + "' first.", // correction
                    ApiConsts.FAIL_NOT_FOUND_VLM_DFN
                );
            }
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "loading volume definition '" + vlmNr + "' from resource definition '" + rscName + "'",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (ValueOutOfRangeException valueOutOfRangeExc)
        {
            throw asExc(
                valueOutOfRangeExc,
                "The given volume number '" + vlmNr + "' is invalid.",
                ApiConsts.FAIL_INVLD_VLM_NR
            );
        }
        catch (LinStorDataAlreadyExistsException | MdException implError)
        {
            throw asImplError(implError);
        }
        catch (SQLException sqlExc)
        {
            throw asSqlExc(
                sqlExc,
                "loading " + getObjectDescriptionInline()
            );
        }
        return vlmDfn;
    }

    private Iterator<Resource> getRscIterator(ResourceDefinition rscDfn)
    {
        Iterator<Resource> iterator;
        try
        {
            iterator = rscDfn.iterateResource(apiCtx);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asImplError(accDeniedExc);
        }
        return iterator;
    }

    private VolumeNumber getOrGenerateVlmNr(VlmDfnApi vlmDfnApi, ResourceDefinition rscDfn, AccessContext accCtx)
    {
        VolumeNumber vlmNr;
        try
        {
            vlmNr = CtrlRscDfnApiCallHandler.getVlmNr(vlmDfnApi, rscDfn, accCtx);
        }
        catch (ValueOutOfRangeException valOORangeExc)
        {
            throw asExc(
                valOORangeExc,
                String.format(
                    "The specified volume number '%d' is invalid. Volume numbers have to be in range of %d - %d.",
                    vlmDfnApi.getVolumeNr(),
                    VolumeNumber.VOLUME_NR_MIN,
                    VolumeNumber.VOLUME_NR_MAX
                ),
                ApiConsts.FAIL_INVLD_VLM_NR
            );
        }
        catch (LinStorException linStorExc)
        {
            throw asExc(
                linStorExc,
                "An exception occured during generation of a volume number.",
                ApiConsts.FAIL_POOL_EXHAUSTED_VLM_NR
            );
        }
        return vlmNr;
    }

    private MinorNumber getOrGenerateMinorNr(VlmDfnApi vlmDfnApi)
    {
        MinorNumber freeMinorNr;
        try
        {
            Integer minorNrInt = vlmDfnApi.getMinorNr();
            if (minorNrInt == null)
            {
                minorNrInt = minorNrPool.autoAllocate();
            }
            else
            {
                minorNrPool.allocate(minorNrInt);
            }
            freeMinorNr = new MinorNumber(minorNrInt);
        }
        catch (ValueOutOfRangeException | ValueInUseException exc)
        {
            throw asExc(
                exc,
                String.format(
                    "The specified minor number '%d' is invalid.",
                    vlmDfnApi.getMinorNr()
                ),
                ApiConsts.FAIL_INVLD_MINOR_NR
            );
        }
        catch (ExhaustedPoolException exhaustedPoolExc)
        {
            throw asExc(
                exhaustedPoolExc,
                String.format(
                    "Could not find free minor number in range %d - %d.",
                    minorNrPool.getRangeMin(),
                    minorNrPool.getRangeMax()
                ),
                ApiConsts.FAIL_POOL_EXHAUSTED_MINOR_NR
            );
        }
        return freeMinorNr;
    }

    private VolumeDefinitionData createVlmDfnData(
        AccessContext accCtx,
        ResourceDefinition rscDfn,
        VolumeNumber volNr,
        MinorNumber minorNr,
        long size,
        VlmDfnFlags[] vlmDfnInitFlags
    )
    {
        VolumeDefinitionData vlmDfn;
        try
        {
            vlmDfn = volumeDefinitionDataFactory.getInstance(
                accCtx,
                rscDfn,
                volNr,
                minorNr,
                size,
                vlmDfnInitFlags,
                currentTransMgr.get(),
                true, // persist this entry
                true // throw exception if the entry exists
            );
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "create " + getObjectDescriptionInline(),
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (LinStorDataAlreadyExistsException dataAlreadyExistsExc)
        {
            throw asExc(
                dataAlreadyExistsExc,
                String.format(
                    "A volume definition with the number %d already exists in resource definition '%s'.",
                        currentVlmDfnApi.get().getVolumeNr(),
                        currentRscNameStr.get()
                ),
                ApiConsts.FAIL_EXISTS_VLM_DFN
            );
        }
        catch (SQLException sqlExc)
        {
            throw asSqlExc(
                sqlExc,
                "creating " + getObjectDescriptionInline()
            );
        }
        catch (MdException mdExc)
        {
            throw asExc(
                mdExc,
                String.format(
                    "The " + getObjectDescriptionInline() + " has an invalid size of '%d'. " +
                        "Valid sizes range from %d to %d.",
                    size,
                    MetaData.DRBD_MIN_NET_kiB,
                    MetaData.DRBD_MAX_kiB
                ),
                ApiConsts.FAIL_INVLD_VLM_SIZE
            );
        }
        return vlmDfn;
    }

    private Props getVlmDfnProps(VolumeDefinitionData vlmDfn)
    {
        Props props;
        try
        {
            props = vlmDfn.getProps(currentAccCtx.get());
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "access the properties of " + getObjectDescriptionInline(),
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return props;
    }

    private void adjustRscVolumes(Resource rsc)
    {
        try
        {
            rsc.adjustVolumes(apiCtx, currentTransMgr.get(), defaultStorPoolName);
        }
        catch (InvalidNameException invalidNameExc)
        {
            throw asExc(
                invalidNameExc,
                "The given stor pool name '" + invalidNameExc.invalidName + "' is invalid",
                ApiConsts.FAIL_INVLD_STOR_POOL_NAME
            );
        }
        catch (LinStorException linStorExc)
        {
            throw asExc(
                linStorExc,
                "An exception occured while adjusting resources.",
                ApiConsts.FAIL_UNKNOWN_ERROR // TODO somehow find out if the exception is caused
            // by a missing storpool (not deployed yet?), and return a more meaningful RC
            );
        }
    }

    private ApiCallRcEntry createVlmDfnCrtSuccessEntry(VolumeDefinition vlmDfn, String rscNameStr)
    {
        ApiCallRcEntry vlmDfnCrtSuccessEntry = new ApiCallRcEntry();
        try
        {
            vlmDfnCrtSuccessEntry.setReturnCode(ApiConsts.MASK_VLM_DFN | ApiConsts.MASK_CRT | ApiConsts.CREATED);
            String successMessage = String.format(
                "New volume definition with number '%d' of resource definition '%s' created.",
                vlmDfn.getVolumeNumber().value,
                rscNameStr
            );
            vlmDfnCrtSuccessEntry.setMessageFormat(successMessage);
            vlmDfnCrtSuccessEntry.putVariable(
                ApiConsts.KEY_RSC_DFN,
                vlmDfn.getResourceDefinition().getName().displayValue
            );
            vlmDfnCrtSuccessEntry.putVariable(ApiConsts.KEY_VLM_NR, Integer.toString(vlmDfn.getVolumeNumber().value));
            vlmDfnCrtSuccessEntry.putVariable(
                ApiConsts.KEY_MINOR_NR,
                Integer.toString(vlmDfn.getMinorNr(apiCtx).value)
            );
            vlmDfnCrtSuccessEntry.putObjRef(ApiConsts.KEY_RSC_DFN, rscNameStr);
            vlmDfnCrtSuccessEntry.putObjRef(ApiConsts.KEY_VLM_NR, Integer.toString(vlmDfn.getVolumeNumber().value));

            errorReporter.logInfo(successMessage);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asImplError(accDeniedExc);
        }
        return vlmDfnCrtSuccessEntry;
    }

    private long getVlmDfnSize(VolumeDefinitionData vlmDfn)
    {
        long volumeSize;
        try
        {
            volumeSize = vlmDfn.getVolumeSize(currentAccCtx.get());
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "accessing Volume definition's size",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return volumeSize;
    }

    private void setVlmDfnSize(VolumeDefinitionData vlmDfn, Long size)
    {
        try
        {
            vlmDfn.setVolumeSize(currentAccCtx.get(), size);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "updating Volume definition's size",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (SQLException sqlExc)
        {
            throw asSqlExc(sqlExc, "updating volume definition's size");
        }

    }

    private boolean hasDeployedVolumes(VolumeDefinitionData vlmDfn)
    {
        boolean hasVolumes;
        try
        {
            hasVolumes = vlmDfn.iterateVolumes(currentAccCtx.get()).hasNext();
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "accessing volume definition",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return hasVolumes;
    }

    private void setMinorNr(VolumeDefinitionData vlmDfn, Integer minorNr)
    {
        try
        {
            vlmDfn.setMinorNr(
                currentAccCtx.get(),
                toMinorNr(minorNr)
            );
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw asAccDeniedExc(
                accDeniedExc,
                "setting the minor number",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (SQLException sqlExc)
        {
            throw asSqlExc(sqlExc, "updating minor number");
        }
    }

    private MinorNumber toMinorNr(Integer minorNr)
    {
        MinorNumber minorNumber;
        try
        {
            minorNumber = new MinorNumber(minorNr);
        }
        catch (ValueOutOfRangeException valOutOfRangeExc)
        {
            throw asExc(
                valOutOfRangeExc,
                "The given minor number '" + minorNr + "' is invalid.",
                ApiConsts.FAIL_INVLD_MINOR_NR
            );
        }
        return minorNumber;
    }

    private AbsApiCallHandler setContext(
        AccessContext accCtx,
        Peer client,
        ApiCallType apiCallType,
        ApiCallRcImpl apiCallRc,
        TransactionMgr transMgr,
        String rscNameStr,
        Integer vlmNr
    )
    {
        super.setContext(
            accCtx,
            client,
            apiCallType,
            apiCallRc,
            transMgr,
            getObjRefs(rscNameStr, vlmNr),
            getVariables(rscNameStr, vlmNr)
        );

        ensureRscMapProtAccess(accCtx);

        currentRscNameStr.set(rscNameStr);
        currentVlmDfnApi.set(null);
        currentVlmNr.set(vlmNr);

        return this;
    }

    private Map<String, String> getObjRefs(String rscName, Integer vlmNr)
    {
        Map<String, String> objRefs = new TreeMap<>();
        objRefs.put(ApiConsts.KEY_RSC_DFN, rscName);
        if (vlmNr != null)
        {
            objRefs.put(ApiConsts.KEY_VLM_NR, vlmNr.toString());
        }
        return objRefs;
    }

    private Map<String, String> getVariables(String rscName, Integer vlmNr)
    {
        Map<String, String> vars = new TreeMap<>();
        vars.put(ApiConsts.KEY_RSC_NAME, rscName);
        if (vlmNr != null)
        {
            vars.put(ApiConsts.KEY_VLM_NR, vlmNr.toString());
        }
        return vars;
    }

    @Override
    protected String getObjectDescription()
    {
        return "Resource definition: " + currentRscNameStr.get() + ", Volume number: " + currentVlmNr.get();
    }

    @Override
    protected String getObjectDescriptionInline()
    {
        return getObjectDescriptionInline(currentRscNameStr.get(), currentVlmNr.get());
    }

    public static String getObjectDescriptionInline(String rscName, Integer vlmNr)
    {
        return "volume definition with number '" + vlmNr + "' of resource definition '" + rscName + "'";
    }
}
