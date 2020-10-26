package com.smartpc.chiyun.aspect;

public class NoRepeatException extends RuntimeException {
    public NoRepeatException(String msg) {
        super(msg);
    }

    public NoRepeatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
