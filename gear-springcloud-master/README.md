## 一、注册中心、配置中心--Nacos

> 注意：
>
> 1. 配置 namespace 需要对应 nacos 的 dataid。如果 dataid 为空，则试用命名空间的显示名称；

### 1、配置中心

#### （1）依赖项

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>${spring.cloud-version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<!-- nacos-config 依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>2.2.4.RELEASE</version>
</dependency>
~~~

#### （2）配置内容

bootstrap.yml

~~~yaml
server:
  port: 10000

spring:
  profiles:
    active: dev
~~~

bootstrap-dev.yml

~~~yaml
spring:
  application:
    name: Vegetable
  cloud:
    nacos:
      config:
        # 默认对应的 data-id 为 {application.name} + "-" + {profiles.active} + "." + {file-extension}
        # 使用这个也可以 data-id  {application.name} + "." + {file-extension}
        server-addr: 111.229.255.253:9003
        file-extension: yaml
        namespace: public
        group: dev

        # 拓展的配置项，优先级更低
        extension-configs:
          - data-id: shareconfig.yaml
            group: dev2
~~~



#### （3）注解及代码示例

~~~java
@RefreshScope	// 自动刷新配置
@RestController
@RequestMapping("/vegetables")
public class TestController {

    @Value("${test.config}")
    private String testConfig;

    @RequestMapping("config")
    public String config(){
        return testConfig;
    }

    @RequestMapping("potato")
    public String potato(){
        return "potato";
    }
}
~~~

> 注意：
>
> 1. 配置文件名称为`bootstrap.yml`、`bootstrap-dev.yml`等；

### 2、注册中心

#### （1）依赖项

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>${spring.cloud-version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2.2.4.RELEASE</version>
</dependency>
~~~

#### （2）配置

bootstrap.yml

~~~yaml
server:
  port: 10000
spring:
  profiles:
    active: dev
~~~

bootstrap-dev.yml

~~~yaml
spring:
  application:
    name: Vegetable
  cloud:
    nacos:
      # 注册中心
      discovery:
        server-addr: 111.229.255.253:9003
        namespace: public
        group: dev
~~~

#### （3）代码

~~~java
@EnableDiscoveryClient	// 开启服务客户端的发现
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
~~~



## 二、微服务调用--Feign

### 1、依赖

~~~xml
~~~

