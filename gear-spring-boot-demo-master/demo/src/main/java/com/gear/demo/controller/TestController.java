package com.gear.demo.controller;

import com.gear.common.result.ResultBody;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author GuoYingdong
 * @date 2022/02/18
 */
@Slf4j
@RestController
@Api(tags = "test")
public class TestController {

    @PostMapping("/test")
    public ResultBody<String> test() {
        log.info("test");
        return ResultBody.success("test");
    }
}
