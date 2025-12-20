package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * SeriesDetailDTO - シリーズ詳細情報（スタンドアロン版）
 *
 * 個別のシリーズ詳細取得エンドポイント用 DTO
 */
@Schema(description = "シリーズ詳細情報")
public class SeriesDetailDTO {
    @Schema(description = "シリーズID", example = "1")
    private Long id;

    @Schema(description = "シリーズ名", example = "Season 1")
    private String name;

    @Schema(description = "タイトルID", example = "1")
    private Long titleId;

    @Schema(description = "タイトル名", example = "進撃の巨人")
    private String titleName;

    @Schema(description = "作成日時", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2025-01-15T10:30:00")
    private LocalDateTime updatedAt;

    public SeriesDetailDTO(Long id, String name, Long titleId, String titleName,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.titleId = titleId;
        this.titleName = titleName;
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

    public Long getTitleId() {
        return titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
