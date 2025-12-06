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
        return seriesMapper.findById(id)
                .map(entity -> entity.toDomain(new ArrayList<>()));
    }

    @Override
    public List<Series> findByTitleId(Long titleId) {
        return seriesMapper.findByTitleId(titleId)
                .stream()
                .map(entity -> entity.toDomain(new ArrayList<>()))
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
        return entity.toDomain(new ArrayList<>(series.getEpisodes()));
    }

    @Override
    public void delete(Long id) {
        seriesMapper.delete(id);
    }
}
