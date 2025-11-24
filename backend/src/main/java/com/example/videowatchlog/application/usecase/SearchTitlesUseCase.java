package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SearchTitlesUseCase - タイトルを検索する UseCase
 */
@Service
@Transactional(readOnly = true)
public class SearchTitlesUseCase {

    private final TitleRepository titleRepository;

    public SearchTitlesUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * タイトルを検索する
     *
     * @param query 検索クエリ (nullの場合は全タイトル)
     * @param watchStatus 視聴状態でのフィルタリング (nullの場合はフィルタリングなし)
     * @return タイトルサマリーのリスト
     */
    public List<TitleSummaryDTO> execute(String query, WatchStatus watchStatus) {
        List<Title> titles = titleRepository.search(query, watchStatus);
        return titles.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    private TitleSummaryDTO toSummaryDTO(Title title) {
        return new TitleSummaryDTO(
                title.getId(),
                title.getName(),
                title.getCreatedAt(),
                title.getUpdatedAt()
        );
    }
}
