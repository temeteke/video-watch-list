package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * TitleRepositoryImpl - Repository 実装
 *
 * TitleRepository インターフェースを MyBatis を使用して実装します
 */
@Repository
public class TitleRepositoryImpl implements TitleRepository {
    private final TitleMapper titleMapper;
    private final SeriesMapper seriesMapper;
    private final EpisodeMapper episodeMapper;

    public TitleRepositoryImpl(TitleMapper titleMapper, SeriesMapper seriesMapper, EpisodeMapper episodeMapper) {
        this.titleMapper = titleMapper;
        this.seriesMapper = seriesMapper;
        this.episodeMapper = episodeMapper;
    }

    @Override
    public Optional<Title> findById(Long id) {
        Optional<com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity> entityOpt = titleMapper.findById(id);
        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        // Load TitleInfoUrls
        List<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrlsList = titleMapper.selectTitleInfoUrlsByTitleId(id);
        java.util.Set<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrls = new java.util.LinkedHashSet<>(titleInfoUrlsList);

        // Load Series (convert SeriesEntity -> Series)
        List<com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity> seriesEntities = seriesMapper.findByTitleId(id);
        List<Series> series = seriesEntities.stream()
                .map(se -> se.toDomain(new java.util.ArrayList<>()))
                .toList();

        return Optional.of(entityOpt.get().toDomain(titleInfoUrls, series));
    }

    @Override
    public List<Title> findAll() {
        List<com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity> entities = titleMapper.findAll();
        return entities.stream()
                .map(entity -> {
                    // Load TitleInfoUrls
                    List<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrlsList = titleMapper.selectTitleInfoUrlsByTitleId(entity.getId());
                    java.util.Set<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrls = new java.util.LinkedHashSet<>(titleInfoUrlsList);

                    // Load Series (convert SeriesEntity -> Series)
                    List<com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity> seriesEntities = seriesMapper.findByTitleId(entity.getId());
                    List<Series> series = seriesEntities.stream()
                            .map(se -> se.toDomain(new java.util.ArrayList<>()))
                            .toList();

                    return entity.toDomain(titleInfoUrls, series);
                })
                .toList();
    }

    @Override
    @Transactional
    public Title save(Title title) {
        if (title.getId() == null) {
            // 1. Title -> TitleEntity に変換して挿入（IDが自動設定される）
            com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity entity =
                    com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity.fromDomain(title);
            titleMapper.insert(entity);

            // 2. デフォルトSeriesを作成して挿入
            Series defaultSeries = Series.createDefault(entity.getId());
            com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity seriesEntity =
                    com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity.fromDomain(defaultSeries);
            seriesMapper.insert(seriesEntity);

            // 3. デフォルトEpisodeを作成して挿入（SeriesEntityのIDを使用）
            Episode defaultEpisode = Episode.createDefault(seriesEntity.getId());
            com.example.videowatchlog.infrastructure.persistence.entity.EpisodeEntity episodeEntity =
                    com.example.videowatchlog.infrastructure.persistence.entity.EpisodeEntity.fromDomain(defaultEpisode);
            episodeMapper.insert(episodeEntity);

            // 4. IDが設定された新しいTitleインスタンスを返す（不変なため新規作成）
            return new Title(
                    entity.getId(),
                    title.getName(),
                    title.getTitleInfoUrls(),
                    title.getSeries(),
                    title.getCreatedAt(),
                    title.getUpdatedAt()
            );
        } else {
            // Title -> TitleEntity に変換して更新
            com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity entity =
                    com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity.fromDomain(title);
            titleMapper.update(entity);
            return title;
        }
    }

    @Override
    public void delete(Long id) {
        titleMapper.delete(id);
    }

    @Override
    public boolean existsByName(String name) {
        return titleMapper.existsByName(name);
    }

    @Override
    public List<Title> search(String query, WatchStatus watchStatus) {
        List<com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity> entities = titleMapper.search(query, watchStatus);
        return entities.stream()
                .map(entity -> {
                    // Load TitleInfoUrls
                    List<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrlsList = titleMapper.selectTitleInfoUrlsByTitleId(entity.getId());
                    java.util.Set<com.example.videowatchlog.domain.model.TitleInfoUrl> titleInfoUrls = new java.util.LinkedHashSet<>(titleInfoUrlsList);

                    // Load Series (convert SeriesEntity -> Series)
                    List<com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity> seriesEntities = seriesMapper.findByTitleId(entity.getId());
                    List<Series> series = seriesEntities.stream()
                            .map(se -> se.toDomain(new java.util.ArrayList<>()))
                            .toList();

                    return entity.toDomain(titleInfoUrls, series);
                })
                .toList();
    }
}
