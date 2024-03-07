package com.gear.common.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * 返回结果定义
 *
 * @author guoyd
 * @version 1.0.0
 * @date 2021/01/25
 */
public class ResultBody<T> {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private T result;

    public ResultBody() {
    }

    /**
     * 结果
     *
     * @param errorInfo 错误信息
     * @return {@link  }
     */
    public ResultBody(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 成功
     *
     * @return {@link ResultBody }
     */
    public static <T> ResultBody<T> success() {
        return success(null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return {@link ResultBody }
     */
    public static <T> ResultBody<T> success(T data) {
        ResultBody<T> rb = new ResultBody<T>();
        rb.setCode(CommonEnum.SUCCESS.getResultCode());
        rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(BaseErrorInfoInterface errorInfo) {
        ResultBody<T> rb = new ResultBody<T>();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(String status, String message) {
        ResultBody<T> rb = new ResultBody<T>();
        rb.setCode(status);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(String message) {
        ResultBody<T> rb = new ResultBody<T>();
        rb.setCode("501");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @SneakyThrows
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

}
