package com.example.util;

public class ZacException extends Exception {

    /**
     * 序列化?????
     */
    private static final long serialVersionUID = 1L;

    private String methodName;

    public ZacException(String message, String methodName) {
        super(message);
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
