package com.example.videowatchlog.domain.model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * ViewingRecord entity (immutable).
 * Represents a single viewing record of an episode.
 *
 * Key business rules:
 * - Rating must be 1-5
 * - Comment is optional (can be null or up to 2000 characters)
 * - Watched date must not be in the future
 * - ViewingRecord is immutable once created (no setters)
 */
public class ViewingRecord {
    private final Long id;
    private final Long episodeId;
    private final ZonedDateTime watchedAt;
    private final Integer rating;
    private final String comment;
    private final ZonedDateTime recordedAt;

    /**
     * Constructor for ViewingRecord entity.
     *
     * @param id Unique identifier (null for new entities)
     * @param episodeId Parent episode ID (required)
     * @param watchedAt Date and time when the episode was watched (required, must not be future)
     * @param rating User rating (required, 1-5)
     * @param comment User comment (optional, max 2000 characters)
     * @param recordedAt Date and time when this record was created (required)
     * @throws IllegalArgumentException if validation fails
     */
    public ViewingRecord(Long id, Long episodeId, ZonedDateTime watchedAt, Integer rating,
                        String comment, ZonedDateTime recordedAt) {
        this.id = id;
        this.episodeId = Objects.requireNonNull(episodeId, "episodeId must not be null");
        this.watchedAt = Objects.requireNonNull(watchedAt, "watchedAt must not be null");
        validateRating(rating);
        this.rating = rating;
        validateComment(comment);
        this.comment = comment;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt must not be null");

        validateWatchedAtNotFuture(watchedAt);
    }

    /**
     * Validates rating according to business rules.
     *
     * @param rating Rating to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRating(Integer rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Rating must not be null");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    /**
     * Validates comment according to business rules.
     *
     * @param comment Comment to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateComment(String comment) {
        if (comment != null && comment.length() > 2000) {
            throw new IllegalArgumentException("Comment must not exceed 2000 characters");
        }
    }

    /**
     * Validates that watchedAt is not in the future.
     *
     * @param watchedAt Date to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateWatchedAtNotFuture(ZonedDateTime watchedAt) {
        if (watchedAt.isAfter(ZonedDateTime.now())) {
            throw new IllegalArgumentException("WatchedAt must not be in the future");
        }
    }

    // Getters only (immutable entity)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewingRecord that = (ViewingRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ViewingRecord{" +
                "id=" + id +
                ", episodeId=" + episodeId +
                ", watchedAt=" + watchedAt +
                ", rating=" + rating +
                ", comment='" + (comment != null && comment.length() > 50 ? comment.substring(0, 50) + "..." : comment) + '\'' +
                ", recordedAt=" + recordedAt +
                '}';
    }
}
