package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
import com.example.videowatchlog.infrastructure.persistence.entity.ViewingRecordEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ViewingRecordRepositoryImpl - Repository 実装
 */
@Repository
public class ViewingRecordRepositoryImpl implements ViewingRecordRepository {
    private final ViewingRecordMapper viewingRecordMapper;

    public ViewingRecordRepositoryImpl(ViewingRecordMapper viewingRecordMapper) {
        this.viewingRecordMapper = viewingRecordMapper;
    }

    @Override
    public Optional<ViewingRecord> findById(Long id) {
        return viewingRecordMapper.findById(id)
                .map(ViewingRecordEntity::toDomain);
    }

    @Override
    public List<ViewingRecord> findByEpisodeId(Long episodeId) {
        return viewingRecordMapper.findByEpisodeId(episodeId)
                .stream()
                .map(ViewingRecordEntity::toDomain)
                .toList();
    }

    @Override
    public ViewingRecord save(ViewingRecord viewingRecord) {
        ViewingRecordEntity entity = ViewingRecordEntity.fromDomain(viewingRecord);
        viewingRecordMapper.insert(entity);

        // Return domain model with the auto-generated ID
        return entity.toDomain();
    }

    @Override
    public void delete(Long id) {
        viewingRecordMapper.delete(id);
    }
}
