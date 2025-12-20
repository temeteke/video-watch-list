package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.SeriesDetailDTO;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetSeriesDetailUseCase - シリーズ詳細取得ユースケース
 */
@Service
public class GetSeriesDetailUseCase {
    private final SeriesRepository seriesRepository;
    private final TitleRepository titleRepository;

    public GetSeriesDetailUseCase(SeriesRepository seriesRepository, TitleRepository titleRepository) {
        this.seriesRepository = seriesRepository;
        this.titleRepository = titleRepository;
    }

    /**
     * シリーズ詳細を取得します
     * Series ID からシリーズ情報を取得し、対応するタイトル名も含めて返します。
     *
     * @param seriesId シリーズID
     * @return シリーズ詳細
     * @throws IllegalArgumentException シリーズが見つからない場合
     */
    @Transactional(readOnly = true)
    public SeriesDetailDTO execute(Long seriesId) {
        // Series を取得
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new IllegalArgumentException("シリーズが見つかりません（ID: " + seriesId + "）"));

        // Title を取得してタイトル名を抽出
        Title title = titleRepository.findById(series.getTitleId())
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + series.getTitleId() + "）"));

        return new SeriesDetailDTO(
                series.getId(),
                series.getName(),
                series.getTitleId(),
                title.getName(),
                series.getCreatedAt(),
                series.getUpdatedAt()
        );
    }
}
