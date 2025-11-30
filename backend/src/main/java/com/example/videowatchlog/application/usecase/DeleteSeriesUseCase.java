package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteSeriesUseCase - シリーズ削除ユースケース
 */
@Service
public class DeleteSeriesUseCase {
    private final SeriesRepository seriesRepository;

    public DeleteSeriesUseCase(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Transactional
    public void execute(Long seriesId) {
        seriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("シリーズが見つかりません"));
        seriesRepository.delete(seriesId);
    }
}
