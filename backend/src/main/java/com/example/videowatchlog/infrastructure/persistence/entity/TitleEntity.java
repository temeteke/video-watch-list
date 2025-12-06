package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.TitleInfoUrl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Title entity for persistence layer (MyBatis mapping).
 * This entity separates persistence concerns from the domain model.
 */
public class TitleEntity {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor for MyBatis.
     */
    TitleEntity() {
    }

    /**
     * Convert to domain model.
     *
     * @param titleInfoUrls Title info URLs (loaded separately)
     * @param series Series list (loaded separately)
     * @return Title domain model
     */
    public Title toDomain(Set<TitleInfoUrl> titleInfoUrls, List<Series> series) {
        return new Title(
                this.id,
                this.name,
                titleInfoUrls != null ? titleInfoUrls : new LinkedHashSet<>(),
                series != null ? series : new ArrayList<>(),
                this.createdAt,
                this.updatedAt
        );
    }

    /**
     * Create from domain model.
     *
     * @param title Title domain model
     * @return TitleEntity persistence model
     */
    public static TitleEntity fromDomain(Title title) {
        TitleEntity entity = new TitleEntity();
        entity.setId(title.getId());
        entity.setName(title.getName());
        entity.setCreatedAt(title.getCreatedAt());
        entity.setUpdatedAt(title.getUpdatedAt());
        return entity;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
