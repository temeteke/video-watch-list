package com.example.videowatchlog.application.readmodel.service;

import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import com.example.videowatchlog.application.readmodel.repository.TitleReadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * TitleReadService - CQRS Query Service (Application Layer)
 *
 * Architecture Decision:
 * This is a Query Service that handles all read-only operations using CQRS Read Models.
 * - Depends on application.readmodel.repository.TitleReadRepository (Domain layer pattern)
 * - Transforms raw database results into immutable ReadModels (DTOs)
 * - Separates read-side queries from domain write operations (Command/Query separation)
 *
 * Onion Architecture Compliance:
 * - This service depends only on TitleReadRepository interface (Application layer)
 * - Infrastructure implementation details (MyBatis Mapper) are encapsulated in TitleReadRepositoryImpl
 * - Ensures strict adherence to dependency inversion principle
 *
 * Performance Optimization:
 * - Phase 7 Architecture: Single JOIN query replaces N+1 queries
 * - GetAllTitles: 101 queries → 1 query (99% reduction)
 * - GetTitleDetail: 2-3 queries → 1 query (50-66% reduction)
 *
 * CQRS Separation:
 * - Write operations: Use domain repositories (domain.model entities, domain.repository interfaces)
 * - Read operations: Use this TitleReadService with TitleReadRepository (infrastructure.persistence.readmodel)
 *
 * Related:
 * - Query Models: application.readmodel.TitleListReadModel, TitleDetailReadModel, etc.
 * - Repository Interface: application.readmodel.repository.TitleReadRepository
 * - Repository Implementation: infrastructure.persistence.readmodel.TitleReadRepositoryImpl (MyBatis)
 * - Command Side: domain repositories and services for write operations
 */
@Service
public class TitleReadService {
    private final TitleReadRepository titleReadRepository;

    public TitleReadService(TitleReadRepository titleReadRepository) {
        this.titleReadRepository = titleReadRepository;
    }

    /**
     * すべてのタイトル一覧を取得
     * Phase 7: 1 クエリで全件取得（元は 101 クエリ）
     *
     * @return タイトル一覧
     */
    @Transactional(readOnly = true)
    public List<TitleListReadModel> getAllTitles() {
        return titleReadRepository.findAllTitles();
    }

    /**
     * タイトル詳細を取得（Series/Episode/ViewingRecord を含む）
     * Phase 7: 1 JOIN クエリで全データ取得（元は 2-3 クエリ）
     *
     * @param titleId タイトルID
     * @return タイトル詳細
     */
    @Transactional(readOnly = true)
    public Optional<TitleDetailReadModel> getTitleDetail(Long titleId) {
        return titleReadRepository.findTitleDetailById(titleId);
    }
}
