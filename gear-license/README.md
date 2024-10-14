## 开始使用

### 客户端开启授权

在其他项目中，添加以下依赖即可开启授权验证：

~~~xml
<dependency>
    <groupId>com.gear</groupId>
    <artifactId>license-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
~~~

### 从服务端获取授权信息

1. 请求 /licenseServer/createLicense

> 空字符串和*均表示全部放行

~~~
{
  "expireDate": "1731310081000",
  "mac": "",
  "ip": "",
  "cpuSerial": "",
  "mainBoard": ""
}
~~~

ActiveCode：

~~~
iJXghYSjOPU7KHBSgvRm7LdPCBJvl8Sff5y+jbg8sV5PoWEj2OeoFwBg3dVCUsVRuwB+f2mhj+rF8SCUxQ5WgLZaAP6q7a+cq8Gs/ttUL2Cw7rUdAZQYDRpezoB6pmM+V1TTrpYUqkAD7Nu+y90nYPa2VfBv1zs3WdOhDc1UZXG/Bf5YiPfScex0UHV2yMvebdyHosUS+FhqnzxH02kXm6Gzam/kDO5GeMk27qfSjKnL5FGTlLUpWNa2IUFTra9Q/dEbWbDcGinqcmXm39fUuo950k5agOe85aHe/Vl0/lQiIPb9TTEEfRkkabb3sXFTdZQ8moCZWUqAakccuzOY+g==
~~~

### 客户端配置授权信息

配置文件中配置授权信息如下：

~~~yml
sys:
  license:
    client:
      active-code: iJXghYSjOPU7KHBSgvRm7LdPCBJvl8Sff5y+jbg8sV5PoWEj2OeoFwBg3dVCUsVRuwB+f2mhj+rF8SCUxQ5WgLZaAP6q7a+cq8Gs/ttUL2Cw7rUdAZQYDRpezoB6pmM+V1TTrpYUqkAD7Nu+y90nYPa2VfBv1zs3WdOhDc1UZXG/Bf5YiPfScex0UHV2yMvebdyHosUS+FhqnzxH02kXm6Gzam/kDO5GeMk27qfSjKnL5FGTlLUpWNa2IUFTra9Q/dEbWbDcGinqcmXm39fUuo950k5agOe85aHe/Vl0/lQiIPb9TTEEfRkkabb3sXFTdZQ8moCZWUqAakccuzOY+g==
~~~

## RestAPI 接口说明

| 端     | 接口                            | 描述                 |
| ------ | ------------------------------- | -------------------- |
| 服务端 | /licenseServer/createLicense    | 创建许可证           |
|        | /licenseServer/listLicense      | 获取已创建的许可列表 |
|        | /licenseServer/verifyActiveCode | 验证激活代码         |
|        | /licenseServer/generateKeyPair  | 生成密钥对           |
| 客户端 | /licenseClient/licenseInfo      | 获取许可证信息       |

