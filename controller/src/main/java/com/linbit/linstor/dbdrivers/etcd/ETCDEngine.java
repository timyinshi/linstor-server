package com.linbit.linstor.dbdrivers.etcd;

import static com.ibm.etcd.client.KeyUtils.bs;

import com.linbit.ImplementationError;
import com.linbit.InvalidIpAddressException;
import com.linbit.InvalidNameException;
import com.linbit.SingleColumnDatabaseDriver;
import com.linbit.ValueOutOfRangeException;
import com.linbit.linstor.LinStorDBRuntimeException;
import com.linbit.linstor.dbdrivers.AbsDatabaseDriver;
import com.linbit.linstor.dbdrivers.DatabaseDriverInfo.DatabaseType;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.dbdrivers.DbEngine;
import com.linbit.linstor.dbdrivers.GeneratedDatabaseTables.Column;
import com.linbit.linstor.dbdrivers.GeneratedDatabaseTables.Table;
import com.linbit.linstor.logging.ErrorReporter;
import com.linbit.linstor.security.AccessDeniedException;
import com.linbit.linstor.stateflags.Flags;
import com.linbit.linstor.stateflags.StateFlagsPersistence;
import com.linbit.linstor.transaction.TransactionMgrETCD;
import com.linbit.utils.ExceptionThrowingFunction;
import com.linbit.utils.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import com.google.inject.Provider;
import com.google.protobuf.ByteString;
import com.ibm.etcd.api.DeleteRangeRequest;
import com.ibm.etcd.api.DeleteRangeRequestOrBuilder;
import com.ibm.etcd.api.PutRequest;
import com.ibm.etcd.client.KeyUtils;
import com.ibm.etcd.client.kv.KvClient.FluentTxnOps;

@Singleton
public class ETCDEngine extends BaseEtcdDriver implements DbEngine
{
    private final ErrorReporter errorReporter;

    @Inject
    public ETCDEngine(
        ErrorReporter errorReporterRef,
        Provider<TransactionMgrETCD> transMgrProviderRef
    )
    {
        super(transMgrProviderRef);
        errorReporter = errorReporterRef;
    }

    @Override
    public DatabaseType getType()
    {
        return DatabaseType.ETCD;
    }

    @Override
    public <DATA> void create(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> setters,
        DATA data,
        Table table,
        DataToString<DATA> dataToString
    )
        throws DatabaseException, AccessDeniedException
    {
        FluentTxnOps<?> transaction = transMgrProvider.get().getTransaction();
        for (Column col : table.values())
        {
            if (!col.isPk())
            {
                String key = getColumnKey(setters, col, data);

                PutRequest putRequest = PutRequest.newBuilder().setKey(bs(key)).setValue(
                    bs(Objects.toString(setters.get(col).accept(data)))
                )
                    .build();

                transaction.put(putRequest);
            }
        }
        // sync will be called within transMgr.commit()
    }

    @Override
    public <DATA> void delete(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> setters,
        DATA data,
        Table table,
        DataToString<DATA> dataToString
    )
        throws DatabaseException, AccessDeniedException
    {
        FluentTxnOps<?> transaction = transMgrProvider.get().getTransaction();

        String key = getPk(setters, table, data);

        ByteString bsKey = bs(key);
        DeleteRangeRequestOrBuilder deleteRequest = DeleteRangeRequest.newBuilder()
            .setKey(bsKey)
            .setRangeEnd(KeyUtils.plusOne(bsKey))
            .build();

        transaction.delete(deleteRequest);
        // sync will be called within transMgr.commit()
    }

    @Override
    public <DATA, INIT_MAPS, LOAD_ALL> Map<DATA, INIT_MAPS> loadAll(
        Table table,
        LOAD_ALL parents,
        DataLoader<DATA, INIT_MAPS, LOAD_ALL> dataLoader,
        Function<Object[], AbsDatabaseDriver<DATA, INIT_MAPS, LOAD_ALL>.RawParameters> objsToRawArgs
    )
        throws DatabaseException, AccessDeniedException, InvalidNameException, InvalidIpAddressException,
        ValueOutOfRangeException
    {
        Map<DATA, INIT_MAPS> loadedObjectsMap = new TreeMap<>();
        final Column[] columns = table.values();

        Map<String, String> dataMap = new TreeMap<>(namespace(table).get(true));
        Set<String> composedPkList = EtcdUtils.getComposedPkList(dataMap);
        for (String composedPk : composedPkList)
        {
            Object[] rawObjects = new Object[columns.length];
            String[] pks = composedPk.split(EtcdUtils.PK_DELIMITER);

            int rawIdx = 0;
            for (; rawIdx < pks.length; ++rawIdx)
            {
                rawObjects[rawIdx] = pks[rawIdx];
            }

            for (Column col : columns)
            {
                String colKey = composedPk + EtcdUtils.PATH_DELIMITER + col.getName();
                String colData = dataMap.get(colKey);
                if (colData == null && !col.isNullable())
                {
                    throw new LinStorDBRuntimeException("Column was unexpectedly null. " + colKey);
                }
                rawObjects[rawIdx] = colData;
                ++rawIdx;
            }

            Pair<DATA, INIT_MAPS> pair = dataLoader.loadImpl(objsToRawArgs.apply(rawObjects), parents);
        }

        return loadedObjectsMap;
    }

    private <DATA> String getPk(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> setters,
        Table table,
        DATA data
    )
        throws AccessDeniedException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(EtcdUtils.LINSTOR_PREFIX).append(table.getName()).append(EtcdUtils.PATH_DELIMITER);

        for (Column col : table.values())
        {
            if (col.isPk())
            {
                sb.append(Objects.toString(setters.get(col).accept(data))).append(EtcdUtils.PK_DELIMITER);
            }
        }
        sb.setLength(sb.length() - EtcdUtils.PK_DELIMITER.length()); // cut last PK_DELIMITER
        sb.append(EtcdUtils.PATH_DELIMITER);
        return sb.toString();
    }

    private <DATA> String getColumnKey(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> setters,
        Column col,
        DATA data
    )
        throws AccessDeniedException
    {
        return getPk(setters, col.getTable(), data) + col.getName();
    }

    @Override
    public <DATA, FLAG extends Enum<FLAG> & Flags> StateFlagsPersistence<DATA> generateFlagsDriver(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> settersRef,
        Column colRef,
        Class<FLAG> flagsClassRef,
        DataToString<DATA> idFormatterRef
    )
    {
        // TODO Auto-generated method stub
        throw new ImplementationError("Not implemented yet");
    }

    @Override
    public <DATA, INPUT_TYPE, DB_TYPE> SingleColumnDatabaseDriver<DATA, INPUT_TYPE> generateSingleColumnDriver(
        Map<Column, ExceptionThrowingFunction<DATA, Object, AccessDeniedException>> settersRef,
        Column colRef,
        Function<INPUT_TYPE, DB_TYPE> typeMapperRef,
        DataToString<DATA> dataToStringRef,
        ExceptionThrowingFunction<DATA, String, AccessDeniedException> dataValueToStringRef
    )
    {
        // TODO Auto-generated method stub
        throw new ImplementationError("Not implemented yet");
    }
}
