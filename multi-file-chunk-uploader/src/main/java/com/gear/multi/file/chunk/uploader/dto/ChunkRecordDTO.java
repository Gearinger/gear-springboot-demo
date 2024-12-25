package com.gear.multi.file.chunk.uploader.dto;

import lombok.Data;

/**
 * 块dto
 *
 * @author guoyingdong
 * @date 2024/12/24
 */
@Data
public class ChunkRecordDTO {

    private Long chunkId;

    private String chunkNumber;

    private Long currentChunkSize;

    private Long chunkSize;

    private Long totalChunks;

    private Long totalSize;

    private String relativePath;

    private String filename;

    private String identifier;

}
