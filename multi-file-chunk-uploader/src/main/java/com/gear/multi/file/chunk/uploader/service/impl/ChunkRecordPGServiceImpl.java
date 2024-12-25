package com.gear.multi.file.chunk.uploader.service.impl;

import com.gear.multi.file.chunk.uploader.dto.ChunkRecordDTO;
import com.gear.multi.file.chunk.uploader.service.IChunkRecordService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上传记录内存服务impl
 *
 * @author guoyingdong
 * @date 2024/12/25
 */
@Service
@ConditionalOnProperty(prefix = "file.chunk", name = "record-save-type", havingValue = "pg")
public class ChunkRecordPGServiceImpl implements IChunkRecordService {

    /**
     * 保存上传记录
     *
     * @param uploadRecordDTO 上传记录数据到
     * @return boolean
     */
    @Override
    public boolean saveUploadRecord(ChunkRecordDTO uploadRecordDTO) {
        throw new RuntimeException();
    }

    /**
     * 获取上传记录
     *
     * @param identifier 标识符
     * @return {@link ChunkRecordDTO }
     */
    @Override
    public List<ChunkRecordDTO> listUploadRecord(String identifier) {
        throw new RuntimeException();
    }

}
