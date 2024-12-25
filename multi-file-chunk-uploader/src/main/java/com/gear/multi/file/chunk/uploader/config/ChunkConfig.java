package com.gear.multi.file.chunk.uploader.config;

import com.gear.multi.file.chunk.uploader.RecordSaveTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 块配置
 *
 * @author guoyingdong
 * @date 2024/12/24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.chunk")
public class ChunkConfig {

    /**
     * 上传目录
     */
    private String uploadDir = "./uploadData/";

    /**
     * 记录保存类型
     */
    private RecordSaveTypeEnum recordSaveType = RecordSaveTypeEnum.MEMORY;


    @PostConstruct
    public void init() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
