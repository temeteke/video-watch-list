package com.example.videowatchlog.application.dto;

import java.time.LocalDateTime;

/**
 * ViewingRecordDetailDTO - 視聴履歴詳細
 */
public class ViewingRecordDetailDTO {
    private Long id;
    private Long episodeId;
    private LocalDateTime watchedAt;
    private Integer rating;
    private String comment;
    private LocalDateTime recordedAt;

    public ViewingRecordDetailDTO(Long id, Long episodeId, LocalDateTime watchedAt,
                                  Integer rating, String comment, LocalDateTime recordedAt) {
        this.id = id;
        this.episodeId = episodeId;
        this.watchedAt = watchedAt;
        this.rating = rating;
        this.comment = comment;
        this.recordedAt = recordedAt;
    }

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
