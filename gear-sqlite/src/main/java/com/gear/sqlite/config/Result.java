package com.gear.sqlite.config;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private Object data;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(500, msg, null);
    }

}
