package com.gear.common.job;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;


/**
 * 自定义任务
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
@Slf4j
public class MyTask implements ITaskProcesser<Map<String, Object>, Object> {
    @Override
    public TaskResult<Object> taskExecute(Map<String, Object> data) throws InterruptedException {
        // 执行任务内容
        Random random = new Random();
        Thread.sleep(random.nextInt(2000));
        System.out.println("任务执行完成");

        return new TaskResult<Object>(TaskResultType.Success, null);
    }
}
