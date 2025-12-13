package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;

/**
 * ViewingRecordReadModel - CQRS Query Model (Application Layer)
 *
 * Architecture Decision:
 * This is a Read Model (Query Model) in CQRS pattern, NOT a Domain Model.
 * - Placed in application.readmodel (NOT domain.model)
 * - No business logic (getters only)
 * - Optimized for read operations (denormalized, lightweight)
 * - Immutable DTO for presentation layer
 *
 * CQRS Separation:
 * - Write operations: Use domain.model.ViewingRecord (Command Model with business logic)
 * - Read operations: Use application.readmodel.ViewingRecordReadModel (Query Model)
 *
 * Why Application Layer?
 * - Read Model is an application concern (view optimization)
 * - Domain layer focuses on business rules and behavior
 * - Follows Onion Architecture (Application → Domain dependency is allowed)
 * - Aligns with Microsoft eShopOnContainers CQRS pattern
 *
 * Related:
 * - Write Model: domain.model.ViewingRecord (Entity)
 * - Query Service: application.readmodel.service.TitleReadService
 * - Persistence: infrastructure.persistence.readmodel.TitleReadMapper (MyBatis)
 */
public class ViewingRecordReadModel {
    private final Long id;
    private final Long episodeId;
    private final LocalDateTime watchedAt;
    private final Integer rating;
    private final String comment;
    private final LocalDateTime recordedAt;

    public ViewingRecordReadModel(Long id, Long episodeId, LocalDateTime watchedAt,
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
