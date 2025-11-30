package com.example.videowatchlog.domain.exception;

/**
 * TitleNotFoundException - タイトルが見つからない場合にスロー
 */
public class TitleNotFoundException extends RuntimeException {
    public TitleNotFoundException(String message) {
        super(message);
    }

    public TitleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
