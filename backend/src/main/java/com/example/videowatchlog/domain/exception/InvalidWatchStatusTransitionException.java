package com.example.videowatchlog.domain.exception;

/**
 * InvalidWatchStatusTransitionException - 無効な監視状態遷移の場合にスロー
 */
public class InvalidWatchStatusTransitionException extends RuntimeException {
    public InvalidWatchStatusTransitionException(String message) {
        super(message);
    }

    public InvalidWatchStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
