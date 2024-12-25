## 概要

基于 SpringBoot + vue-simple-uploader，实现的文件上传模块。

* 支持文件、多文件、文件夹上传

* 支持拖拽文件、文件夹上传

* 统一对待文件和文件夹，方便操作管理

* 可暂停、继续上传

* 错误处理

* 支持“快传”，通过文件判断服务端是否已存在从而实现“快传”

* 上传队列管理，支持最大并发上传

* 分块上传

* 支持进度、预估剩余时间、出错自动重试、重传等操作


## 快速开始

### 此项目启动

启动项目，访问 http://localhost:3000/upload-test.html ，即可测试文件上传

<img src="data:image/png;base64,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QskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZAwAAAAAAKJSMAQAAAAAAFErGAAAAAAAACiVjAAAAAAAAhZIxAAAAAACAQskYAAAAAABAoWQMAAAAAACgUDIGAAAAAABQKBkDAAAAAAAolIwBAAAAAAAUSsYAAAAAAAAKJWMAAAAAAACFkjEAAAAAAIBCyRgAAAAAAEChZAwAAAAAAKBQMgYAAAAAAFAoGQMAAAAAACiUjAEAAAAAABRKxgAAAAAAAAolYwAAAAAAAIWSMQAAAAAAgELJGAAAAAAAQKFkDAAAAAAAoFAyBgAAAAAAUCgZA/gb9uxYAAAAAGCQv/U0dpRGAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAAAAAYEpjAAAAAAAAUxoDAAAAAACY0hgAAAAAAMCUxgAAAAAAAKY0BgAAAAAAMKUxAAAAAACAKY0BAAAAAABMaQwAAAAAAGBKYwAAAAAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgCmNAQAAAAAATGkMAAAAAABgSmMAAAAAAABTGgMAAAAAAJjSGAAAAAAAwJTGAAAAAAAApjQGAAAAAAAwpTEAAAAAAIApjQEAAAAAAExpDAAAoPbsWAAAAABgkL/1NHaURgAAAFMaAwAAAAAAmNIYAAAAAADAlMYAAAAAAACmNAYAAAAAADClMQAAAAAAgKkADeP1pj6i2RIAAAAASUVORK5CYII=">

### SpringBoot 项目集成

pom.xml 引入依赖

```xml
<dependency>
    <groupId>io.github.gearinger</groupId>
    <artifactId>multi-file-chunk-uploader</artifactId>
    <version>0.0.1</version>
</dependency>
```

启动服务，访问 http://localhost:port/upload-test.html
