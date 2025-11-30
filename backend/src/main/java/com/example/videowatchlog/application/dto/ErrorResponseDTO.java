package com.example.videowatchlog.application.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * ErrorResponseDTO - エラーレスポンス
 */
public class ErrorResponseDTO {
    private int code;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
