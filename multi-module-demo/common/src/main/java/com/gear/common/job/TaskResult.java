package com.gear.common.job;


/**
 * 任务返回的结果实体类
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
public class TaskResult<R> {

    /**
     * 方法是否成功完成
     */
    private final TaskResultType resultType;
    /**
     * 方法处理后的结果数据
     */
    private final R returnValue;
    /**
     * 如果方法失败，这里可以填充原因
     */
    private final String reason;


    public TaskResult(TaskResultType resultType, R returnValue, String reason) {
        super();
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reason = reason;
    }

    public TaskResult(TaskResultType resultType, R returnValue) {
        super();
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reason = "Success";
    }


    public TaskResultType getResultType() {
        return resultType;
    }

    public R getReturnValue() {
        return returnValue;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "resultType=" + resultType +
                ", returnValue=" + returnValue +
                ", reason='" + reason + '\'' +
                '}';
    }
}
