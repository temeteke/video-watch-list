package com.example.videowatchlog.presentation.dto;

import java.time.LocalDateTime;

/**
 * ErrorResponseDTO - エラーレスポンス
 */
public class ErrorResponseDTO {
    private String message;
    private String code;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponseDTO(String message, String code, String path) {
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ErrorResponseDTO(String message, String code) {
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
