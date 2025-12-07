package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.CreateTitleRequestDTO;
import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.repository.TitleRepository;
import com.example.videowatchlog.domain.service.TitleIdService;
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
    private TitleIdService titleIdService;

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
        when(titleIdService.generateId()).thenReturn(1L);
        when(duplicationCheckService.isDuplicate("進撃の巨人")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenAnswer(invocation -> {
            Title title = invocation.getArgument(0);
            // 不変なため、IDが設定された新しいインスタンスを返す
            // Phase 7: Series パラメータを削除
            return new Title(title.getId(), title.getName(), title.getTitleInfoUrls(),
                           title.getCreatedAt(), title.getUpdatedAt());
        });

        // When
        TitleSummaryDTO result = useCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("進撃の巨人");
        verify(titleIdService, times(1)).generateId();
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
        verify(titleIdService, never()).generateId();
        verify(titleRepository, never()).save(any());
    }

    @Test
    @DisplayName("タイトルが正しく保存される")
    void shouldSaveTitle() {
        // Given
        CreateTitleRequestDTO request = new CreateTitleRequestDTO("鬼滅の刃", null);
        when(titleIdService.generateId()).thenReturn(2L);
        when(duplicationCheckService.isDuplicate("鬼滅の刃")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenAnswer(invocation -> {
            Title title = invocation.getArgument(0);
            // 不変なため、IDが設定された新しいインスタンスを返す
            // Phase 7: Series パラメータを削除
            return new Title(title.getId(), title.getName(), title.getTitleInfoUrls(),
                           title.getCreatedAt(), title.getUpdatedAt());
        });

        // When
        useCase.execute(request);

        // Then
        verify(titleIdService, times(1)).generateId();
        verify(titleRepository, times(1)).save(argThat(title ->
                title.getId().equals(2L) && title.getName().equals("鬼滅の刃")
        ));
    }
}
