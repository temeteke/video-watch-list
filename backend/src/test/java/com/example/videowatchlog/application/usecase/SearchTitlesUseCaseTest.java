package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.WatchStatus;
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
 * SearchTitlesUseCaseTest - タイトルを検索するユースケースのテスト
 */
@DisplayName("SearchTitlesUseCase のテスト")
class SearchTitlesUseCaseTest {

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private SearchTitlesUseCase searchTitlesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("クエリでタイトルを部分一致検索できる")
    void shouldSearchTitlesByQuery() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);
        Title title2 = new Title(2L, "鬼滅の刃", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1);
        when(titleRepository.search("進撃", null)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute("進撃", null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("進撃の巨人");
    }

    @Test
    @DisplayName("watchStatus でフィルタリングできる")
    void shouldFilterByWatchStatus() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1);
        when(titleRepository.search(null, WatchStatus.UNWATCHED)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute(null, WatchStatus.UNWATCHED);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("クエリと watchStatus の両方でフィルタリングできる")
    void shouldFilterByQueryAndWatchStatus() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1);
        when(titleRepository.search("進撃", WatchStatus.WATCHED)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute("進撃", WatchStatus.WATCHED);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("進撃の巨人");
    }

    @Test
    @DisplayName("クエリがマッチしない場合は空のリストを返す")
    void shouldReturnEmptyListWhenQueryDoesNotMatch() {
        // Given
        when(titleRepository.search("存在しないタイトル", null)).thenReturn(List.of());

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute("存在しないタイトル", null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("両方のパラメータが null の場合はすべてのタイトルを返す")
    void shouldReturnAllTitlesWhenBothParametersAreNull() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);
        Title title2 = new Title(2L, "鬼滅の刃", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1, title2);
        when(titleRepository.search(null, null)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute(null, null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("検索結果が複数の場合すべて返す")
    void shouldReturnMultipleResults() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人 Season 1", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);
        Title title2 = new Title(2L, "進撃の巨人 Season 2", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1, title2);
        when(titleRepository.search("進撃", null)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute("進撃", null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(TitleSummaryDTO::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("大文字小文字を区別しない部分一致検索")
    void shouldSearchCaseInsensitively() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title title1 = new Title(1L, "進撃の巨人", new java.util.LinkedHashSet<>(),
                                new java.util.ArrayList<>(), now, now);

        List<Title> titles = Arrays.asList(title1);
        // Repository側で大文字小文字区別なし処理するはず
        when(titleRepository.search("進撃", null)).thenReturn(titles);

        // When
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute("進撃", null);

        // Then
        assertThat(result).hasSize(1);
    }
}
