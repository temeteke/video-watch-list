package com.example.videowatchlog.domain.service;

import com.example.videowatchlog.domain.repository.TitleRepository;
import org.springframework.stereotype.Service;

/**
 * TitleDuplicationCheckService ドメインサービス
 *
 * タイトル名の重複をチェックします
 * （大文字小文字区別なし、完全一致チェック）
 */
@Service
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
