package com.example.videowatchlog.application.dto;

import java.time.LocalDateTime;

/**
 * TitleSummaryDTO - タイトル概要情報
 *
 * タイトル一覧表示時に使用される簡潔な情報
 */
public class TitleSummaryDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TitleSummaryDTO(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
