package com.linbit.linstor.numberpool;

import com.linbit.ExhaustedPoolException;
import com.linbit.ValueInUseException;
import com.linbit.ValueOutOfRangeException;

/**
 * Wrapper for a {@link NumberPool} which allows the range to be reloaded.
 */
public interface DynamicNumberPool
{
    /**
     * Caller must have write-locked the reconfigurationLock
     */
    void reloadRange();

    int getRangeMin();

    int getRangeMax();

    void allocate(int nr)
        throws ValueOutOfRangeException, ValueInUseException;

    int autoAllocate()
        throws ExhaustedPoolException;
}
