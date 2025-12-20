package com.example.videowatchlog.infrastructure.persistence.readmodel;

import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.ViewingRecordReadModel;
import com.example.videowatchlog.application.readmodel.repository.EpisodeReadRepository;
import com.example.videowatchlog.domain.model.WatchStatus;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EpisodeReadRepositoryImpl - CQRS Read Model Repository 実装
 *
 * Application層の EpisodeReadRepository インターフェースの Infrastructure層での実装です。
 * MyBatis の EpisodeReadMapper を使用して読み取り専用クエリを実行します。
 */
@Repository
public class EpisodeReadRepositoryImpl implements EpisodeReadRepository {
    private final EpisodeReadMapper episodeReadMapper;

    public EpisodeReadRepositoryImpl(EpisodeReadMapper episodeReadMapper) {
        this.episodeReadMapper = episodeReadMapper;
    }

    @Override
    public Optional<EpisodeReadModel> findEpisodeDetailByIdAndSeriesId(Long seriesId, Long episodeId) {
        List<Map<String, Object>> rawResults = episodeReadMapper.findEpisodeDetailByIdAndSeriesIdRaw(seriesId, episodeId);
        if (rawResults.isEmpty()) {
            return Optional.empty();
        }

        // 生データから EpisodeReadModel を構築
        return Optional.of(buildEpisodeDetail(rawResults));
    }

    /**
     * 生データから EpisodeReadModel を構築
     * JOIN クエリの結果（複数行）をメモリで集約
     * Multiple LEFT JOINs により、watch_page_urls と viewing_records の各行の組み合わせが返される
     */
    private EpisodeReadModel buildEpisodeDetail(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No data to build EpisodeReadModel");
        }

        // エピソード情報は全行で同じ
        Map<String, Object> firstRow = rows.get(0);
        Long episodeId = ((Number) firstRow.get("e_id")).longValue();
        Long seriesId = ((Number) firstRow.get("e_series_id")).longValue();
        String episodeInfo = (String) firstRow.get("e_episode_info");
        WatchStatus watchStatus = WatchStatus.valueOf((String) firstRow.get("e_watch_status"));
        LocalDateTime episodeCreatedAt = toLocalDateTime(firstRow.get("e_created_at"));
        LocalDateTime episodeUpdatedAt = toLocalDateTime(firstRow.get("e_updated_at"));

        // watchPageUrls と viewingRecords を集約
        List<String> watchPageUrls = new ArrayList<>();
        Map<Long, ViewingRecordReadModel> viewingRecordsMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            // watch_page_urls を処理
            String url = (String) row.get("wpu_url");
            if (url != null && !watchPageUrls.contains(url)) {
                watchPageUrls.add(url);
            }

            // ViewingRecord を処理
            Object recordIdObj = row.get("vr_id");
            if (recordIdObj != null) {
                Long recordId = ((Number) recordIdObj).longValue();

                // まだ処理していない ViewingRecord の場合
                if (!viewingRecordsMap.containsKey(recordId)) {
                    Long recordEpisodeId = ((Number) row.get("vr_episode_id")).longValue();
                    LocalDateTime watchedAt = toLocalDateTime(row.get("vr_watched_at"));
                    Integer rating = ((Number) row.get("vr_rating")).intValue();
                    String comment = (String) row.get("vr_comment");
                    LocalDateTime recordedAt = toLocalDateTime(row.get("vr_recorded_at"));

                    ViewingRecordReadModel record = new ViewingRecordReadModel(
                        recordId,
                        recordEpisodeId,
                        watchedAt,
                        rating,
                        comment,
                        recordedAt
                    );
                    viewingRecordsMap.put(recordId, record);
                }
            }
        }

        return new EpisodeReadModel(
            episodeId,
            seriesId,
            episodeInfo,
            watchStatus,
            watchPageUrls,
            new ArrayList<>(viewingRecordsMap.values()),
            episodeCreatedAt,
            episodeUpdatedAt
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
