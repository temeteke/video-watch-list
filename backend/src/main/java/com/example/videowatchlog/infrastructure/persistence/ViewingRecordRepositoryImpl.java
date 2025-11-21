package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
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
        return viewingRecordMapper.findById(id);
    }

    @Override
    public List<ViewingRecord> findByEpisodeId(Long episodeId) {
        return viewingRecordMapper.findByEpisodeId(episodeId);
    }

    @Override
    public ViewingRecord save(ViewingRecord viewingRecord) {
        return viewingRecordMapper.insert(viewingRecord);
    }

    @Override
    public void delete(Long id) {
        viewingRecordMapper.delete(id);
    }
}
