package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.infrastructure.persistence.ViewingRecordMapper;
import com.example.videowatchlog.infrastructure.persistence.entity.ViewingRecordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for DeleteViewingRecordUseCase.
 * Tests the workflow of deleting viewing records and reverting to UNWATCHED when necessary.
 */
@DisplayName("DeleteViewingRecordUseCase")
class DeleteViewingRecordUseCaseTest {
    @Mock
    private ViewingRecordMapper viewingRecordMapper;

    @Mock
    private EpisodeRepository episodeRepository;

    private DeleteViewingRecordUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new DeleteViewingRecordUseCase(episodeRepository, viewingRecordMapper);
    }

    @Test
    @DisplayName("should delete viewing record from watched episode with multiple records")
    void testDeleteViewingRecordKeepsWatched() {
        // Arrange
        Long recordId = 1L;
        Long episodeId = 1L;

        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        ViewingRecord record1 = new ViewingRecord(1L, episodeId, LocalDateTime.now().minusHours(2), 4, "First", LocalDateTime.now().minusHours(2));
        ViewingRecord record2 = new ViewingRecord(2L, episodeId, LocalDateTime.now().minusHours(1), 5, "Second", LocalDateTime.now().minusHours(1));

        episode.addViewingRecord(record1);
        episode.addViewingRecord(record2);

        // Create entity for mocking
        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(1L);
        entity.setEpisodeId(episodeId);
        entity.setWatchedAt(LocalDateTime.now().minusHours(2));
        entity.setRating(4);
        entity.setComment("First");
        entity.setRecordedAt(LocalDateTime.now().minusHours(2));

        when(viewingRecordMapper.findById(recordId)).thenReturn(Optional.of(entity));
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        useCase.execute(recordId);

        // Assert
        assertEquals(WatchStatus.WATCHED, episode.getWatchStatus());
        assertEquals(1, episode.getViewingRecords().size());

        verify(episodeRepository, times(1)).save(episode);
    }

    @Test
    @DisplayName("should delete last viewing record and revert episode to UNWATCHED")
    void testDeleteLastViewingRecordRevertsToUnwatched() {
        // Arrange
        Long recordId = 1L;
        Long episodeId = 1L;

        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        ViewingRecord record = new ViewingRecord(1L, episodeId, LocalDateTime.now().minusHours(1), 4, "Only", LocalDateTime.now().minusHours(1));
        episode.addViewingRecord(record);

        // Create entity for mocking
        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(1L);
        entity.setEpisodeId(episodeId);
        entity.setWatchedAt(LocalDateTime.now().minusHours(1));
        entity.setRating(4);
        entity.setComment("Only");
        entity.setRecordedAt(LocalDateTime.now().minusHours(1));

        when(viewingRecordMapper.findById(recordId)).thenReturn(Optional.of(entity));
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        useCase.execute(recordId);

        // Assert
        assertEquals(WatchStatus.UNWATCHED, episode.getWatchStatus());
        assertEquals(0, episode.getViewingRecords().size());

        verify(episodeRepository, times(1)).save(episode);
    }

    @Test
    @DisplayName("should throw exception if viewing record not found")
    void testRecordNotFound() {
        // Arrange
        Long recordId = 999L;

        when(viewingRecordMapper.findById(recordId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(recordId),
                "Should throw exception when record not found"
        );

        verify(episodeRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw exception if episode not found")
    void testEpisodeNotFound() {
        // Arrange
        Long recordId = 1L;
        Long episodeId = 1L;

        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(1L);
        entity.setEpisodeId(episodeId);

        when(viewingRecordMapper.findById(recordId)).thenReturn(Optional.of(entity));
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(recordId),
                "Should throw exception when episode not found"
        );

        verify(episodeRepository, never()).save(any());
    }
}
