package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.SeriesDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.EpisodeDetailDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO.ViewingRecordDTO;
import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.SeriesReadModel;
import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.ViewingRecordReadModel;
import com.example.videowatchlog.application.readmodel.service.TitleReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetTitleDetailUseCase - タイトル詳細取得ユースケース
 */
@Service
public class GetTitleDetailUseCase {
    private final TitleReadService titleReadService;

    public GetTitleDetailUseCase(TitleReadService titleReadService) {
        this.titleReadService = titleReadService;
    }

    /**
     * タイトル詳細を取得します
     * Phase 7: TitleReadService を使用して 1 JOIN クエリで全データ取得
     * 元は 2-3 クエリ → 1 クエリに削減
     *
     * @param titleId タイトルID
     * @return タイトル詳細
     * @throws IllegalArgumentException タイトルが見つからない場合
     */
    @Transactional(readOnly = true)
    public TitleDetailDTO execute(Long titleId) {
        // Phase 7: TitleReadService から Read Model を取得（1 JOIN クエリで全データ）
        TitleDetailReadModel titleDetail = titleReadService.getTitleDetail(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + titleId + "）"));

        // Series データを変換
        List<SeriesDetailDTO> seriesDetailList = titleDetail.getSeries().stream()
                .map(this::mapToSeriesDetailDTO)
                .collect(Collectors.toList());

        return new TitleDetailDTO(
                titleDetail.getId(),
                titleDetail.getName(),
                seriesDetailList,
                List.of(), // TitleInfoUrls は Write Model から取得が必要な場合のみ
                titleDetail.getCreatedAt(),
                titleDetail.getUpdatedAt()
        );
    }

    private SeriesDetailDTO mapToSeriesDetailDTO(SeriesReadModel series) {
        // Phase 7: Episode データを Read Model から取得
        List<EpisodeDetailDTO> episodeDetailList = series.getEpisodes().stream()
                .map(this::mapToEpisodeDetailDTO)
                .collect(Collectors.toList());

        return new SeriesDetailDTO(
                series.getId(),
                series.getTitleId(),
                series.getName(),
                episodeDetailList,
                series.getCreatedAt(),
                series.getUpdatedAt()
        );
    }

    private EpisodeDetailDTO mapToEpisodeDetailDTO(EpisodeReadModel episode) {
        // WatchPageUrls を Read Model から取得
        List<String> watchPageUrls = episode.getWatchPageUrls();

        // ViewingRecords を Read Model から取得
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
