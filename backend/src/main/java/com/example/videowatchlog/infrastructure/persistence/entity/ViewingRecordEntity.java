package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.ViewingRecord;

import java.time.LocalDateTime;

/**
 * ViewingRecord entity for persistence layer (MyBatis mapping).
 * This class is responsible for database persistence only.
 * Business logic resides in domain.model.ViewingRecord.
 */
public class ViewingRecordEntity {
    private Long id;
    private Long episodeId;
    private LocalDateTime watchedAt;
    private Integer rating;
    private String comment;
    private LocalDateTime recordedAt;

    /**
     * Default constructor for MyBatis and testing.
     */
    public ViewingRecordEntity() {
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

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    /**
     * Convert to domain model (ViewingRecord).
     *
     * @return ViewingRecord domain model
     */
    public ViewingRecord toDomain() {
        return new ViewingRecord(
            this.id,
            this.episodeId,
            this.watchedAt,
            this.rating,
            this.comment,
            this.recordedAt
        );
    }

    /**
     * Create from domain model (ViewingRecord).
     *
     * @param viewingRecord Domain model
     * @return ViewingRecordEntity for persistence
     */
    public static ViewingRecordEntity fromDomain(ViewingRecord viewingRecord) {
        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(viewingRecord.getId());
        entity.setEpisodeId(viewingRecord.getEpisodeId());
        entity.setWatchedAt(viewingRecord.getWatchedAt());
        entity.setRating(viewingRecord.getRating());
        entity.setComment(viewingRecord.getComment());
        entity.setRecordedAt(viewingRecord.getRecordedAt());
        return entity;
    }
}
