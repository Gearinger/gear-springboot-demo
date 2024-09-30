package com.gear.sqlite.service.impl;

import com.gear.sqlite.config.SystemConfig;
import com.gear.sqlite.db.FileEntity;
import com.gear.sqlite.mapper.FileMapper;
import com.gear.sqlite.service.IFileService;
import com.gear.sqlite.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

    private final SystemConfig systemConfig;

    private final FileMapper fileMapper;

    @Override
    public FileEntity add(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名不能为空");
        }
        String fileFormat = FileUtil.getExt(originalFilename);
        String fileType;
        fileType = parseFileType(fileFormat);
        String md5Hex = FileUtil.md5(file.getInputStream());
        String filePath = "/" + fileType + "/" + fileFormat;
        File targetPath = new File(systemConfig.getStoragePath(), filePath);
        File targetFile = new File(targetPath.getCanonicalPath(), md5Hex);
        mkdirs(targetPath);
        saveFile(file, targetFile);
        log.info("文件上传成功: {}", targetPath);

        FileEntity entity = new FileEntity();
        entity.setFileFormat(fileFormat);
        entity.setFileType(fileType);
        entity.setFileName(originalFilename);
        entity.setFilePath(filePath + "/" + md5Hex);
        entity.setFileSize(file.getSize());
        entity.setMd5(md5Hex);
        return fileMapper.save(entity);
    }

    private static void mkdirs(File targetPath) {
        if (!targetPath.exists()) {
            boolean mkdirs = targetPath.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建目录失败");
            }
        }
    }

    private static void saveFile(MultipartFile file, File targetFile) {
        if (!targetFile.exists()) {
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                throw new RuntimeException("文件上传失败", e);
            }
        }
    }

    private static String parseFileType(String fileFormat) {
        String fileType;
        switch (fileFormat) {
            case "glb":
            case "gltf":
                fileType = ("model");
                break;
            case "jpg":
            case "png":
                fileType = ("image");
                break;
            default:
                throw new RuntimeException("不支持的文件类型");
        }
        return fileType;
    }

    @Override
    public List<FileEntity> list() {
        return fileMapper.findAll();
    }

    @Override
    public FileEntity getById(Integer id) {
        return fileMapper.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        FileEntity entity = getById(id);
        long l = countByMd5(entity);
        if (l <= 1) {
            boolean delete = new File(systemConfig.getStoragePath(), entity.getFilePath()).delete();
            if (!delete) {
                log.warn("删除文件失败: {}", entity);
            }
        }
        fileMapper.deleteById(id);
    }

    private long countByMd5(FileEntity entity) {
        FileEntity entity2 = new FileEntity();
        entity2.setMd5(entity.getMd5());
        return fileMapper.count(Example.of(entity2));
    }

    @Override
    public void download(HttpServletResponse response, Integer id) throws IOException {
        FileEntity fileEntity = getById(id);

        response.reset();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        String utf8FileName = URLEncoder.encode(fileEntity.getFileName(), StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + utf8FileName);

        File file = new File(systemConfig.getStoragePath(), fileEntity.getFilePath());
        try (ServletOutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file);) {
            int count = 0;
            byte[] by = new byte[1024];
            //通过response对象获取OutputStream流
            while ((count = fileInputStream.read(by)) != -1) {
                outputStream.write(by, 0, count);
            }
            outputStream.flush();
        }

    }
}
