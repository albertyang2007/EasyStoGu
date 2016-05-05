package org.easystogu.runner;

import java.io.UnsupportedEncodingException;

/**
 * General purpose request parsing and encoding utility methods.
 * 
 */

public final class RequestUtil {
    private static final int SHIFT_FOUR = 4;

    private static final int BYTE = 10;

    // Hiding utility class constructor
    private RequestUtil()
    {
    }

    /**
     * Decode and return the specified URL-encoded String. When the byte array is converted to a string, the system
     * default character encoding is used. This may be different than some other servers.
     * 
     * @param str
     *            The url-encoded string
     * @throws UnsupportedEncodingException
     * 
     * @exception IllegalArgumentException
     *                if a '%' character is not followed by a valid 2-digit hexadecimal number
     */
    public static String urlDecode(String str) throws UnsupportedEncodingException
    {

        return urlDecode(str, null);

    }

    /**
     * Decode and return the specified URL-encoded String.
     * 
     * @param str
     *            The url-encoded string
     * @param enc
     *            The encoding to use; if null, the default encoding is used
     * @throws UnsupportedEncodingException
     * @exception IllegalArgumentException
     *                if a '%' character is not followed by a valid 2-digit hexadecimal number
     */
    @SuppressWarnings("deprecation")
    public static String urlDecode(String str, String enc) throws UnsupportedEncodingException
    {

        if (str == null)
        {
            return null;
        }
        int len = str.length();
        byte[] bytes = new byte[len];
        str.getBytes(0, len, bytes, 0);

        return urlDecode(bytes, enc);

    }

    /**
     * Decode and return the specified URL-encoded byte array.
     * 
     * @param bytes
     *            The url-encoded byte array
     * @throws UnsupportedEncodingException
     * @exception IllegalArgumentException
     *                if a '%' character is not followed by a valid 2-digit hexadecimal number
     */
    public static String urlDecode(byte[] bytes) throws UnsupportedEncodingException
    {
        return urlDecode(bytes, null);
    }

    /**
     * Decode and return the specified URL-encoded byte array.
     * 
     * @param bytes
     *            The url-encoded byte array
     * @param enc
     *            The encoding to use; if null, the default encoding is used
     * @throws UnsupportedEncodingException
     * @exception IllegalArgumentException
     *                if a '%' character is not followed by a valid 2-digit hexadecimal number
     */
    public static String urlDecode(byte[] bytes, String enc) throws UnsupportedEncodingException
    {
        if (bytes == null)
        {
            return null;
        }

        int len = bytes.length;
        int ix = 0;
        int ox = 0;
        while (ix < len)
        {
            byte b = bytes[ix++]; // Get byte to test
            /*
             * if (b == '+') { // b = (byte) ' '; } else
             */
            if (b == '%')
            {
                b = (byte) ((convertHexDigit(bytes[ix++]) << SHIFT_FOUR) + convertHexDigit(bytes[ix++]));
            }
            bytes[ox++] = b;
        }
        String decodedUrlString;
        if (enc != null)
        {
            decodedUrlString = new String(bytes, 0, ox, enc);
        }
        else
        {
            decodedUrlString = new String(bytes, 0, ox);
        }
        return decodedUrlString;
    }

    /**
     * Convert a byte character value to hexadecimal digit value.
     * 
     * @param b
     *            the character value byte
     */
    private static byte convertHexDigit(byte b)
    {
        if (b >= '0' && b <= '9')
        {
            return (byte) (b - '0');
        }
        if (b >= 'a' && b <= 'f')
        {
            return (byte) (b - 'a' + BYTE);
        }
        if (b >= 'A' && b <= 'F')
        {
            return (byte) (b - 'A' + BYTE);
        }

        return 0;

    }

}
