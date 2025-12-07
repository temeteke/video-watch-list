package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.application.dto.TitleDetailDTO;
import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.SeriesReadModel;
import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.ViewingRecordReadModel;
import com.example.videowatchlog.application.readmodel.service.TitleReadService;
import com.example.videowatchlog.domain.model.WatchStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GetTitleDetailUseCaseTest - タイトル詳細取得ユースケーステスト
 *
 * Phase 7: TitleReadService を使用した Read Model テスト
 */
@DisplayName("GetTitleDetailUseCase")
class GetTitleDetailUseCaseTest {

    private GetTitleDetailUseCase useCase;
    private TitleReadService titleReadService;

    @BeforeEach
    void setUp() {
        titleReadService = mock(TitleReadService.class);
        useCase = new GetTitleDetailUseCase(titleReadService);
    }

    @Test
    @DisplayName("タイトル詳細を正しく取得できること")
    void testExecuteSuccess() {
        // Arrange
        Long titleId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Read Model データを構築
        ViewingRecordReadModel viewingRecord = new ViewingRecordReadModel(
                1L, 1L, now, 4, "Great episode", now
        );

        EpisodeReadModel episode = new EpisodeReadModel(
                1L, 1L, "Episode 1", WatchStatus.WATCHED,
                List.of("https://example.com/watch"),
                List.of(viewingRecord),
                now, now
        );

        SeriesReadModel series = new SeriesReadModel(
                1L, titleId, "Series 1",
                List.of(episode),
                now, now
        );

        TitleDetailReadModel titleDetail = new TitleDetailReadModel(
                titleId, "Title 1",
                List.of(series),
                now, now
        );

        when(titleReadService.getTitleDetail(titleId)).thenReturn(Optional.of(titleDetail));

        // Act
        TitleDetailDTO result = useCase.execute(titleId);

        // Assert
        assertNotNull(result);
        assertEquals(titleId, result.getId());
        assertEquals("Title 1", result.getName());
        assertEquals(1, result.getSeries().size());

        // Series の検証
        TitleDetailDTO.SeriesDetailDTO seriesDTO = result.getSeries().get(0);
        assertEquals(1L, seriesDTO.getId());
        assertEquals(titleId, seriesDTO.getTitleId());
        assertEquals("Series 1", seriesDTO.getName());
        assertEquals(1, seriesDTO.getEpisodes().size());

        // Episode の検証
        TitleDetailDTO.EpisodeDetailDTO episodeDTO = seriesDTO.getEpisodes().get(0);
        assertEquals(1L, episodeDTO.getId());
        assertEquals(1L, episodeDTO.getSeriesId());
        assertEquals("Episode 1", episodeDTO.getEpisodeInfo());
        assertEquals("WATCHED", episodeDTO.getWatchStatus());
        assertEquals(1, episodeDTO.getWatchPageUrls().size());
        assertEquals(1, episodeDTO.getViewingRecords().size());

        // ViewingRecord の検証
        TitleDetailDTO.ViewingRecordDTO recordDTO = episodeDTO.getViewingRecords().get(0);
        assertEquals(1L, recordDTO.getId());
        assertEquals(1L, recordDTO.getEpisodeId());
        assertEquals(4, recordDTO.getRating());
        assertEquals("Great episode", recordDTO.getComment());

        verify(titleReadService).getTitleDetail(titleId);
    }

