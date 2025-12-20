package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EpisodeDetailDTO - エピソード詳細情報（スタンドアロン版）
 *
 * 個別のエピソード詳細取得エンドポイント用 DTO
 */
@Schema(description = "エピソード詳細情報")
public class EpisodeDetailDTO {
    @Schema(description = "エピソードID", example = "1")
    private Long id;

    @Schema(description = "エピソード情報", example = "第1話 巨人襲撃")
    private String episodeInfo;

    @Schema(description = "視聴ステータス", example = "UNWATCHED")
    private String watchStatus;

    @Schema(description = "視聴ページURL一覧", example = "[\"https://example.com/ep/1\"]")
    private List<String> watchPageUrls;

    @Schema(description = "シリーズID", example = "1")
    private Long seriesId;

    @Schema(description = "シリーズ名", example = "Season 1")
    private String seriesName;

    @Schema(description = "タイトルID", example = "1")
    private Long titleId;

    @Schema(description = "タイトル名", example = "進撃の巨人")
    private String titleName;

    @Schema(description = "視聴記録一覧")
    private List<ViewingRecordDTO> viewingRecords;

    @Schema(description = "作成日時", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2025-01-15T10:30:00")
    private LocalDateTime updatedAt;

    public EpisodeDetailDTO(Long id, String episodeInfo, String watchStatus, List<String> watchPageUrls,
                           Long seriesId, String seriesName, Long titleId, String titleName,
                           List<ViewingRecordDTO> viewingRecords, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.episodeInfo = episodeInfo;
        this.watchStatus = watchStatus;
        this.watchPageUrls = watchPageUrls;
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.titleId = titleId;
        this.titleName = titleName;
        this.viewingRecords = viewingRecords;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEpisodeInfo() {
        return episodeInfo;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public List<String> getWatchPageUrls() {
        return watchPageUrls;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public Long getTitleId() {
        return titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public List<ViewingRecordDTO> getViewingRecords() {
        return viewingRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * ViewingRecordDTO - 視聴記録情報
     */
    @Schema(description = "視聴記録情報")
    public static class ViewingRecordDTO {
        @Schema(description = "視聴記録ID", example = "1")
        private Long id;

        @Schema(description = "エピソードID", example = "1")
        private Long episodeId;

        @Schema(description = "視聴日時", example = "2025-01-15T20:00:00")
        private LocalDateTime watchedAt;

        @Schema(description = "評価（1～5）", example = "5", minimum = "1", maximum = "5")
        private Integer rating;

        @Schema(description = "視聴感想・コメント", example = "素晴らしいエピソード！")
        private String comment;

        @Schema(description = "記録日時", example = "2025-01-15T20:30:00")
        private LocalDateTime recordedAt;

        public ViewingRecordDTO(Long id, Long episodeId, LocalDateTime watchedAt,
                              Integer rating, String comment, LocalDateTime recordedAt) {
            this.id = id;
            this.episodeId = episodeId;
            this.watchedAt = watchedAt;
            this.rating = rating;
            this.comment = comment;
            this.recordedAt = recordedAt;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public Long getEpisodeId() {
            return episodeId;
        }

        public LocalDateTime getWatchedAt() {
            return watchedAt;
        }

        public Integer getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public LocalDateTime getRecordedAt() {
            return recordedAt;
        }
    }
}
