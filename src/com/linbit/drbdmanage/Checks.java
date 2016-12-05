package com.linbit.drbdmanage;

import com.linbit.ImplementationError;
import com.linbit.ValueOutOfRangeException;

/**
 * Universal validity checks
 *
 * @author Robert Altnoeder &lt;robert.altnoeder@linbit.com&gt;
 */
public class Checks
{
    public static final int HOSTNAME_MIN_LENGTH = 2;
    public static final int HOSTNAME_MAX_LENGTH = 255;
    public static final int HOSTNAME_LABEL_MAX_LENGTH = 63;

    public static final int MINOR_NR_MIN = 0;
    public static final int MINOR_NR_MAX = 1 << 19;

    public static final int VOLUME_NR_MIN = 0;

    // FIXME: VOLUME_NR_MAX is probably something else around ~65530
    //        Check DRBD kernel module for the correct value
    public static final int VOLUME_NR_MAX = (1 << 16) - 2;

    private static final String MINOR_NR_EXC_FORMAT =
        "Minor number %d is out of range [%d - %d]";

    private static final String VOLUME_NR_EXC_FORMAT =
        "Volume number %d is out of range [%d - %d]";

    private static final String RANGE_EXC_FORMAT =
        "Value %d is out of range [%d - %d]";

    /**
     * Universal name check
     *
     * @param name The name to check for validity
     * @param minLength Allowed minimum length of the name
     * @param maxLength Allowed maximum length of the name
     * @param validChars Letters allowed anywhere in the name
     * @param validInnerChars Letters allowed after the first letter in the name
     * @throws InvalidNameException If the name is not valid
     */
    public static void nameCheck(
        String name,
        int minLength,
        int maxLength,
        byte[] validChars,
        byte[] validInnerChars
    ) throws InvalidNameException
    {
        if (name == null)
        {
           throw new ImplementationError(
               "Method called with name == null",
               new NullPointerException()
           );
        }

        byte[] nameBuffer = name.getBytes();

        if (minLength < 1)
        {
            throw new ImplementationError(
                "Method called with minLength < 1",
                new IllegalArgumentException()
            );
        }

        // Length check
        if (nameBuffer.length < minLength)
        {
            throw new InvalidNameException(
                String.format(
                    "Invalid name: Name length %d is less than minimum length %d",
                    nameBuffer.length, minLength
                )
            );
        }
        if (nameBuffer.length > maxLength)
        {
            throw new InvalidNameException(
                String.format(
                    "Invalid name: Name length %d is greater than maximum length %d",
                    nameBuffer.length, maxLength
                )
            );
        }

        // Check for the presence of alpha-numeric characters
        {
            boolean alpha = false;
            for (int idx = 0; idx < nameBuffer.length; ++idx)
            {
                byte letter = nameBuffer[idx];
                if ((letter >= 'a' && letter <= 'z') ||
                    (letter >= 'A' && letter <= 'Z'))
                {
                    alpha = true;
                    break;
                }
            }
            if (!alpha)
            {
                throw new InvalidNameException(
                    "Invalid name: Name must contain at least one character that matches [a-zA-Z]"
                );
            }
        }

        // First character validity check
        {
            byte letter = nameBuffer[0];
            if (!((letter >= 'a' && letter <= 'z') ||
                (letter >= 'A' && letter <= 'Z')))
            {
                int vIdx = 0;
                while (vIdx < validChars.length)
                {
                    if (letter == validChars[vIdx])
                    {
                        break;
                    }
                    ++vIdx;
                }
                if (vIdx >= validChars.length)
                {
                    throw new InvalidNameException(
                        String.format(
                            "Invalid name: Cannot begin with character '%c'",
                            (char) letter
                        )
                    );
                }
            }
        }

        // Remaining characters validity check
        for (int idx = 1; idx < nameBuffer.length; ++idx)
        {
            byte letter = nameBuffer[idx];
            if (!((letter >= 'a' && letter <= 'z') ||
                (letter >= 'A' && letter <= 'Z') ||
                (letter >= '0' && letter <= '9')))
            {
                int vIdx = 0;
                while (vIdx < validInnerChars.length)
                {
                    if (letter == validInnerChars[vIdx])
                    {
                        break;
                    }
                    ++vIdx;
                }
                if (vIdx >= validInnerChars.length)
                {
                    throw new InvalidNameException(
                        String.format(
                            "Invalid name: Cannot contain character '%c'",
                            (char) letter
                        )
                    );
                }
            }
        }
    }

