package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchPageUrl;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.infrastructure.persistence.entity.EpisodeEntity;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * EpisodeRepositoryImpl - Repository 実装
 */
@Repository
public class EpisodeRepositoryImpl implements EpisodeRepository {
    private final EpisodeMapper episodeMapper;
    private final ViewingRecordMapper viewingRecordMapper;

    public EpisodeRepositoryImpl(EpisodeMapper episodeMapper, ViewingRecordMapper viewingRecordMapper) {
        this.episodeMapper = episodeMapper;
        this.viewingRecordMapper = viewingRecordMapper;
    }

    @Override
    public Optional<Episode> findById(Long id) {
        return episodeMapper.findById(id)
                .map(entity -> {
                    // Load WatchPageUrls
                    List<WatchPageUrl> watchPageUrls = episodeMapper.selectWatchPageUrlsByEpisodeId(id);

                    // Load ViewingRecords
                    List<ViewingRecord> viewingRecords = viewingRecordMapper.findByEpisodeId(id)
                            .stream()
                            .map(viewingRecordEntity -> viewingRecordEntity.toDomain())
                            .toList();

                    return entity.toDomain(watchPageUrls, viewingRecords);
                });
    }

    @Override
    public List<Episode> findBySeriesId(Long seriesId) {
        return episodeMapper.findBySeriesId(seriesId)
                .stream()
                .map(entity -> {
                    // Load WatchPageUrls
                    List<WatchPageUrl> watchPageUrls = episodeMapper.selectWatchPageUrlsByEpisodeId(entity.getId());

                    // Load ViewingRecords
                    List<ViewingRecord> viewingRecords = viewingRecordMapper.findByEpisodeId(entity.getId())
                            .stream()
                            .map(viewingRecordEntity -> viewingRecordEntity.toDomain())
                            .toList();

                    return entity.toDomain(watchPageUrls, viewingRecords);
                })
                .toList();
    }

    @Override
    public Episode save(Episode episode) {
        EpisodeEntity entity = EpisodeEntity.fromDomain(episode);
        Long episodeId;

        if (episode.getId() == null) {
            // Insert new episode
            episodeMapper.insert(entity);
            episodeId = entity.getId();
        } else {
            // Update existing episode
            episodeMapper.update(entity);
            episodeId = episode.getId();
        }

        // Save WatchPageUrls: delete all and re-insert
        episodeMapper.deleteWatchPageUrlsByEpisodeId(episodeId);
        for (WatchPageUrl url : episode.getWatchPageUrls()) {
            episodeMapper.insertWatchPageUrl(episodeId, url.getUrl());
        }

        // Save new ViewingRecords (only insert new ones with null ID)
        for (ViewingRecord record : episode.getViewingRecords()) {
            if (record.getId() == null) {
                viewingRecordMapper.insert(com.example.videowatchlog.infrastructure.persistence.entity.ViewingRecordEntity.fromDomain(record));
            }
        }

        // Reload from database to get auto-generated IDs for ViewingRecords
        return findById(episodeId).orElseThrow(() ->
                new IllegalStateException("Failed to reload episode after save: " + episodeId));
    }

    @Override
    public void delete(Long id) {
        episodeMapper.delete(id);
    }
}
