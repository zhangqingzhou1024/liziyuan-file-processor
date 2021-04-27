package com.liziyuan.hope.file.core.exception;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 23:22
 */
public class ExcelWriteException extends ExcelException {

    public ExcelWriteException(String message, Throwable t) {
        super(message, t);
    }

    public ExcelWriteException(String message) {
        super(message);
    }
}