    /**
     * RFC952 / RFC1035 / RFC1123 internet host name validity check
     *
     * @param name The hostname to check for validity
     * @throws InvalidNameException If the hostname is not valid
     */
    public static void hostNameCheck(String name) throws InvalidNameException
    {
        if (name == null)
        {
            throw new ImplementationError(
                "Method called with name == null",
                new NullPointerException()
            );
        }

        // First & last character special treatment check
        if (name.startsWith("."))
        {
            throw new InvalidNameException(
                "Hostname cannot begin with '.'"
            );
        }
        if (name.startsWith("-"))
        {
            throw new InvalidNameException(
                "Hostname cannot begin with '-'"
            );
        }
        if (name.endsWith("-"))
        {
            throw new InvalidNameException(
                "Hostname cannot end with '-'"
            );
        }

        byte[] nameBuffer = name.getBytes();

        // Length check
        if (nameBuffer.length < HOSTNAME_MIN_LENGTH)
        {
            throw new InvalidNameException(
                String.format(
                    "Hostname length of %d violates RFC1123 minimum length of %d",
                    nameBuffer.length, HOSTNAME_MIN_LENGTH
                )
            );
        }
        if (nameBuffer.length > HOSTNAME_MAX_LENGTH)
        {
            throw new InvalidNameException(
                String.format(
                    "Hostname length of %d violates RFC1123 maximum length of %d",
                    nameBuffer.length, HOSTNAME_MAX_LENGTH
                )
            );
        }

        // Individual domain name components length check
        {
            int labelLength = 0;
            for (int idx = 0; idx < nameBuffer.length; ++idx)
            {
                byte letter = nameBuffer[idx];
                if (letter == '.')
                {
                    labelLength = 0;
                }
                else
                {
                    ++labelLength;
                    if (labelLength > HOSTNAME_LABEL_MAX_LENGTH)
                    {
                        throw new InvalidNameException(
                            String.format(
                                "Domain name component length of %d violates RFC1123 maximum length of %d",
                                labelLength, HOSTNAME_LABEL_MAX_LENGTH
                            )
                        );
                    }
                    if (!((letter >= 'a' && letter <= 'z') ||
                        (letter >= 'A' && letter <= 'Z') ||
                        (letter >= '0' && letter <= '9') ||
                        (letter == '.' || letter == '-')))
                    {
                        throw new InvalidNameException(
                            String.format(
                                "Domain name cannot contain character '%c'",
                                (char) letter
                            )
                        );
                    }
                }
            }
        }
    }

    /**
     * Checks whether a value is within the range [minValue, maxValue]
     *
     * @param value The value to check
     * @param minValue Allowed minimum value
     * @param maxValue Allowed maximum value
     * @throws ValueOutOfRangeException If the value is out of range
     */
    public static void rangeCheck(long value, long minValue, long maxValue)
        throws ValueOutOfRangeException
    {
        genericRangeCheck(value, minValue, maxValue, RANGE_EXC_FORMAT);
    }

    /**
     * Checks whether a value is within the range [minValue, maxValue], and
     * uses the specified format to generate an exception with a message
     * describing the problem
     *
     * @param value The value to check
     * @param minValue Allowed minimum value
     * @param maxValue Allowed maximum value
     * @throws ValueOutOfRangeException If the value is out of range
     */
    private static void genericRangeCheck(
        long value,
        long minValue, long maxValue,
        String excFormat
    ) throws ValueOutOfRangeException
    {
        if (minValue > maxValue)
        {
            throw new ImplementationError(
                String.format(
                    "Impossible range: Method called with minValue %d > maxValue %d",
                    minValue, maxValue
                ),
                new IllegalArgumentException()
            );
        }

        if (value < minValue)
        {
            throw new ValueOutOfRangeException(
                String.format(
                    excFormat,
                    value, minValue, maxValue
                ),
                ValueOutOfRangeException.ViolationType.TOO_LOW
            );
        }
        if (value > maxValue)
        {
            throw new ValueOutOfRangeException(
                String.format(
                    excFormat,
                    value, minValue, maxValue
                ),
                ValueOutOfRangeException.ViolationType.TOO_HIGH
            );
        }
    }

    /**
     * Checks the validity of a DRBD volume number
     *
     * @param volNr The volume number to check
     * @throws ValueOutOfRangeException If the volume number is out of range
     */
    public static void volumeNrCheck(int volNr) throws ValueOutOfRangeException
    {
        genericRangeCheck(volNr, VOLUME_NR_MIN, VOLUME_NR_MAX, VOLUME_NR_EXC_FORMAT);
    }

    /**
     * Checks the validity of a UNIX minor number
     *
     * @param minorNr The minor number to check
     * @throws ValueOutOfRangeException If the minor number is out of range
     */
    public static void minorNrCheck(int minorNr) throws ValueOutOfRangeException
    {
        genericRangeCheck(minorNr, MINOR_NR_MIN, MINOR_NR_MAX, MINOR_NR_EXC_FORMAT);
    }
}
