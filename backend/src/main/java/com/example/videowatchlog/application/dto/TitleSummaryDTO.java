package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * TitleSummaryDTO - タイトル概要情報
 *
 * タイトル一覧表示時に使用される簡潔な情報
 */
@Schema(description = "タイトル概要情報")
public class TitleSummaryDTO {
    @Schema(description = "タイトルID", example = "1")
    private Long id;

    @Schema(description = "タイトル名", example = "進撃の巨人")
    private String name;

    @Schema(description = "作成日時", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2025-01-15T10:30:00")
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
