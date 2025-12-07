package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateSeriesRequestDTO;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import com.example.videowatchlog.domain.repository.TitleRepository;
import com.example.videowatchlog.domain.service.SeriesIdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateSeriesUseCase - シリーズ作成ユースケース
 */
@Service
public class CreateSeriesUseCase {
    private final SeriesIdService seriesIdService;
    private final SeriesRepository seriesRepository;
    private final TitleRepository titleRepository;

    public CreateSeriesUseCase(SeriesIdService seriesIdService, SeriesRepository seriesRepository, TitleRepository titleRepository) {
        this.seriesIdService = seriesIdService;
        this.seriesRepository = seriesRepository;
        this.titleRepository = titleRepository;
    }

    @Transactional
    public void execute(Long titleId, CreateSeriesRequestDTO request) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません"));

        Long id = seriesIdService.generateId();
        Series series = Series.create(id, titleId, request.getName());
        seriesRepository.save(series);
        // Phase 7: Title の Series フィールドがなくなったため、Series を単独で保存
        // Title はもはや Series リストを保持しません
    }
}
