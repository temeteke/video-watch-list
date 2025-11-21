package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.repository.EpisodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteEpisodeUseCase - エピソード削除ユースケース
 */
@Service
public class DeleteEpisodeUseCase {
    private final EpisodeRepository episodeRepository;

    public DeleteEpisodeUseCase(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Transactional
    public void execute(Long episodeId) {
        episodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("エピソードが見つかりません"));
        episodeRepository.delete(episodeId);
    }
}
