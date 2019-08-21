package com.github.lany192.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Long id;
    /**
     * 请求状态是否成功
     */
    private int status;
    private String error;
    /**
     * 详细的信息
     */
    private String message;
    private String path;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 返回对象
     */
    private T data;
    private Long total;
    private Integer ack;
    private Object additional;

    public ResponseResult() {
        this.status = GlobalConstant.SUCCESS;
    }
}
