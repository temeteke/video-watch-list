package com.example.videowatchlog.domain.exception;

/**
 * TitleDuplicateException - タイトルが既に存在する場合にスロー
 */
public class TitleDuplicateException extends RuntimeException {
    public TitleDuplicateException(String message) {
        super(message);
    }

    public TitleDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
