package io.github.gearinger.multi.file.chunk.uploader.service.impl;

import io.github.gearinger.multi.file.chunk.uploader.dto.ChunkRecordDTO;
import io.github.gearinger.multi.file.chunk.uploader.service.IChunkRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 上传记录内存服务impl
 *
 * @author guoyingdong
 * @date 2024/12/25
 */
@Service
@ConditionalOnProperty(prefix = "file.chunk", name = "record-save-type", havingValue = "memory", matchIfMissing = true)
public class ChunkRecordMemmoryServiceImpl implements IChunkRecordService {

    Logger logger = LoggerFactory.getLogger(ChunkRecordMemmoryServiceImpl.class);

    private static ConcurrentLinkedQueue<ChunkRecordDTO> queue = new ConcurrentLinkedQueue<>();

    private static List<ChunkRecordDTO> list = new ArrayList<>();

    /**
     * 保存上传记录
     *
     * @param chunkRecordDTO 上传记录数据到
     * @return boolean
     */
    @Override
    public boolean saveUploadRecord(ChunkRecordDTO chunkRecordDTO) {
        try {
            list.add(chunkRecordDTO);
            return true;
        } catch (Exception e) {
            logger.error("保存分片上传记录异常", e);
            return false;
        }
    }

    /**
     * 获取上传记录
     *
     * @param identifier 标识符
     * @return {@link List }<{@link ChunkRecordDTO }>
     */
    @Override
    public List<ChunkRecordDTO> listUploadRecord(String identifier) {
        return list.stream()
                .filter(p -> p.getIdentifier().equals(identifier))
                .collect(Collectors.toList());
    }

}
