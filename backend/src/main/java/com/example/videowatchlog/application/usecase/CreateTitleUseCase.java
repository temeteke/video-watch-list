package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateTitleRequestDTO;
import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.TitleInfoUrl;
import com.example.videowatchlog.domain.repository.TitleRepository;
import com.example.videowatchlog.domain.service.EntityIdentityService;
import com.example.videowatchlog.domain.service.TitleDuplicationCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateTitleUseCase - タイトル作成ユースケース
 *
 * ユーザーストーリー1: タイトルと複数シリーズを登録する
 */
@Service
public class CreateTitleUseCase {
    private final EntityIdentityService identityService;
    private final TitleRepository titleRepository;
    private final TitleDuplicationCheckService duplicationCheckService;

    public CreateTitleUseCase(
            EntityIdentityService identityService,
            TitleRepository titleRepository,
            TitleDuplicationCheckService duplicationCheckService) {
        this.identityService = identityService;
        this.titleRepository = titleRepository;
        this.duplicationCheckService = duplicationCheckService;
    }

    /**
     * タイトルを作成します
     *
     * @param request タイトル作成リクエスト
     * @return 作成されたタイトル（概要）
     * @throws IllegalArgumentException タイトル名が重複している場合
     */
    @Transactional
    public TitleSummaryDTO execute(CreateTitleRequestDTO request) {
        // 重複チェック
        if (duplicationCheckService.isDuplicate(request.getName())) {
            throw new IllegalArgumentException("タイトル「" + request.getName() + "」はすでに存在します");
        }

        // ID採番（ドメインサービス）
        Long id = identityService.generateId();

        // タイトルを作成（デフォルトシリーズ・エピソードが自動生成される）
        Title title = Title.create(id, request.getName());

        // タイトル情報URLを追加
        if (request.getTitleInfoUrls() != null && !request.getTitleInfoUrls().isEmpty()) {
            for (String url : request.getTitleInfoUrls()) {
                title.addTitleInfoUrl(new TitleInfoUrl(url));
            }
        }

        // 保存
        Title saved = titleRepository.save(title);

        // DTOに変換
        return new TitleSummaryDTO(
                saved.getId(),
                saved.getName(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
