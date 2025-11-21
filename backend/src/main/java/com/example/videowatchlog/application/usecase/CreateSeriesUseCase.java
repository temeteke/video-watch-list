package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateSeriesRequestDTO;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateSeriesUseCase - シリーズ作成ユースケース
 */
@Service
public class CreateSeriesUseCase {
    private final SeriesRepository seriesRepository;
    private final TitleRepository titleRepository;

    public CreateSeriesUseCase(SeriesRepository seriesRepository, TitleRepository titleRepository) {
        this.seriesRepository = seriesRepository;
        this.titleRepository = titleRepository;
    }

    @Transactional
    public void execute(Long titleId, CreateSeriesRequestDTO request) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません"));

        Series series = Series.create(titleId, request.getName());
        seriesRepository.save(series);
        title.getSeries().add(series);
        titleRepository.save(title);
    }
}
