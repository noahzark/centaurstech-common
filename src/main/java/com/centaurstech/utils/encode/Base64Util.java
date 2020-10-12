package com.centaurstech.utils.encode;

public class Base64Util {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //Base64解码
    public static byte[] decode(String str) {
        return java.util.Base64.getDecoder().decode(str);
    }

    //Base64编码
    public static String encode(final byte[] bytes) {
        return new String(java.util.Base64.getEncoder().encode(bytes));
    }

}
