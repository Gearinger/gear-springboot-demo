package com.gear.common.job;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * 任务完成后，在一定时间内共查询，之后会释放节约内存（从缓存中清除）
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
@Slf4j
public class CheckJobProcesser {

    /**
     * 存放任务的队列
     */
    private static DelayQueue<ItemVo<String>> queue = new DelayQueue<>();

    /**
     * 单例化
     *
     * @author GuoYingdong
     * @date 2022/05/24
     */
    private static class ProcesserHolder{
        public static CheckJobProcesser processer = new CheckJobProcesser();
    }

    public static CheckJobProcesser getInstance() {
        return ProcesserHolder.processer;
    }
    /*单例化*/

    //处理队列中到期的任务
    private static class FetchJob implements Runnable{

        private static DelayQueue<ItemVo<String>> queue = CheckJobProcesser.queue;

        private static Map<String, JobInfo<?>> jobInfoMap = PendingJobPool.getMap();

        @Override
        public void run() {
            try {
                ItemVo<String> itemVo = queue.take();
                String jobName = itemVo.getData();
                jobInfoMap.remove(jobName);
                //移除应用缓存中的任务
//                batchJobNameCache.remove(jobName);
                log.info("Job：["+ jobName+"] is out of date,remove from JobList! ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 任务完成后，放入队列，到期后，从缓存中清除
     *
     * @param jobName    作业名
     * @param expireTime 到期时间
     */
    public void putJob(String jobName, long expireTime){
        Thread thread = new Thread(new FetchJob());
        // thread.setName("outOfDate"+jobName);
        thread.setDaemon(true);
        thread.start();
        log.info("开启[ " + jobName + " ]工作过期检查守护线程...........");
        //包装工作，放入延时队列
        ItemVo<String> itemVo = new ItemVo<>(expireTime, jobName);
        queue.offer(itemVo);
        log.info("任务[" + jobName + "]已被放入过期检查缓存，过期时长：" + expireTime + "s");
    }
}