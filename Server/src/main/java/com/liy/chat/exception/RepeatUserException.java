package com.liy.chat.exception;

/**
 * data: 2019/6/5 23:06
 **/

public class RepeatUserException extends RuntimeException{
    public RepeatUserException() {
        super();
    }

    public RepeatUserException(String message) {
        super(message);
    }

    public RepeatUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatUserException(Throwable cause) {
        super(cause);
    }

    protected RepeatUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
