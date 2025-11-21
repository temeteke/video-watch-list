package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.UpdateTitleRequestDTO;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateSeriesUseCase - シリーズ更新ユースケース
 */
@Service
public class UpdateSeriesUseCase {
    private final SeriesRepository seriesRepository;

    public UpdateSeriesUseCase(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Transactional
    public void execute(Long seriesId, String name) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("シリーズが見つかりません"));
        series.updateName(name);
        seriesRepository.save(series);
    }
}
