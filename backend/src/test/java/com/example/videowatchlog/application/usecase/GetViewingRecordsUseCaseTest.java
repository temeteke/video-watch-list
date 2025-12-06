package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for GetViewingRecordsUseCase.
 * Tests the workflow of retrieving viewing records sorted by recorded date (newest first).
 */
@DisplayName("GetViewingRecordsUseCase")
class GetViewingRecordsUseCaseTest {
    @Mock
    private EpisodeRepository episodeRepository;

    private GetViewingRecordsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetViewingRecordsUseCase(episodeRepository);
    }

    @Test
    @DisplayName("should return viewing records sorted by recorded date descending")
    void testGetViewingRecordsSorted() {
        // Arrange
        Long episodeId = 1L;

        Episode episode = Episode.create(episodeId, 1L, "Episode 1");
        LocalDateTime now = LocalDateTime.now();
        ViewingRecord record1 = ViewingRecord.create(1L, episodeId, now.minusHours(3), 4, "First");
        ViewingRecord record2 = ViewingRecord.create(2L, episodeId, now.minusHours(2), 5, "Second");
        ViewingRecord record3 = ViewingRecord.create(3L, episodeId, now.minusHours(1), 3, "Third");

        episode.addViewingRecord(record1);
        episode.addViewingRecord(record2);
        episode.addViewingRecord(record3);

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(3, result.size());
        // Should be sorted newest first (descending by recordedAt)
        assertTrue(result.get(0).getRecordedAt().isAfter(result.get(1).getRecordedAt()));
        assertTrue(result.get(1).getRecordedAt().isAfter(result.get(2).getRecordedAt()));

        verify(episodeRepository, times(1)).findById(episodeId);
    }

    @Test
    @DisplayName("should return empty list when episode has no viewing records")
    void testEmptyRecords() {
        // Arrange
        Long episodeId = 1L;

        Episode episode = Episode.create(episodeId, 1L, "Episode 1");

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(0, result.size());

        verify(episodeRepository, times(1)).findById(episodeId);
    }

    @Test
    @DisplayName("should return single viewing record")
    void testSingleRecord() {
        // Arrange
        Long episodeId = 1L;

        Episode episode = Episode.create(episodeId, 1L, "Episode 1");
        ViewingRecord record = ViewingRecord.create(1L, episodeId, LocalDateTime.now().minusHours(1), 5, "Single");
        episode.addViewingRecord(record);

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(record.getEpisodeId(), result.get(0).getEpisodeId());
        assertEquals(record.getRating(), result.get(0).getRating());

        verify(episodeRepository, times(1)).findById(episodeId);
    }

    @Test
    @DisplayName("should maintain order with unsorted input")
    void testSortingWithUnsortedInput() {
        // Arrange
        Long episodeId = 1L;

        Episode episode = Episode.create(episodeId, 1L, "Episode 1");
        LocalDateTime base = LocalDateTime.now();
        ViewingRecord record1 = ViewingRecord.create(1L, episodeId, base.minusHours(1), 3, "First");
        ViewingRecord record2 = ViewingRecord.create(2L, episodeId, base.minusHours(3), 4, "Third");
        ViewingRecord record3 = ViewingRecord.create(3L, episodeId, base.minusHours(2), 5, "Second");

        episode.addViewingRecord(record1);
        episode.addViewingRecord(record2);
        episode.addViewingRecord(record3);

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(3, result.size());
        // Should be sorted newest first regardless of input order
        assertTrue(result.get(0).getRecordedAt().isAfter(result.get(1).getRecordedAt()));
        assertTrue(result.get(1).getRecordedAt().isAfter(result.get(2).getRecordedAt()));

        verify(episodeRepository, times(1)).findById(episodeId);
    }
}
