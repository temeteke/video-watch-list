package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetTitleDetailUseCase - タイトル詳細取得ユースケース
 */
@Service
public class GetTitleDetailUseCase {
    private final TitleRepository titleRepository;

    public GetTitleDetailUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * タイトル詳細を取得します
     *
     * @param titleId タイトルID
     * @return タイトル詳細
     * @throws IllegalArgumentException タイトルが見つからない場合
     */
    @Transactional(readOnly = true)
    public TitleSummaryDTO execute(Long titleId) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません（ID: " + titleId + "）"));

        return new TitleSummaryDTO(
                title.getId(),
                title.getName(),
                title.getCreatedAt(),
                title.getUpdatedAt()
        );
    }
}
