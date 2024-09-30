package com.gear.sqlite.controller;

import com.gear.sqlite.db.FileEntity;
import com.gear.sqlite.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final IFileService fileService;

    @PostMapping("/upload")
    public FileEntity upload(@RequestPart MultipartFile file) throws IOException {
        return fileService.add(file);
    }

    @GetMapping("/list")
    public List<FileEntity> list(){
        return fileService.list();
    }

    @GetMapping("/getInfoById")
    public FileEntity getInfoById(Integer id){
        return fileService.getById(id);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, Integer id) throws IOException {
        fileService.download(response, id);
    }

    @PostMapping("/delete")
    public void delete(Integer id){
        fileService.delete(id);
    }
}
