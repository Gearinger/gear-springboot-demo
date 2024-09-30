package com.gear.sqlite.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String getExt(File file) {
        return getExt(file.getName());
    }

    public static String getExt(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public static String md5(File file) {
        try {
            return md5(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        }
    }

    public static String md5(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        }
    }

}
