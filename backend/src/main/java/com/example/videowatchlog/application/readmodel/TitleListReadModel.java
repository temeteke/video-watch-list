package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;

/**
 * TitleListReadModel - CQRS Query Model (Application Layer)
 *
 * Architecture Decision:
 * This is a Read Model (Query Model) in CQRS pattern, NOT a Domain Model.
 * - Placed in application.readmodel (NOT domain.model)
 * - No business logic (getters only)
 * - Optimized for read operations (denormalized, lightweight)
 * - Immutable DTO for presentation layer
 * - Title 単体の情報を保持
 * - Series/Episode 情報は含まない（一覧表示では不要）
 *
 * CQRS Separation:
 * - Write operations: Use domain.model.Title (Command Model with business logic)
 * - Read operations: Use application.readmodel.TitleListReadModel (Query Model)
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
public class TitleListReadModel {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TitleListReadModel(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
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
}
