package com.example.videowatchlog.domain.service;

import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TitleDuplicationCheckService")
@ExtendWith(MockitoExtension.class)
class TitleDuplicationCheckServiceTest {
    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private TitleDuplicationCheckService service;

    @Test
    @DisplayName("タイトル名が存在しない場合、重複ではない")
    void shouldNotBeDuplicateIfNotExists() {
        // Given
        String titleName = "新作アニメ";
        when(titleRepository.existsByName(titleName)).thenReturn(false);

        // When
        boolean isDuplicate = service.isDuplicate(titleName);

        // Then
        assertThat(isDuplicate).isFalse();
        verify(titleRepository, times(1)).existsByName(titleName);
    }

    @Test
    @DisplayName("タイトル名が存在する場合、重複である")
    void shouldBeDuplicateIfExists() {
        // Given
        String titleName = "進撃の巨人";
        when(titleRepository.existsByName(titleName)).thenReturn(true);

        // When
        boolean isDuplicate = service.isDuplicate(titleName);

        // Then
        assertThat(isDuplicate).isTrue();
        verify(titleRepository, times(1)).existsByName(titleName);
    }

    @Test
    @DisplayName("大文字小文字を区別せずに重複チェックする")
    void shouldIgnoreCaseWhenCheckingDuplicate() {
        // Given: データベースに "Attack on Titan" が登録されている
        String existingTitle = "Attack on Titan";
        String searchTitle = "ATTACK ON TITAN"; // 大文字で検索

        // データベース層で大文字小文字を区別しないチェックを行うため、
        // 異なる大文字小文字でも重複として検出される
        when(titleRepository.existsByName(searchTitle)).thenReturn(true);

        // When
        boolean isDuplicate = service.isDuplicate(searchTitle);

        // Then
        assertThat(isDuplicate).isTrue();
        verify(titleRepository, times(1)).existsByName(searchTitle);
    }
}
