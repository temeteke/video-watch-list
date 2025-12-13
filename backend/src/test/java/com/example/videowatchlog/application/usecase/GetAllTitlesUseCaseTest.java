package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import com.example.videowatchlog.application.readmodel.service.TitleReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * GetAllTitlesUseCaseTest - すべてのタイトルを取得する UseCase のテスト
 *
 * Phase 7: TitleReadService を使用した Read Model テスト
 */
@DisplayName("GetAllTitlesUseCase のテスト")
class GetAllTitlesUseCaseTest {

    @Mock
    private TitleReadService titleReadService;

    @InjectMocks
    private GetAllTitlesUseCase getAllTitlesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("すべてのタイトルを取得できる")
    void shouldGetAllTitles() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        TitleListReadModel title1 = new TitleListReadModel(1L, "Title 1", now, now);
        TitleListReadModel title2 = new TitleListReadModel(2L, "Title 2", now, now);

        List<TitleListReadModel> titles = Arrays.asList(title1, title2);
        when(titleReadService.getAllTitles()).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = getAllTitlesUseCase.execute();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Title 1");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("Title 2");
    }

    @Test
    @DisplayName("タイトルが存在しない場合は空のリストを返す")
    void shouldReturnEmptyListWhenNoTitles() {
        // Given
        when(titleReadService.getAllTitles()).thenReturn(List.of());

        // When
        List<TitleSummaryDTO> result = getAllTitlesUseCase.execute();

        // Then
        assertThat(result).isEmpty();
    }
}
