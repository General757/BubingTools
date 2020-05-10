package com.bubing.camera.exception;

/**
 * @ClassName: BException
 * @Description: 封装异常
 * @Author: bubing
 * @Date: 2020-05-09 19:56
 */
public class BException extends Exception {
    String detailMessage;

    public BException(BExceptionType exceptionType) {
        super(exceptionType.getStringValue());
        this.detailMessage = exceptionType.getStringValue();
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
