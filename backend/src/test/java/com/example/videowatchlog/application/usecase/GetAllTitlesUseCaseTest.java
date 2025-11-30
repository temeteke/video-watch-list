package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
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
 */
@DisplayName("GetAllTitlesUseCase のテスト")
class GetAllTitlesUseCaseTest {

    @Mock
    private TitleRepository titleRepository;

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
        Title title1 = Title.create("Title 1");
        title1.setId(1L);
        title1.setCreatedAt(now);
        title1.setUpdatedAt(now);

        Title title2 = Title.create("Title 2");
        title2.setId(2L);
        title2.setCreatedAt(now);
        title2.setUpdatedAt(now);

        List<Title> titles = Arrays.asList(title1, title2);
        when(titleRepository.findAll()).thenReturn(titles);

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
        when(titleRepository.findAll()).thenReturn(List.of());

        // When
        List<TitleSummaryDTO> result = getAllTitlesUseCase.execute();

        // Then
        assertThat(result).isEmpty();
    }
}
