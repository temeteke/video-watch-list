package com.example.videowatchlog.application.readmodel.service;

import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.repository.EpisodeReadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * EpisodeReadService - CQRS Query Service (Application Layer)
 *
 * Architecture Decision:
 * This is a Query Service that handles all read-only operations for episodes using CQRS Read Models.
 * - Depends on application.readmodel.repository.EpisodeReadRepository
 * - Transforms raw database results into immutable ReadModels (DTOs)
 * - Separates read-side queries from domain write operations (Command/Query separation)
 */
@Service
public class EpisodeReadService {
    private final EpisodeReadRepository episodeReadRepository;

    public EpisodeReadService(EpisodeReadRepository episodeReadRepository) {
        this.episodeReadRepository = episodeReadRepository;
    }

    /**
     * エピソード詳細を取得（ViewingRecord を含む）
     *
     * @param seriesId シリーズID
     * @param episodeId エピソードID
     * @return エピソード詳細
     */
    @Transactional(readOnly = true)
    public Optional<EpisodeReadModel> getEpisodeDetail(Long seriesId, Long episodeId) {
        return episodeReadRepository.findEpisodeDetailByIdAndSeriesId(seriesId, episodeId);
    }
}
