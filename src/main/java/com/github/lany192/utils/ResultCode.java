package com.github.lany192.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举，参考HTTP状态码的语义
 *
 * @author Lany
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "请求成功"),
    FAIL(400, "请求失败"),
    NOT_LOGIN(401, "未登录或者登录过期，请重新登录"),
    ILLEGAL_DEVICE(402, "请求失败,请求设备异常"),
    UNAUTHORIZED(403, "权限不足，请联系管理员!"),
    NOT_FOUND(404, "接口不存在"),
    SIGN_FAIL(405, "签名校验失败"),
    PARAM_ERROR(406, "参数错误"),
    SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String msg;
}
