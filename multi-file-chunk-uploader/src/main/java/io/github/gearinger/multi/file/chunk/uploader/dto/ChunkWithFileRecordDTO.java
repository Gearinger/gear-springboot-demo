package io.github.gearinger.multi.file.chunk.uploader.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 块dto
 *
 * @author guoyingdong
 * @date 2024/12/24
 */
@Data
public class ChunkWithFileRecordDTO {
    private Long chunkId;

    private String chunkNumber;

    private Long currentChunkSize;

    private Long chunkSize;

    private Long totalChunks;

    private Long totalSize;

    private String relativePath;

    private String filename;

    private String identifier;

    private MultipartFile file;

    public ChunkRecordDTO toChunkRecord(){
        ChunkRecordDTO chunkRecordDTO = new ChunkRecordDTO();
        chunkRecordDTO.setChunkId(chunkId);
        chunkRecordDTO.setChunkNumber(chunkNumber);
        chunkRecordDTO.setCurrentChunkSize(currentChunkSize);
        chunkRecordDTO.setChunkSize(chunkSize);
        chunkRecordDTO.setTotalChunks(totalChunks);
        chunkRecordDTO.setTotalSize(totalSize);
        chunkRecordDTO.setRelativePath(relativePath);
        chunkRecordDTO.setFilename(filename);
        chunkRecordDTO.setIdentifier(identifier);
        return chunkRecordDTO;
    }
}
