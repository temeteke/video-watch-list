package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.SeriesDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.EpisodeDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.ViewingRecordDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetTitleDetailUseCase - タイトル詳細取得ユースケース
 */
@Service
public class GetTitleDetailUseCase {
    private final TitleRepository titleRepository;

    public GetTitleDetailUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * タイトル詳細を取得します
     *
     * @param titleId タイトルID
     * @return タイトル詳細
     * @throws IllegalArgumentException タイトルが見つからない場合
     */
    @Transactional(readOnly = true)
    public TitleDetailDTO execute(Long titleId) {
        // Phase 7: Read Model 実装後、TitleReadService を使用するに変更予定
        // 現在は空の Series リストを返す一時実装
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + titleId + "）"));

        // TitleInfoUrls を変換
        List<String> titleInfoUrls = title.getTitleInfoUrls().stream()
                .map(url -> url.getUrl())
                .collect(Collectors.toList());

        return new TitleDetailDTO(
                title.getId(),
                title.getName(),
                List.of(), // Phase 7: Read Model 実装後に Series データを取得
                titleInfoUrls,
                title.getCreatedAt(),
                title.getUpdatedAt()
        );
    }

    private SeriesDetailDTO mapToSeriesDetailDTO(Series series) {
        // Phase 7: 削除予定（Read Model で置き換え）
        return new SeriesDetailDTO(
                series.getId(),
                series.getTitleId(),
                series.getName(),
                List.of(), // Phase 7: Episode データを Read Model から取得
                series.getCreatedAt(),
                series.getUpdatedAt()
        );
    }

    private EpisodeDetailDTO mapToEpisodeDetailDTO(Episode episode) {
        List<String> watchPageUrls = episode.getWatchPageUrls().stream()
                .map(url -> url.getUrl())
                .collect(Collectors.toList());

        List<ViewingRecordDTO> viewingRecords = episode.getViewingRecords().stream()
                .map(record -> new ViewingRecordDTO(
                        record.getId(),
                        record.getEpisodeId(),
                        record.getWatchedAt(),
                        record.getRating(),
                        record.getComment(),
                        record.getRecordedAt()
                ))
                .collect(Collectors.toList());

        return new EpisodeDetailDTO(
                episode.getId(),
                episode.getSeriesId(),
                episode.getEpisodeInfo(),
                episode.getWatchStatus().toString(),
                watchPageUrls,
                viewingRecords,
                episode.getCreatedAt(),
                episode.getUpdatedAt()
        );
    }
}
