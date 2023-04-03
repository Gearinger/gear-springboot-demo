package com.gear.minio.dto;

import lombok.Data;

@Data
public class FileInfo {
    private String filename;
    private String ext;
    private Long size;
    private Boolean isDirectory;
}