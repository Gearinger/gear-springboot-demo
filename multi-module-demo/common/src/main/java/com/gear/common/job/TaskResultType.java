package com.gear.common.job;

/**
 * 任务结果类型
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
public enum TaskResultType {

    /*
      方法成功执行并返回了业务人员需要的结果
     */
    Success,
    /*
      方法成功执行但是返回的是业务人员不需要的结果
     */
    Failure,
    /*
      方法执行抛出了Exception
     */
    Exception
}
