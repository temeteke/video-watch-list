package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SeriesReadModel - CQRS Query Model (Application Layer)
 *
 * Architecture Decision:
 * This is a Read Model (Query Model) in CQRS pattern, NOT a Domain Model.
 * - Placed in application.readmodel (NOT domain.model)
 * - No business logic (getters only)
 * - Optimized for read operations (denormalized, nested structure)
 * - Immutable DTO for presentation layer
 * - Series + Episode の情報を保持
 * - TitleDetailReadModel に含まれる
 *
 * CQRS Separation:
 * - Write operations: Use domain.model.Series (Command Model with business logic)
 * - Read operations: Use application.readmodel.SeriesReadModel (Query Model)
 *
 * Why Application Layer?
 * - Read Model is an application concern (view optimization)
 * - Domain layer focuses on business rules and behavior
 * - Follows Onion Architecture (Application → Domain dependency is allowed)
 * - Aligns with Microsoft eShopOnContainers CQRS pattern
 *
 * Related:
 * - Write Model: domain.model.Series (Entity)
 * - Query Service: application.readmodel.service.TitleReadService
 * - Persistence: infrastructure.persistence.readmodel.TitleReadMapper (MyBatis)
 */
public class SeriesReadModel {
    private final Long id;
    private final Long titleId;
    private final String name;
    private final List<EpisodeReadModel> episodes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SeriesReadModel(Long id, Long titleId, String name, List<EpisodeReadModel> episodes,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titleId = titleId;
        this.name = name;
        this.episodes = episodes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTitleId() {
        return titleId;
    }

    public String getName() {
        return name;
    }

    public List<EpisodeReadModel> getEpisodes() {
        return episodes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
