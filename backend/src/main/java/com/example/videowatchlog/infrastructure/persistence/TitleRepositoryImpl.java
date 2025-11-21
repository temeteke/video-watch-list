package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Repository;
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

    public TitleRepositoryImpl(TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
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
    public Title save(Title title) {
        if (title.getId() == null) {
            return titleMapper.insert(title);
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
}
