package com.gear.common.job;

/**
 * 任务管理
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
public interface ITaskProcesser<T, R> {
    TaskResult<R> taskExecute(T data) throws InterruptedException;
}
