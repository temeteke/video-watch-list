package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for GetViewingRecordsUseCase.
 * Tests the workflow of retrieving viewing records sorted by recorded date (newest first).
 */
@DisplayName("GetViewingRecordsUseCase")
class GetViewingRecordsUseCaseTest {
    @Mock
    private ViewingRecordRepository viewingRecordRepository;

    private GetViewingRecordsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetViewingRecordsUseCase(viewingRecordRepository);
    }

    @Test
    @DisplayName("should return viewing records sorted by recorded date descending")
    void testGetViewingRecordsSorted() {
        // Arrange
        Long episodeId = 1L;

        LocalDateTime now = LocalDateTime.now();
        ViewingRecord record1 = ViewingRecord.create(episodeId, now.minusHours(3), 4, "First");
        ViewingRecord record2 = ViewingRecord.create(episodeId, now.minusHours(2), 5, "Second");
        ViewingRecord record3 = ViewingRecord.create(episodeId, now.minusHours(1), 3, "Third");

        List<ViewingRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        records.add(record3);

        when(viewingRecordRepository.findByEpisodeId(episodeId)).thenReturn(records);

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(3, result.size());
        // Should be sorted newest first (descending by recordedAt)
        assertTrue(result.get(0).getRecordedAt().isAfter(result.get(1).getRecordedAt()));
        assertTrue(result.get(1).getRecordedAt().isAfter(result.get(2).getRecordedAt()));

        verify(viewingRecordRepository, times(1)).findByEpisodeId(episodeId);
    }

    @Test
    @DisplayName("should return empty list when episode has no viewing records")
    void testEmptyRecords() {
        // Arrange
        Long episodeId = 1L;

        when(viewingRecordRepository.findByEpisodeId(episodeId)).thenReturn(new ArrayList<>());

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(0, result.size());

        verify(viewingRecordRepository, times(1)).findByEpisodeId(episodeId);
    }

    @Test
    @DisplayName("should return single viewing record")
    void testSingleRecord() {
        // Arrange
        Long episodeId = 1L;

        ViewingRecord record = ViewingRecord.create(episodeId, LocalDateTime.now().minusHours(1), 5, "Single");

        List<ViewingRecord> records = new ArrayList<>();
        records.add(record);

        when(viewingRecordRepository.findByEpisodeId(episodeId)).thenReturn(records);

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(record.getEpisodeId(), result.get(0).getEpisodeId());
        assertEquals(record.getRating(), result.get(0).getRating());

        verify(viewingRecordRepository, times(1)).findByEpisodeId(episodeId);
    }

    @Test
    @DisplayName("should maintain order with unsorted input")
    void testSortingWithUnsortedInput() {
        // Arrange
        Long episodeId = 1L;

        LocalDateTime base = LocalDateTime.now();
        ViewingRecord record1 = ViewingRecord.create(episodeId, base.minusHours(1), 3, "First");
        ViewingRecord record2 = ViewingRecord.create(episodeId, base.minusHours(3), 4, "Third");
        ViewingRecord record3 = ViewingRecord.create(episodeId, base.minusHours(2), 5, "Second");

        List<ViewingRecord> unsortedRecords = new ArrayList<>();
        unsortedRecords.add(record1);
        unsortedRecords.add(record2);
        unsortedRecords.add(record3);

        when(viewingRecordRepository.findByEpisodeId(episodeId)).thenReturn(unsortedRecords);

        // Act
        List<ViewingRecord> result = useCase.execute(episodeId);

        // Assert
        assertEquals(3, result.size());
        // Should be sorted newest first regardless of input order
        assertTrue(result.get(0).getRecordedAt().isAfter(result.get(1).getRecordedAt()));
        assertTrue(result.get(1).getRecordedAt().isAfter(result.get(2).getRecordedAt()));

        verify(viewingRecordRepository, times(1)).findByEpisodeId(episodeId);
    }
}
