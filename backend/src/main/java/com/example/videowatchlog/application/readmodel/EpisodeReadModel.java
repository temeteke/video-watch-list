package com.example.videowatchlog.application.readmodel;

import com.example.videowatchlog.domain.model.WatchStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EpisodeReadModel - CQRS Query Model (Application Layer)
 *
 * Architecture Decision:
 * This is a Read Model (Query Model) in CQRS pattern, NOT a Domain Model.
 * - Placed in application.readmodel (NOT domain.model)
 * - No business logic (getters only)
 * - Optimized for read operations (denormalized, nested structure)
 * - Immutable DTO for presentation layer
 * - Episode + ViewingRecord の情報を保持
 * - SeriesReadModel に含まれる
 *
 * CQRS Separation:
 * - Write operations: Use domain.model.Episode (Command Model with business logic)
 * - Read operations: Use application.readmodel.EpisodeReadModel (Query Model)
 *
 * Why Application Layer?
 * - Read Model is an application concern (view optimization)
 * - Domain layer focuses on business rules and behavior
 * - Follows Onion Architecture (Application → Domain dependency is allowed)
 * - Aligns with Microsoft eShopOnContainers CQRS pattern
 *
 * Related:
 * - Write Model: domain.model.Episode (Entity)
 * - Query Service: application.readmodel.service.TitleReadService
 * - Persistence: infrastructure.persistence.readmodel.TitleReadMapper (MyBatis)
 * - Domain Value Object: domain.model.WatchStatus (referenced for view data)
 */
public class EpisodeReadModel {
    private final Long id;
    private final Long seriesId;
    private final String episodeInfo;
    private final WatchStatus watchStatus;
    private final List<String> watchPageUrls;
    private final List<ViewingRecordReadModel> viewingRecords;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public EpisodeReadModel(Long id, Long seriesId, String episodeInfo, WatchStatus watchStatus,
                           List<String> watchPageUrls, List<ViewingRecordReadModel> viewingRecords,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.seriesId = seriesId;
        this.episodeInfo = episodeInfo;
        this.watchStatus = watchStatus;
        this.watchPageUrls = watchPageUrls;
        this.viewingRecords = viewingRecords;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getEpisodeInfo() {
        return episodeInfo;
    }

    public WatchStatus getWatchStatus() {
        return watchStatus;
    }

    public List<String> getWatchPageUrls() {
        return watchPageUrls;
    }

    public List<ViewingRecordReadModel> getViewingRecords() {
        return viewingRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
