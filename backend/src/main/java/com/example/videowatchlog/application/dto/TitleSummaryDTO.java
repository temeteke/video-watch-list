package com.example.videowatchlog.application.dto;

import java.time.ZonedDateTime;

/**
 * TitleSummaryDTO - タイトル概要情報
 *
 * タイトル一覧表示時に使用される簡潔な情報
 */
public class TitleSummaryDTO {
    private Long id;
    private String name;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public TitleSummaryDTO(Long id, String name, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
}
