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
        return titleMapper.findById(id);
    }

    @Override
    public List<Title> findAll() {
        return titleMapper.findAll();
    }

    @Override
    @Transactional
    public Title save(Title title) {
        if (title.getId() == null) {
            // 1. Titleを挿入（IDが自動設定される）
            titleMapper.insert(title);

            // 2. デフォルトSeriesを作成して挿入
            Series defaultSeries = Series.createDefault(title.getId());
            seriesMapper.insert(defaultSeries);

            // 3. デフォルトEpisodeを作成して挿入
            Episode defaultEpisode = Episode.createDefault(defaultSeries.getId());
            episodeMapper.insert(defaultEpisode);

            return title;
        } else {
            titleMapper.update(title);
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
        return titleMapper.search(query, watchStatus);
    }
}
