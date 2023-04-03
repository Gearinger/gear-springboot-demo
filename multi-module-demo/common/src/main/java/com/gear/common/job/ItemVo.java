package com.gear.common.job;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * 存放的延时队列的元素
 *
 * @author GuoYingdong
 * @date 2022/05/24
 */
public class ItemVo<T> implements Delayed {

    /**
     * 到期时间,单位毫秒
     */
    private long activeTime;

    /**
     * 业务数据，泛型
     */
    private T data;

    /**
     * 传入过期时长，单位秒（内部转换为毫秒）
     *
     * @param expirationTime 过期时间
     * @param data           数据
     */
    public ItemVo(long expirationTime, T data) {
        this.activeTime = expirationTime*1000 + System.currentTimeMillis();
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }


    /**
     * 返回到激活日期的剩余时间，时间单位由单位参数指定
     *
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime - System.currentTimeMillis(), unit);
    }

    /**
     * Delayed接口继承了Comparable接口，按剩余时间排序
     *
     */
    @Override
    public int compareTo(Delayed o) {
        long d = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);

        if(d == 0){
            return 0;
        }else{
            if(d < 0 ){
                return -1;
            }else{
                return 1;
            }
        }
    }
}
