import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * mybatis-plus 生成
 *
 * @author GuoYingdong
 * @date 2022/02/18
 */
public class MybatisPlusGenerator {
    static String dbUrl = "jdbc:postgresql://192.168.10.96:5432/agricultural_data_service";
    static String dbUsername = "docker";
    static String dbPassword = "docker";
    /**
     * 输出代码的路径
     */
//    static String outputDir = ".\\business\\src\\main\\java";
    static String outputDir = "D:\\MyWork\\Project\\农业数据平台\\Program\\agricultural-data-service\\src\\main\\java";

    /**
     * 输出 mapper.xml 代码的路径
     */
//    static String outputMapperDir = ".\\business\\src\\main\\resources\\mapper";
    static String outputMapperDir = "D:\\MyWork\\Project\\农业数据平台\\Program\\agricultural-data-service\\src\\main\\resources\\mapper";

    static List<String> tableNameList = Arrays.asList(
            "crop",
            "disaster_check",
            "disaster_field",
            "farmar",
            "field",
            "project",
            "region",
            "sys_setting",
            "sys_user"
    );

    static String tablePrefix = "";

    public static void main(String[] args) throws Exception {
        System.out.println("Confirm to generate code from tables: \n" + tableNameList + "\n(y/n)?");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            String next = scanner.next();
            if ("y".equalsIgnoreCase(next)) {
                File file = new File(outputDir);
                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();
                    if (!mkdirs){
                        throw new Exception("创建输出文件夹失败！");
                    }
                }
                FastAutoGenerator.create(dbUrl, dbUsername, dbPassword)
                        .globalConfig(builder -> {
                            builder.author("GuoYingdong") // 设置作者
//                                    .enableSwagger() // 开启 swagger 模式
                                    .fileOverride() // 覆盖已生成文件
                                    .outputDir(outputDir); // 指定输出目录
                        })
                        .packageConfig(builder -> {
                            builder.parent("com.xag") // groupId
                                    .moduleName("agricultural") // artifactId
                                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, outputMapperDir)); // 设置mapperXml生成路径
                        })
                        .strategyConfig(builder -> {
                            builder
                                    .addInclude(tableNameList) // 设置需要生成的表名
                                    .addTablePrefix(tablePrefix) // 设置过滤表前缀
                                    .controllerBuilder()
                                    .enableRestStyle()
                                    .entityBuilder()
                                    .enableLombok();
                        })
                        // 使用 Freemarker 引擎模板，默认的是Velocity引擎模板
                        .templateEngine(new FreemarkerTemplateEngine())
                        .execute();
            }
        }
    }
}
