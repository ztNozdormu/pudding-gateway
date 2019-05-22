package com.mohism.pudding.gateway.core.exception;


import com.mohism.pudding.kernel.model.exception.AbstractBaseExceptionEnum;

/**
 * 签名异常
 *
 * @author fengshuonan
 * @date 2018-01-05 14:48
 */
public enum AuthExceptionEnum implements AbstractBaseExceptionEnum {

    TOKEN_EMPTY(8001, "登录令牌为空，请检查是否已经登录"),
    TOKEN_ERROR(8002, "token无效"),
    NO_PERMISSION(8003, "没有访问该资源的权限");

    AuthExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
