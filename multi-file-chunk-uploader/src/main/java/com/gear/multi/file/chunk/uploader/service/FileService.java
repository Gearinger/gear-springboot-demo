package com.gear.multi.file.chunk.uploader.service;

import com.gear.multi.file.chunk.uploader.config.ChunkConfig;
import com.gear.multi.file.chunk.uploader.dto.ChunkRecordDTO;
import com.gear.multi.file.chunk.uploader.dto.ChunkWithFileRecordDTO;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final ChunkConfig chunkConfig;

    private final IChunkRecordService uploadRecordService;

    Logger logger = LoggerFactory.getLogger(FileService.class);

    public FileService(ChunkConfig chunkConfig, IChunkRecordService uploadRecordService) {
        this.chunkConfig = chunkConfig;
        this.uploadRecordService = uploadRecordService;
    }

    /**
     * 检查
     *
     * @param chunkRecordDTO 块dto
     * @return boolean
     */
    public boolean check(ChunkRecordDTO chunkRecordDTO) {
        List<ChunkRecordDTO> chunkRecordDTOS = uploadRecordService.listUploadRecord(chunkRecordDTO.getIdentifier());
        if (CollectionUtils.isEmpty(chunkRecordDTOS)) {
            return false;
        }
        int uploadedChunkCount = chunkRecordDTOS.size();
        if (uploadedChunkCount != chunkRecordDTO.getTotalChunks()) {
            return false;
        }
        Optional<ChunkRecordDTO> result = chunkRecordDTOS.stream().filter(p -> p.getFilename().equals(chunkRecordDTO.getFilename())).findFirst();
        return result.isPresent();
    }

    /**
     * 上传
     *
     * @param chunkWithFileDTO 块dto
     * @throws IOException IOException
     */
    public void upload(ChunkWithFileRecordDTO chunkWithFileDTO) throws IOException {
        String uploadDir = chunkConfig.getUploadDir();
        String identifier = chunkWithFileDTO.getIdentifier();
        String chunkFileName = identifier + chunkWithFileDTO.getChunkNumber();
        File targetFile = new File(uploadDir, chunkFileName);
        try (InputStream inputStream = chunkWithFileDTO.getFile().getInputStream();
             FileOutputStream outputStream = new FileOutputStream(targetFile)
        ) {
            IOUtils.copy(inputStream, outputStream);
            logger.info("文件标识:{},chunkNumber:{}", identifier, chunkWithFileDTO.getChunkNumber());
        }
        uploadRecordService.saveUploadRecord(chunkWithFileDTO.toChunkRecord());

        List<ChunkRecordDTO> chunkRecordDTOS = uploadRecordService.listUploadRecord(identifier);
        File mergeFile = new File(uploadDir, chunkWithFileDTO.getFilename());
        if (!CollectionUtils.isEmpty(chunkRecordDTOS) && chunkRecordDTOS.size() >= chunkRecordDTOS.get(0).getTotalChunks()) {
            mergeFile(mergeFile, chunkRecordDTOS);
        }
    }

    private void mergeFile(File mergeFile, List<ChunkRecordDTO> chunkRecordDTOS) throws IOException {
        try (RandomAccessFile randomAccessFileWriter = new RandomAccessFile(mergeFile, "rw")) {
            logger.info("开始合并文件");
            chunkRecordDTOS.sort(Comparator.comparing(ChunkRecordDTO::getChunkNumber));
            byte[] bytes = new byte[5120];
            for (ChunkRecordDTO chunk : chunkRecordDTOS) {
                File chunkFile = new File(chunkConfig.getUploadDir(), chunk.getIdentifier() + chunk.getChunkNumber());
                try (RandomAccessFile randomAccessFileReader = new RandomAccessFile(chunkFile, "r");) {
                    int len;
                    while ((len = randomAccessFileReader.read(bytes)) != -1) {
                        randomAccessFileWriter.write(bytes, 0, len);
                    }
                }
            }
            logger.info("合并文件完成");
        }
    }

}
