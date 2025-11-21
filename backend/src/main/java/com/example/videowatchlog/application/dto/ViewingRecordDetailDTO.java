package com.example.videowatchlog.application.dto;

import java.time.ZonedDateTime;

/**
 * ViewingRecordDetailDTO - 視聴履歴詳細
 */
public class ViewingRecordDetailDTO {
    private Long id;
    private Long episodeId;
    private ZonedDateTime watchedAt;
    private Integer rating;
    private String comment;
    private ZonedDateTime recordedAt;

    public ViewingRecordDetailDTO(Long id, Long episodeId, ZonedDateTime watchedAt,
                                  Integer rating, String comment, ZonedDateTime recordedAt) {
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

    public ZonedDateTime getWatchedAt() {
        return watchedAt;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public ZonedDateTime getRecordedAt() {
        return recordedAt;
    }
}
