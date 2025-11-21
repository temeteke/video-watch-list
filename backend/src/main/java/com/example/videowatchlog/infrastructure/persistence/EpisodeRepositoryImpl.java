package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * EpisodeRepositoryImpl - Repository 実装
 */
@Repository
public class EpisodeRepositoryImpl implements EpisodeRepository {
    private final EpisodeMapper episodeMapper;

    public EpisodeRepositoryImpl(EpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    @Override
    public Optional<Episode> findById(Long id) {
        return episodeMapper.findById(id);
    }

    @Override
    public List<Episode> findBySeriesId(Long seriesId) {
        return episodeMapper.findBySeriesId(seriesId);
    }

    @Override
    public Episode save(Episode episode) {
        if (episode.getId() == null) {
            return episodeMapper.insert(episode);
        } else {
            episodeMapper.update(episode);
            return episode;
        }
    }

    @Override
    public void delete(Long id) {
        episodeMapper.delete(id);
    }
}
