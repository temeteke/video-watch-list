package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.repository.SeriesRepository;
import com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
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
        // Phase 7: Episode パラメータを削除
        return seriesMapper.findById(id)
                .map(entity -> entity.toDomain());
    }

    @Override
    public List<Series> findByTitleId(Long titleId) {
        // Phase 7: Episode パラメータを削除
        return seriesMapper.findByTitleId(titleId)
                .stream()
                .map(entity -> entity.toDomain())
                .toList();
    }

    @Override
    public Series save(Series series) {
        SeriesEntity entity = SeriesEntity.fromDomain(series);
        Long seriesId = series.getId();

        // Check if series exists in database
        boolean exists = seriesMapper.findById(seriesId).isPresent();

        if (!exists) {
            // Insert new series
            seriesMapper.insert(entity);
        } else {
            // Update existing series
            seriesMapper.update(entity);
        }
        // Return domain model with the auto-generated or existing ID
        // Phase 7: Episode パラメータを削除
        return entity.toDomain();
    }

    @Override
    public void delete(Long id) {
        seriesMapper.delete(id);
    }
}
