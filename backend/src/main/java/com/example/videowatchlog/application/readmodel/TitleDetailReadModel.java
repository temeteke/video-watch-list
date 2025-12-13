package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TitleDetailReadModel - CQRS Query Model (Application Layer)
 *
 * Architecture Decision:
 * This is a Read Model (Query Model) in CQRS pattern, NOT a Domain Model.
 * - Placed in application.readmodel (NOT domain.model)
 * - No business logic (getters only)
 * - Optimized for read operations (denormalized, JOIN-friendly)
 * - Immutable DTO for presentation layer
 * - Title + Series + Episode の完全な階層構造を保持
 *
 * CQRS Separation:
 * - Write operations: Use domain.model.Title (Command Model with business logic)
 * - Read operations: Use application.readmodel.TitleDetailReadModel (Query Model)
 *
 * Why Application Layer?
 * - Read Model is an application concern (view optimization)
 * - Domain layer focuses on business rules and behavior
 * - Follows Onion Architecture (Application → Domain dependency is allowed)
 * - Aligns with Microsoft eShopOnContainers CQRS pattern
 *
 * Related:
 * - Write Model: domain.model.Title (Aggregate Root)
 * - Query Service: application.readmodel.service.TitleReadService
 * - Persistence: infrastructure.persistence.readmodel.TitleReadMapper (MyBatis)
 */
public class TitleDetailReadModel {
    private final Long id;
    private final String name;
    private final List<SeriesReadModel> series;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TitleDetailReadModel(Long id, String name, List<SeriesReadModel> series,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.series = series;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SeriesReadModel> getSeries() {
        return series;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
