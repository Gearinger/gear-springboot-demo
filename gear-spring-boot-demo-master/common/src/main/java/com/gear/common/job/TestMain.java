package com.gear.common.job;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试。自定义实现线程池
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
@Slf4j
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        PendingJobPool pendingJobPool = new PendingJobPool();
        //批量工作标识唯一
        String jobName = "xxx";
        try {
            //使用框架第一步：注册工作
            pendingJobPool.registerJob(jobName, 100, new MyTask(), 5);
        } catch (Exception e) {
            log.info("已经注册，请勿重复提交！");
        }
        //使用框架第二步：将任务依次放进去
        for (int i = 0; i < 100; i++) {
            //构造任务需要的参数
            Map<String, Object> paramMap = new HashMap<>();
            //场次
            paramMap.put("plansCode", "planCode");
            //循环将任务放进去执行
            pendingJobPool.putTask(jobName, paramMap);
        }

        while (true){
            Thread.sleep(1000);
            System.out.println(pendingJobPool.getTaskDetail(jobName));
        }
    }
}
