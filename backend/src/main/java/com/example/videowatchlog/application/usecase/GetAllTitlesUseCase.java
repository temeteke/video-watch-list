package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetAllTitlesUseCase - すべてのタイトルを取得する UseCase
 */
@Service
@Transactional(readOnly = true)
public class GetAllTitlesUseCase {

    private final TitleRepository titleRepository;

    public GetAllTitlesUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * すべてのタイトルを取得する
     *
     * @return タイトルサマリーのリスト
     */
    public List<TitleSummaryDTO> execute() {
        List<Title> titles = titleRepository.findAll();
        return titles.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    private TitleSummaryDTO toSummaryDTO(Title title) {
        TitleSummaryDTO dto = new TitleSummaryDTO();
        dto.setId(title.getId());
        dto.setName(title.getName());
        dto.setCreatedAt(title.getCreatedAt());
        dto.setUpdatedAt(title.getUpdatedAt());
        return dto;
    }
}
