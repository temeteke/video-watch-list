package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.TitleInfoUrl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Title entity for persistence layer (MyBatis mapping).
 * This entity separates persistence concerns from the domain model.
 *
 * Phase 7 アーキテクチャ改善：toDomain() が Series パラメータを受け取らなくなりました。
 * Series は独立した集約として扱われます。
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
     * Phase 7: Series パラメータを削除し、Title のみを生成します。
     *
     * @param titleInfoUrls Title info URLs (loaded separately)
     * @return Title domain model
     */
    public Title toDomain(Set<TitleInfoUrl> titleInfoUrls) {
        return new Title(
                this.id,
                this.name,
                titleInfoUrls != null ? titleInfoUrls : new LinkedHashSet<>(),
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
