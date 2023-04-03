import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * mybatis-plus 生成
 *
 * @author GuoYingdong
 * @date 2022/02/18
 */
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:postgresql://127.0.0.1:5432/test", "postgres", "postgres")
                .globalConfig(builder -> {
                    builder.author("GuoYingdong") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            // ~~~~~~~~需修改~~~~~~~
                            .outputDir(".\\demo\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.gear") // groupId
                            .moduleName("demo") // artifactId
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, ".\\demo\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 设置需要生成的表名
                            .addTablePrefix("") // 设置过滤表前缀
                            .controllerBuilder()
                            .enableRestStyle()
                            .entityBuilder()
                            .enableLombok();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }
}
