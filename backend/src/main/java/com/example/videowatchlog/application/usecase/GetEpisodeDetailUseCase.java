package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.EpisodeDetailDTO;
import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.ViewingRecordReadModel;
import com.example.videowatchlog.application.readmodel.service.EpisodeReadService;
import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetEpisodeDetailUseCase - エピソード詳細取得ユースケース
 */
@Service
public class GetEpisodeDetailUseCase {
    private final EpisodeReadService episodeReadService;
    private final EpisodeRepository episodeRepository;
    private final SeriesRepository seriesRepository;
    private final TitleRepository titleRepository;

    public GetEpisodeDetailUseCase(EpisodeReadService episodeReadService,
                                  EpisodeRepository episodeRepository,
                                  SeriesRepository seriesRepository,
                                  TitleRepository titleRepository) {
        this.episodeReadService = episodeReadService;
        this.episodeRepository = episodeRepository;
        this.seriesRepository = seriesRepository;
        this.titleRepository = titleRepository;
    }

    /**
     * エピソード詳細を取得します
     * Episode ID からエピソード情報を取得し、対応するシリーズ名、タイトル名も含めて返します。
     *
     * @param episodeId エピソードID
     * @return エピソード詳細
     * @throws IllegalArgumentException エピソードが見つからない場合
     */
    @Transactional(readOnly = true)
    public EpisodeDetailDTO execute(Long episodeId) {
        // Episode を取得（Series ID を確認するため）
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("エピソードが見つかりません（ID: " + episodeId + "）"));

        // Series を取得
        Series series = seriesRepository.findById(episode.getSeriesId())
                .orElseThrow(() -> new IllegalArgumentException("シリーズが見つかりません（ID: " + episode.getSeriesId() + "）"));

        // Title を取得
        Title title = titleRepository.findById(series.getTitleId())
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + series.getTitleId() + "）"));

        // EpisodeReadModel を取得（ViewingRecords を含む）
        EpisodeReadModel episodeReadModel = episodeReadService.getEpisodeDetail(series.getId(), episodeId)
                .orElseThrow(() -> new IllegalArgumentException("エピソード詳細が見つかりません（ID: " + episodeId + "）"));

        // ViewingRecords を DTO に変換
        List<EpisodeDetailDTO.ViewingRecordDTO> viewingRecords = episodeReadModel.getViewingRecords().stream()
                .map(record -> new EpisodeDetailDTO.ViewingRecordDTO(
                        record.getId(),
                        record.getEpisodeId(),
                        record.getWatchedAt(),
                        record.getRating(),
                        record.getComment(),
                        record.getRecordedAt()
                ))
                .collect(Collectors.toList());

        return new EpisodeDetailDTO(
                episodeReadModel.getId(),
                episodeReadModel.getEpisodeInfo(),
                episodeReadModel.getWatchStatus().toString(),
                episodeReadModel.getWatchPageUrls(),
                series.getId(),
                series.getName(),
                title.getId(),
                title.getName(),
                viewingRecords,
                episodeReadModel.getCreatedAt(),
                episodeReadModel.getUpdatedAt()
        );
    }
}
