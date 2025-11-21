package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteTitleUseCase - タイトル削除ユースケース
 */
@Service
public class DeleteTitleUseCase {
    private final TitleRepository titleRepository;

    public DeleteTitleUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Transactional
    public void execute(Long titleId) {
        titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません"));
        titleRepository.delete(titleId);
    }
}
