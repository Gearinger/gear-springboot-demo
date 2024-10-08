package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
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
    public Result<FileEntity> upload(@RequestPart MultipartFile file) throws IOException {
        FileEntity add = fileService.add(file);
        return Result.success(add);
    }

    @GetMapping("/list")
    public Result<List<FileEntity>> list(){
        List<FileEntity> list = fileService.list();
        return Result.success(list);
    }

    @GetMapping("/getInfoById")
    public Result<FileEntity> getInfoById(Integer id){
        FileEntity fileEntity = fileService.getById(id);
        return Result.success(fileEntity);
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
