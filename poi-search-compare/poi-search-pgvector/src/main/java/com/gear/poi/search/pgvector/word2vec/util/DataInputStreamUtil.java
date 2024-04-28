package com.gear.poi.search.pgvector.word2vec.util;

import java.io.DataInputStream;
import java.io.IOException;

public class DataInputStreamUtil {

    private static final int MAX_SIZE = 50;


    /**
     * 读取一个字符串
     *
     * @param dis
     * @return
     * @throws IOException
     */
    public static String readString(DataInputStream dis) throws IOException {
        // TODO Auto-generated method stub
        byte[] bytes = new byte[MAX_SIZE];
        byte b = dis.readByte();
        int i = -1;
        StringBuilder sb = new StringBuilder();
        while (b != 32 && b != 10) {
            i++;
            bytes[i] = b;
            b = dis.readByte();
            if (i == 49) {
                sb.append(new String(bytes, "UTF-8"));
                i = -1;
                bytes = new byte[MAX_SIZE];
            }
        }
        sb.append(new String(bytes, 0, i + 1, "UTF-8"));
        return sb.toString();
    }
}
