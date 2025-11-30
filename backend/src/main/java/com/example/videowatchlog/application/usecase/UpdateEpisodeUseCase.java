package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.WatchPageUrl;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * UpdateEpisodeUseCase - エピソード更新ユースケース
 */
@Service
public class UpdateEpisodeUseCase {
    private final EpisodeRepository episodeRepository;

    public UpdateEpisodeUseCase(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Transactional
    public void execute(Long episodeId, String episodeInfo, List<String> watchPageUrls) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("エピソードが見つかりません"));

        if (episodeInfo != null) {
            episode.updateEpisodeInfo(episodeInfo);
        }

        if (watchPageUrls != null) {
            episode.getWatchPageUrls().clear();
            for (String url : watchPageUrls) {
                episode.addWatchPageUrl(new WatchPageUrl(url));
            }
        }

        episodeRepository.save(episode);
    }
}
