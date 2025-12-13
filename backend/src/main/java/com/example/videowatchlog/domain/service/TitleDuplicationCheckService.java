package com.example.videowatchlog.domain.service;

import com.example.videowatchlog.domain.repository.TitleRepository;

/**
 * TitleDuplicationCheckService ドメインサービス
 *
 * タイトル名の重複をチェックします
 * （大文字小文字区別なし、完全一致チェック）
 *
 * Note: This is a pure domain service with no Spring dependencies.
 * Bean registration is handled in DomainServiceConfig.
 */
public class TitleDuplicationCheckService {
    private final TitleRepository titleRepository;

    /**
     * コンストラクタ
     * @param titleRepository TitleRepository
     */
    public TitleDuplicationCheckService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * タイトル名が重複しているかチェックします
     *
     * @param titleName チェックするタイトル名
     * @return 重複している場合 true
     */
    public boolean isDuplicate(String titleName) {
        return titleRepository.existsByName(titleName);
    }
}
