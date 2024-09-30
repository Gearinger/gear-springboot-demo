package com.gear.sqlite.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file")
public class FileEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    private String fileName;

    private String filePath;

    private String fileType;

    private String fileFormat;

    private Long fileSize;

    private String md5;

}
