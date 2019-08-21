package com.github.lany192.utils;


import java.util.Map;

/**
 * 响应结果生成工具
 */
public class ResultBuilder {

    public static Result success() {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(ResultCode.SUCCESS.getMsg())
                .setData(null);
    }

    public static Result message(String message) {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(message)
                .setData(null);
    }

    public static Result success(String message, Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(message)
                .setData(data);
    }

    public static Result success(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(ResultCode.SUCCESS.getMsg())
                .setData(data);
    }

    public static Result map(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return success();
        }
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(ResultCode.SUCCESS.getMsg())
                .setData(map);
    }

    public static Result map(String message, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return success();
        }
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(message)
                .setData(map);
    }

    public static Result fail(String message) {
        return new Result()
                .setCode(ResultCode.FAIL.getCode())
                .setMsg(message)
                .setData(null);
    }

    public static Result fail(ResultCode code) {
        return new Result()
                .setCode(code.getCode())
                .setMsg(code.getMsg())
                .setData(null);
    }

    public static Result error() {
        return new Result()
                .setCode(ResultCode.SERVER_ERROR.getCode())
                .setMsg(ResultCode.SERVER_ERROR.getMsg())
                .setData(null);
    }
}
