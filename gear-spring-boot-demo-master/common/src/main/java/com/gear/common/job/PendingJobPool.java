package com.gear.common.job;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * 框架的主体类，也是调用者主要使用的类
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
@Service
public class PendingJobPool {
    /**
     * 运行的线程数，机器的CPU数相同
     */
    private static final int THREAD_COUNTS = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池队列，用以存放待处理的任务
     */
    private static final BlockingQueue<Runnable> TASK_QUEUE = new ArrayBlockingQueue<>(5000);

    /**
     * 线程池，固定大小，有界队列
     */
    private static final ExecutorService TASK_EXECUTOR = new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS,
            60, TimeUnit.SECONDS, TASK_QUEUE);


    /**
     * 提交给线程池的工作信息的存放容器
     */
    private static final ConcurrentHashMap<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();

    public static Map<String, JobInfo<?>> getMap() {
        return jobInfoMap;
    }

    /**
     * 对工作中任务进行包装，提交给线程池使用，并处理任务结果，写入缓存供查询
     *
     * @author GuoYingdong
     * @date 2022/05/24
     */
    private static class PendingTask<T, R> implements Runnable {

        private final JobInfo<R> jobInfo;
        private final T processData;

        public PendingTask(JobInfo<R> jobInfo, T processData) {
            super();
            this.jobInfo = jobInfo;
            this.processData = processData;
        }

        @Override
        public void run() {
            //取得任务的处理器
            ITaskProcesser<T, R> taskProcesser = (ITaskProcesser<T, R>) jobInfo.getTaskProcessor();
            TaskResult<R> result = null;
            try {
                //执行任务，获得处理结果
                result = taskProcesser.taskExecute(processData);
                //检查处理器的返回结果，避免调用者处理不当
                if (result == null) {
                    result = new TaskResult<>(TaskResultType.Exception, null, "result is NULL");
                } else if (result.getResultType() == null) {
                    if (result.getReason() == null) {
                        result = new TaskResult<>(TaskResultType.Exception, null, "result is NULL");
                    } else {
                        result = new TaskResult<>(TaskResultType.Exception, null, "result is NULL，reason：" + result.getReason());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = new TaskResult<>(TaskResultType.Exception, null, e.getMessage());
            } finally {
                //将任务的处理结果写入缓存
                if (result == null) {
                    result = new TaskResult<>(TaskResultType.Exception, null, "result is NULL");
                    System.out.println("不可能事件！ result is NULL！！！");
                }
                jobInfo.addTaskResult(result);
            }
        }
    }

    /**
     * 提交工作中的任务
     *
     * @param jobName 作业名
     * @param t       t
     */
    public <T, R> void putTask(String jobName, T t) {
        JobInfo<R> jobInfo = getJob(jobName);
        PendingTask<T, R> task = new PendingTask<>(jobInfo, t);
        TASK_EXECUTOR.execute(task);
    }

    /**
     * 根据工作名检索工作
     *
     * @param jobName 作业名
     * @return {@link JobInfo}<{@link R}>
     */
    private <R> JobInfo<R> getJob(String jobName) {
        JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
        if (null == jobInfo) {
//            throw new RuntimeException(jobName + "已结束! ");
            return null;
        }
        return jobInfo;
    }

    /**
     * 调用者注册工作（工作标识，任务处理器等）
     *
     * @param jobName       作业名
     * @param taskLength    任务长度
     * @param taskProcesser 任务制造者
     * @param expireTime    到期时间
     */
    public <R> void registerJob(String jobName, int taskLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        JobInfo<R> jobInfo = new JobInfo<>(jobName, taskLength, taskProcesser, expireTime);
        if (jobInfoMap.putIfAbsent(jobName, jobInfo) != null) {
            throw new RuntimeException(jobName + "已经注册! ");
        }
    }

    /**
     * 获得每个任务的处理详情
     *
     * @param jobName 作业名
     * @return {@link List}<{@link TaskResult}<{@link R}>>
     */
    public <R> List<TaskResult<R>> getTaskDetail(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskDetail();
    }

    /**
     * 获得工作的整体处理进度
     *
     * @param jobName 作业名
     * @return {@link String}
     */
    public <R> String getTaskProgess(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTotalProcess();
    }

    /**
     * 获取工作中子任务个数
     *
     * @param jobName 作业名
     * @return int
     */
    public <R> int getTaskLength(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskLength();
    }

    /**
     * 获取工作中子任务已处理的个数
     *
     * @param jobName 作业名
     * @return int
     */
    public <R> int gettaskProcesserCount(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskProcesserCount().get();
    }

}
