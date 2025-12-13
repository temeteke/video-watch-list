package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
import com.example.videowatchlog.infrastructure.persistence.entity.ViewingRecordEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * ViewingRecordRepositoryImpl - Repository 実装
 *
 * ViewingRecordRepository インターフェースの Infrastructure層での実装です。
 * MyBatis の ViewingRecordMapper を使用して永続化を行います。
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
    public void delete(Long id) {
        viewingRecordMapper.delete(id);
    }
}
