package com.gear.multi.file.chunk.uploader.controller;

import com.gear.multi.file.chunk.uploader.dto.ChunkRecordDTO;
import com.gear.multi.file.chunk.uploader.dto.ChunkWithFileRecordDTO;
import com.gear.multi.file.chunk.uploader.service.FileService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 上传控制器
 *
 * @author guoyingdong
 * @date 2024/12/24
 */
@RequestMapping("/file")
@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/check")
    public boolean check(ChunkRecordDTO chunkRecordDTO) throws IOException {
        return fileService.check(chunkRecordDTO);
    }

    @PostMapping("/upload")
    public void upload(ChunkWithFileRecordDTO chunkWithFileDTO) throws IOException {
        fileService.upload(chunkWithFileDTO);
    }

}
