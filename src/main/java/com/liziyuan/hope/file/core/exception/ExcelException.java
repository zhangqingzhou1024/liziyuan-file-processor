package com.liziyuan.hope.file.core.exception;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 23:20
 */
public class ExcelException extends RuntimeException {

    public ExcelException(String message, Throwable t) {
        super(message, t);
    }

    public ExcelException(String message) {
        super(message);
    }
}
