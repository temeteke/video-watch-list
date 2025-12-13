package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import com.example.videowatchlog.application.readmodel.service.TitleReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetAllTitlesUseCase - すべてのタイトルを取得する UseCase
 *
 * Phase 7 CQRS 改善:
 * - 101 クエリ → 1 クエリに削減（99% 削減）
 * - TitleReadService を使用して読み取り最適化
 */
@Service
@Transactional(readOnly = true)
public class GetAllTitlesUseCase {

    private final TitleReadService titleReadService;

    public GetAllTitlesUseCase(TitleReadService titleReadService) {
        this.titleReadService = titleReadService;
    }

    /**
     * すべてのタイトルを取得する
     * Phase 7: 1 クエリで全件取得（元は 101 クエリ）
     *
     * @return タイトルサマリーのリスト
     */
    public List<TitleSummaryDTO> execute() {
        // Phase 7: TitleReadService から Read Model を取得
        List<TitleListReadModel> titles = titleReadService.getAllTitles();
        return titles.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    private TitleSummaryDTO toSummaryDTO(TitleListReadModel model) {
        return new TitleSummaryDTO(
                model.getId(),
                model.getName(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }
}
