package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Series entity for persistence layer (MyBatis mapping).
 * This class is responsible for database persistence only.
 * Business logic resides in domain.model.Series.
 */
public class SeriesEntity {
    private Long id;
    private Long titleId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Package-private default constructor for MyBatis.
     */
    SeriesEntity() {
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

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Convert to domain model (Series).
     * Related entities (episodes) are loaded separately by the repository.
     *
     * @param episodes Episodes (loaded separately)
     * @return Series domain model
     */
    public Series toDomain(List<Episode> episodes) {
        return new Series(
            this.id,
            this.titleId,
            this.name,
            episodes != null ? episodes : new ArrayList<>(),
            this.createdAt,
            this.updatedAt
        );
    }

    /**
     * Create from domain model (Series).
     * Note: episodes are not included (managed separately).
     *
     * @param series Domain model
     * @return SeriesEntity for persistence
     */
    public static SeriesEntity fromDomain(Series series) {
        SeriesEntity entity = new SeriesEntity();
        entity.setId(series.getId());
        entity.setTitleId(series.getTitleId());
        entity.setName(series.getName());
        entity.setCreatedAt(series.getCreatedAt());
        entity.setUpdatedAt(series.getUpdatedAt());
        return entity;
    }
}
