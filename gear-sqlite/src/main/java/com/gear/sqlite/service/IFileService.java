package com.gear.sqlite.service;

import com.gear.sqlite.db.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IFileService {
    FileEntity add(MultipartFile file) throws IOException;

    List<FileEntity> list();

    FileEntity getById(Integer id);

    void delete(Integer id);

    void download(HttpServletResponse response, Integer id) throws IOException;
}
