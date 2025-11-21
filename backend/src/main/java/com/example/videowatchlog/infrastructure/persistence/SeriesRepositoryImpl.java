package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * SeriesRepositoryImpl - Repository 実装
 */
@Repository
public class SeriesRepositoryImpl implements SeriesRepository {
    private final SeriesMapper seriesMapper;

    public SeriesRepositoryImpl(SeriesMapper seriesMapper) {
        this.seriesMapper = seriesMapper;
    }

    @Override
    public Optional<Series> findById(Long id) {
        return seriesMapper.findById(id);
    }

    @Override
    public List<Series> findByTitleId(Long titleId) {
        return seriesMapper.findByTitleId(titleId);
    }

    @Override
    public Series save(Series series) {
        if (series.getId() == null) {
            return seriesMapper.insert(series);
        } else {
            seriesMapper.update(series);
            return series;
        }
    }

    @Override
    public void delete(Long id) {
        seriesMapper.delete(id);
    }
}
