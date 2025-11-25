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
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + titleId + "）"));

        // Series を変換
        List<SeriesDetailDTO> seriesList = title.getSeries().stream()
                .map(this::mapToSeriesDetailDTO)
                .collect(Collectors.toList());

        // TitleInfoUrls を変換
        List<String> titleInfoUrls = title.getTitleInfoUrls().stream()
                .map(url -> url.getUrl())
                .collect(Collectors.toList());

        return new TitleDetailDTO(
                title.getId(),
                title.getName(),
                seriesList,
                titleInfoUrls,
                title.getCreatedAt(),
                title.getUpdatedAt()
        );
    }

    private SeriesDetailDTO mapToSeriesDetailDTO(Series series) {
        List<EpisodeDetailDTO> episodeList = series.getEpisodes().stream()
                .map(this::mapToEpisodeDetailDTO)
                .collect(Collectors.toList());

        return new SeriesDetailDTO(
                series.getId(),
                series.getTitleId(),
                series.getName(),
                episodeList,
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
