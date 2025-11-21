package com.example.videowatchlog.application.dto;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * ErrorResponseDTO - エラーレスポンス
 */
public class ErrorResponseDTO {
    private int code;
    private String message;
    private Map<String, String> details;
    private ZonedDateTime timestamp;

    public ErrorResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }

    public ErrorResponseDTO(int code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = ZonedDateTime.now();
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

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
