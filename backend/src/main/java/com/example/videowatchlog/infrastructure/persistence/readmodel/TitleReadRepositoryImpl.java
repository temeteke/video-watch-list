package com.example.videowatchlog.infrastructure.persistence.readmodel;

import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.SeriesReadModel;
import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import com.example.videowatchlog.application.readmodel.repository.TitleReadRepository;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.infrastructure.persistence.readmodel.TitleReadMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.LinkedHashMap;

/**
 * TitleReadRepositoryImpl - CQRS Read Model Repository 実装
 *
 * Application層の TitleReadRepository インターフェースの Infrastructure層での実装です。
 * MyBatis の TitleReadMapper を使用して読み取り専用クエリを実行します。
 *
 * Architecture Decision:
 * - Repository パターンで Infrastructure層（MyBatis Mapper）を抽象化
 * - Application層は TitleReadRepository インターフェースのみに依存
 * - これにより、読み取り側ロジックでも Onion Architecture を遵守
 */
@Repository
public class TitleReadRepositoryImpl implements TitleReadRepository {
    private final TitleReadMapper titleReadMapper;

    public TitleReadRepositoryImpl(TitleReadMapper titleReadMapper) {
        this.titleReadMapper = titleReadMapper;
    }

    @Override
    public List<TitleListReadModel> findAllTitles() {
        return titleReadMapper.findAllTitles();
    }

    @Override
    public Optional<TitleDetailReadModel> findTitleDetailById(Long titleId) {
        List<Map<String, Object>> rawResults = titleReadMapper.findTitleDetailByIdRaw(titleId);
        if (rawResults.isEmpty()) {
            return Optional.empty();
        }

        // 生データから TitleDetailReadModel を構築
        return Optional.of(buildTitleDetail(rawResults));
    }

    /**
     * 生データから TitleDetailReadModel を構築
     * JOIN クエリの結果（複数行）をメモリで集約
     */
    private TitleDetailReadModel buildTitleDetail(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No data to build TitleDetailReadModel");
        }

        // タイトル情報は全行で同じ
        Map<String, Object> firstRow = rows.get(0);
        Long titleId = ((Number) firstRow.get("title_id")).longValue();
        String titleName = (String) firstRow.get("title_name");
        LocalDateTime titleCreatedAt = toLocalDateTime(firstRow.get("title_created_at"));
        LocalDateTime titleUpdatedAt = toLocalDateTime(firstRow.get("title_updated_at"));

        // Series と Episode をメモリで集約
        LinkedHashMap<Long, SeriesReadModel> seriesMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Object seriesIdObj = row.get("s_id");

            // Series がある場合のみ処理
            if (seriesIdObj != null) {
                Long seriesId = ((Number) seriesIdObj).longValue();

                // Series を新規作成する場合
                if (!seriesMap.containsKey(seriesId)) {
                    Long sTitleId = ((Number) row.get("s_title_id")).longValue();
                    String seriesName = (String) row.get("s_name");
                    LocalDateTime seriesCreatedAt = toLocalDateTime(row.get("s_created_at"));
                    LocalDateTime seriesUpdatedAt = toLocalDateTime(row.get("s_updated_at"));

                    SeriesReadModel series = new SeriesReadModel(
                        seriesId,
                        sTitleId,
                        seriesName,
                        new ArrayList<>(),
                        seriesCreatedAt,
                        seriesUpdatedAt
                    );
                    seriesMap.put(seriesId, series);
                }

                // Episode を処理
                Object episodeIdObj = row.get("e_id");
                if (episodeIdObj != null) {
                    Long episodeId = ((Number) episodeIdObj).longValue();
                    Long episodeSeriesId = ((Number) row.get("e_series_id")).longValue();
                    String episodeInfo = (String) row.get("e_episode_info");
                    WatchStatus watchStatus = WatchStatus.valueOf((String) row.get("e_watch_status"));
                    LocalDateTime episodeCreatedAt = toLocalDateTime(row.get("e_created_at"));
                    LocalDateTime episodeUpdatedAt = toLocalDateTime(row.get("e_updated_at"));

                    EpisodeReadModel episode = new EpisodeReadModel(
                        episodeId,
                        episodeSeriesId,
                        episodeInfo,
                        watchStatus,
                        new ArrayList<>(), // watchPageUrls は遅延ロード
                        new ArrayList<>(), // viewingRecords は遅延ロード
                        episodeCreatedAt,
                        episodeUpdatedAt
                    );

                    // 対応する Series に Episode を追加
                    SeriesReadModel series = seriesMap.get(episodeSeriesId);
                    if (series != null) {
                        series.getEpisodes().add(episode);
                    }
                }
            }
        }

        return new TitleDetailReadModel(
            titleId,
            titleName,
            new ArrayList<>(seriesMap.values()),
            titleCreatedAt,
            titleUpdatedAt
        );
    }

    /**
     * sql.Timestamp から java.time.LocalDateTime への型変換ヘルパー
     * MyBatis がデータベースから取得した日時は Timestamp の場合がある
     */
    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to LocalDateTime");
    }
}
