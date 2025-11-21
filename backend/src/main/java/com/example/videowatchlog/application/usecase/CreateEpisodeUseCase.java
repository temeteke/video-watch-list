package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateEpisodeRequestDTO;
import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.WatchPageUrl;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateEpisodeUseCase - エピソード作成ユースケース
 */
@Service
public class CreateEpisodeUseCase {
    private final EpisodeRepository episodeRepository;
    private final SeriesRepository seriesRepository;

    public CreateEpisodeUseCase(EpisodeRepository episodeRepository, SeriesRepository seriesRepository) {
        this.episodeRepository = episodeRepository;
        this.seriesRepository = seriesRepository;
    }

    @Transactional
    public void execute(Long seriesId, CreateEpisodeRequestDTO request) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("シリーズが見つかりません"));

        Episode episode = Episode.create(seriesId, request.getEpisodeInfo());

        if (request.getWatchPageUrls() != null && !request.getWatchPageUrls().isEmpty()) {
            for (String url : request.getWatchPageUrls()) {
                episode.addWatchPageUrl(new WatchPageUrl(url));
            }
        }

        episodeRepository.save(episode);
        series.getEpisodes().add(episode);
        seriesRepository.save(series);
    }
}
