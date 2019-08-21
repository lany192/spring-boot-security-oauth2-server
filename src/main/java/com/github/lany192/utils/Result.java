package com.github.lany192.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
