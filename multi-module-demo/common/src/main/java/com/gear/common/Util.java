package com.gear.common;

import jakarta.activation.MimetypesFileTypeMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class Util {
    /**
     * 将本地文件转换成流，并添加到 response 中
     *
     * @param filePath 文件路径
     * @throws IOException ioexception
     */
    public static void fileStreamToResponse(String filePath) throws IOException {
        File file = new File(filePath);
        String fileName = file.getName();

        // 设置返回内容
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(filePath);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type", type);
        response.setCharacterEncoding("utf-8");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

        try ( OutputStream outputStream = response.getOutputStream()){
            byte[] buff = new byte[1024];
            // 读取filename
            try (BufferedInputStream bis= new BufferedInputStream(new FileInputStream(filePath))){
                int i = bis.read(buff);
                while (i != -1) {
                    outputStream.write(buff, 0, buff.length);
                    outputStream.flush();
                    i = bis.read(buff);
                }
            }
        }
    }
}
