package io.github.gearinger.multi.file.chunk.uploader.service;

import io.github.gearinger.multi.file.chunk.uploader.dto.ChunkRecordDTO;

import java.util.List;

public interface IChunkRecordService {

    boolean saveUploadRecord(ChunkRecordDTO chunkRecordDTO);

    List<ChunkRecordDTO> listUploadRecord(String identifier);

}
