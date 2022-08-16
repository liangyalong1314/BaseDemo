package com.lyl.baselibrary.utils;

public class PortConversionUtils {



    /**
     * Hex转byte[]，两种情况，Hex长度为奇数最后一个字符会被舍去
     */
    public static byte[] hexTobytes(String hex) {
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for (int i = 0; i < hex.length(); i += 2) {
                result[j++] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            }
            return result;
        }
    }

    /**
     * 十六进制转byte 加异或
     * @param hex
     * @return
     */
    public static byte[] hexToBytesAddXor(String hex) {
        byte[] data = hexTobytes(hex);
        byte xor = 0;
        for (int i = 0; i < data.length; i++) {
            xor ^= data[i];
        }

        return hexTobytes(hex + byteToHex(xor));
    }

    /**
     * 计算异或
     * @param data
     * @return
     */
    public static boolean calculateXor(byte[] data) {
        byte xor = 0;
        for (int i = 0; i < data.length - 1; i++) {
            xor ^= data[i];
        }

        return xor == data[data.length - 1];
    }
    /**
     * 单字节转十六进制
     * @param src
     * @return
     */
    public static String byteToHex(byte src) {

        String hv = Integer.toHexString(src & 0xFF).toUpperCase();
        if (hv.length() < 2) {
            return "0" + hv;
        }
        return hv;

    }

    /**
     * 字节数组转16进制
     * @param src
     * @return
     */
    public static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
