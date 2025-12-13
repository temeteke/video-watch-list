package com.example.videowatchlog.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Series entity.
 * Represents a season within a Title (e.g., Season 1, Season 2).
 *
 * Phase 7 アーキテクチャ改善：Series は独立した集約になり、Episode への参照は ID のみです。
 * Episode の詳細データは、Read Model を通じてのみ取得します。
 *
 * Key business rules:
 * - Series name can be empty (for default series)
 * - Series name must not exceed 100 characters
 * - Deleting a series cascades to all Episodes and ViewingRecords
 */
public class Series {
    private final Long id;
    private final Long titleId;
    private String name;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor for Series entity.
     *
     * @param id Unique identifier (null for new entities)
     * @param titleId Parent title ID (required)
     * @param name Series name (can be empty, max 100 characters)
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     * @throws IllegalArgumentException if validation fails
     */
    public Series(Long id, Long titleId, String name,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titleId = Objects.requireNonNull(titleId, "titleId must not be null");
        validateName(name);
        this.name = name != null ? name : "";
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }


    /**
     * Factory method to create a new Series.
     *
     * @param id Series ID (EntityIdentityServiceで生成)
     * @param titleId Parent title ID
     * @param name Series name
     * @return New Series
     */
    public static Series create(Long id, Long titleId, String name) {
        LocalDateTime now = LocalDateTime.now();
        return new Series(id, titleId, name, now, now);
    }

    /**
     * Factory method to create a new default Series (with empty name) with initial default Episode.
     *
     * @param id Series ID (EntityIdentityServiceで生成)
     * @param titleId Parent title ID
     * @return New Series with empty name and one default Episode
     */
    public static Series createDefault(Long id, Long titleId) {
        return create(id, titleId, "");
    }

    /**
     * Validates series name according to business rules.
     *
     * @param name Series name to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateName(String name) {
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("Series name must not exceed 100 characters");
        }
    }

    /**
     * Updates the series name.
     *
     * @param name New series name
     */
    public void updateName(String name) {
        validateName(name);
        this.name = name != null ? name : "";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the updatedAt timestamp.
     */
    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getTitleId() {
        return titleId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(id, series.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", titleId=" + titleId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
