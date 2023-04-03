package com.gear.common.job;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提交给框架执行的工作实体类（本批次需要处理的同一性质的任务的集合）
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
public class JobInfo<R> {
    /**
     * 工作的名称(唯一标识)
     */
    private final String jobName;

    /**
     * 工作中任务的个数
     */
    private final int taskLength;

    /**
     * 工作中任务的处理器
     */
    private final ITaskProcesser<?, ?> taskProcessor;

    /**
     * 成功处理的任务数
     */
    private final AtomicInteger successCount;

    /**
     * 已处理的任务数
     */
    private final AtomicInteger  taskProcesserCount;

    /**
     * 存放每个任务的处理结果，工查询用（拿结果从头拿，放结果从尾部放）
     */
    private final LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;

    /**
     * 工作完成后，保留工作结果信息供查询的时间
     */
    private final long expireTime;

    /**
     * 检查过期工作的处理器
     */
    private static CheckJobProcesser checkJobProcesser = CheckJobProcesser.getInstance();


    public JobInfo(String jobName, int taskLength, ITaskProcesser<?, ?> taskProcessor, long expireTime) {
        this.jobName = jobName;
        this.taskLength = taskLength;
        this.taskProcessor = taskProcessor;
        successCount = new AtomicInteger(0);
        taskProcesserCount = new AtomicInteger(0);
        taskDetailQueue = new LinkedBlockingDeque<TaskResult<R>>(taskLength);
        this.expireTime = expireTime;
    }

    public AtomicInteger getSuccessCount() {
        return successCount;
    }

    public AtomicInteger getTaskProcesserCount() {
        return taskProcesserCount;
    }

    public int getTaskLength() {
        return taskLength;
    }

    public ITaskProcesser<?, ?> getTaskProcessor() {
        return taskProcessor;
    }

    /**
     * 提供工作中失败的次数
     * @return int
     */
    public int getFailCount(){
        return taskProcesserCount.get() - successCount.get();
    }


    /**
     * 提供工作的整体进度信息
     * @return {@link String}
     */
    public String getTotalProcess(){
        return "Success [" + successCount.get() + "]/Current[" + taskProcesserCount.get()
                + "] Total [" + taskLength + "]";
    }

    /**
     * 取任务处理结果：提供工作中每个任务的处理结果
     *
     * @return {@link List}<{@link TaskResult}<{@link R}>>
     */
    public List<TaskResult<R>> getTaskDetail(){
        List<TaskResult<R>> taskDetailList = new LinkedList<>();
        TaskResult<R> taskResult;
        //，每次从结果队列拿结果，直到拿不到
        while ((taskResult = taskDetailQueue.pollFirst()) != null){
            taskDetailList.add(taskResult);
        }
        return taskDetailList;
    }

    /**
     * 放任务处理结果：每个任务处理完后，记录任务处理结果（保持最终一致性即可）
     *
     * @param result 结果
     */
    public void addTaskResult(TaskResult<R> result){
        if(TaskResultType.Success.equals(result.getResultType())){
            successCount.getAndIncrement();
        }
        taskDetailQueue.addLast(result);
        taskProcesserCount.getAndIncrement();

        if(taskProcesserCount.get() == taskLength){
            //推进过期检查处理器
            checkJobProcesser.putJob(jobName, expireTime);
        }
    }
}
