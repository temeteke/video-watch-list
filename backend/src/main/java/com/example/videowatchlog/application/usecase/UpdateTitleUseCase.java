package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.UpdateTitleRequestDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.TitleInfoUrl;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpdateTitleUseCase - タイトル更新ユースケース
 */
@Service
public class UpdateTitleUseCase {
    private final TitleRepository titleRepository;

    public UpdateTitleUseCase(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Transactional
    public void execute(Long titleId, UpdateTitleRequestDTO request) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("タイトルが見つかりません"));

        title.updateName(request.getName());

        if (request.getTitleInfoUrls() != null) {
            title.getTitleInfoUrls().clear();
            for (String url : request.getTitleInfoUrls()) {
                title.addTitleInfoUrl(new TitleInfoUrl(url));
            }
        }

        titleRepository.save(title);
    }
}
