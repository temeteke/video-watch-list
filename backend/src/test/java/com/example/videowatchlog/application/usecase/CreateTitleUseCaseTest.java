package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateTitleRequestDTO;
import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import com.example.videowatchlog.domain.service.TitleDuplicationCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CreateTitleUseCase")
@ExtendWith(MockitoExtension.class)
class CreateTitleUseCaseTest {

    @Mock
    private TitleRepository titleRepository;

    @Mock
    private TitleDuplicationCheckService duplicationCheckService;

    @InjectMocks
    private CreateTitleUseCase useCase;

    @Test
    @DisplayName("タイトルを作成できる")
    void shouldCreateTitle() {
        // Given
        CreateTitleRequestDTO request = new CreateTitleRequestDTO("進撃の巨人", null);
        when(duplicationCheckService.isDuplicate("進撃の巨人")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenAnswer(invocation -> {
            Title title = invocation.getArgument(0);
            title.setId(1L);
            return title;
        });

        // When
        TitleSummaryDTO result = useCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("進撃の巨人");
        verify(duplicationCheckService, times(1)).isDuplicate("進撃の巨人");
        verify(titleRepository, times(1)).save(any(Title.class));
    }

    @Test
    @DisplayName("重複したタイトルは作成できない")
    void shouldThrowExceptionWhenTitleDuplicated() {
        // Given
        CreateTitleRequestDTO request = new CreateTitleRequestDTO("進撃の巨人", null);
        when(duplicationCheckService.isDuplicate("進撃の巨人")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("すでに存在します");
        verify(titleRepository, never()).save(any());
    }

    @Test
    @DisplayName("デフォルトシリーズが自動生成される")
    void shouldCreateDefaultSeries() {
        // Given
        CreateTitleRequestDTO request = new CreateTitleRequestDTO("鬼滅の刃", null);
        when(duplicationCheckService.isDuplicate("鬼滅の刃")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenAnswer(invocation -> {
            Title title = invocation.getArgument(0);
            title.setId(1L);
            return title;
        });

        // When
        useCase.execute(request);

        // Then
        verify(titleRepository, times(1)).save(argThat(title ->
                title.getName().equals("鬼滅の刃") &&
                        !title.getSeries().isEmpty()
        ));
    }
}