    @Test
    @DisplayName("タイトルが見つからない場合に例外が発生すること")
    void testExecuteNotFound() {
        // Arrange
        Long titleId = 999L;
        when(titleReadService.getTitleDetail(titleId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(titleId)
        );

        assertTrue(exception.getMessage().contains("タイトルが見つかりません"));
        assertTrue(exception.getMessage().contains("999"));

        verify(titleReadService).getTitleDetail(titleId);
    }

    @Test
    @DisplayName("複数 Series を持つタイトル詳細が取得できること")
    void testExecuteWithMultipleSeries() {
        // Arrange
        Long titleId = 2L;
        LocalDateTime now = LocalDateTime.now();

        // 複数の Series を持つ詳細データ
        EpisodeReadModel episode1 = new EpisodeReadModel(
                1L, 1L, "Episode 1", WatchStatus.WATCHED, List.of(), List.of(), now, now
        );
        EpisodeReadModel episode2 = new EpisodeReadModel(
                2L, 1L, "Episode 2", WatchStatus.UNWATCHED, List.of(), List.of(), now, now
        );

        SeriesReadModel series1 = new SeriesReadModel(
                1L, titleId, "Series 1",
                List.of(episode1, episode2),
                now, now
        );

        EpisodeReadModel episode3 = new EpisodeReadModel(
                3L, 2L, "Episode 1", WatchStatus.WATCHED, List.of(), List.of(), now, now
        );

        SeriesReadModel series2 = new SeriesReadModel(
                2L, titleId, "Series 2",
                List.of(episode3),
                now, now
        );

        TitleDetailReadModel titleDetail = new TitleDetailReadModel(
                titleId, "Title 2",
                List.of(series1, series2),
                now, now
        );

        when(titleReadService.getTitleDetail(titleId)).thenReturn(Optional.of(titleDetail));

        // Act
        TitleDetailDTO result = useCase.execute(titleId);

        // Assert
        assertNotNull(result);
        assertEquals(titleId, result.getId());
        assertEquals("Title 2", result.getName());
        assertEquals(2, result.getSeries().size());

        // Series 1 の検証
        assertEquals("Series 1", result.getSeries().get(0).getName());
        assertEquals(2, result.getSeries().get(0).getEpisodes().size());

        // Series 2 の検証
        assertEquals("Series 2", result.getSeries().get(1).getName());
        assertEquals(1, result.getSeries().get(1).getEpisodes().size());
    }

    @Test
    @DisplayName("空の Series を持つタイトル詳細が取得できること")
    void testExecuteWithEmptySeries() {
        // Arrange
        Long titleId = 3L;
        LocalDateTime now = LocalDateTime.now();

        TitleDetailReadModel titleDetail = new TitleDetailReadModel(
                titleId, "Title 3",
                List.of(), // 空の Series
                now, now
        );

        when(titleReadService.getTitleDetail(titleId)).thenReturn(Optional.of(titleDetail));

        // Act
        TitleDetailDTO result = useCase.execute(titleId);

        // Assert
        assertNotNull(result);
        assertEquals(titleId, result.getId());
        assertEquals("Title 3", result.getName());
        assertEquals(0, result.getSeries().size());

        verify(titleReadService).getTitleDetail(titleId);
    }

    @Test
    @DisplayName("複数の ViewingRecord を持つ Episode が正しく変換されること")
    void testExecuteWithMultipleViewingRecords() {
        // Arrange
        Long titleId = 4L;
        LocalDateTime now = LocalDateTime.now();

        ViewingRecordReadModel record1 = new ViewingRecordReadModel(
                1L, 1L, now.minusDays(2), 5, "Excellent", now.minusDays(2)
        );
        ViewingRecordReadModel record2 = new ViewingRecordReadModel(
                2L, 1L, now.minusDays(1), 4, "Good", now.minusDays(1)
        );

        EpisodeReadModel episode = new EpisodeReadModel(
                1L, 1L, "Episode 1", WatchStatus.WATCHED,
                List.of("https://example.com/ep1"),
                List.of(record1, record2),
                now, now
        );

        SeriesReadModel series = new SeriesReadModel(
                1L, titleId, "Series 1",
                List.of(episode),
                now, now
        );

        TitleDetailReadModel titleDetail = new TitleDetailReadModel(
                titleId, "Title 4",
                List.of(series),
                now, now
        );

        when(titleReadService.getTitleDetail(titleId)).thenReturn(Optional.of(titleDetail));

        // Act
        TitleDetailDTO result = useCase.execute(titleId);

        // Assert
        TitleDetailDTO.EpisodeDetailDTO episodeDTO = result.getSeries().get(0).getEpisodes().get(0);
        assertEquals(2, episodeDTO.getViewingRecords().size());
        assertEquals("Excellent", episodeDTO.getViewingRecords().get(0).getComment());
        assertEquals("Good", episodeDTO.getViewingRecords().get(1).getComment());
    }
}
