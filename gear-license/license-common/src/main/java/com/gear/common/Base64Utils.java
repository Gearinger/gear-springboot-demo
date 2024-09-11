package com.gear.common;


/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar 或 common-codec
 * </p>
 *
 * @author IceWee
 * @version 1.0
 * @date 2012-5-19
 */
public class Base64Utils {

    /** */
    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;


    public static String decode(String base64) throws Exception {
        return new String(new BASE64Decoder().decodeBuffer(base64));
    }

    public static byte[] decodeToBytes(String base64) throws Exception {
        return new BASE64Decoder().decodeBuffer(base64);
    }

    public static String encode(String text) throws Exception {
        return new BASE64Encoder().encode(text.getBytes());
    }

    public static String encodeFromBytes(byte[] bytes) throws Exception {
        return new BASE64Encoder().encode(bytes);
    }
}