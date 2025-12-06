package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.service.ViewingRecordIdService;
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
 * Tests for AddViewingRecordUseCase.
 * Tests the workflow of adding viewing records to already watched episodes.
 */
@DisplayName("AddViewingRecordUseCase")
class AddViewingRecordUseCaseTest {
    @Mock
    private ViewingRecordIdService viewingRecordIdService;

    @Mock
    private EpisodeRepository episodeRepository;

    private AddViewingRecordUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new AddViewingRecordUseCase(viewingRecordIdService, episodeRepository);
    }

    @Test
    @DisplayName("should add viewing record to watched episode")
    void testAddViewingRecordToWatchedEpisode() {
        // Arrange
        Long episodeId = 1L;
        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);
        Integer rating = 3;
        String comment = "Good episode";

        when(viewingRecordIdService.generateId()).thenReturn(1L);
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        useCase.execute(episodeId, watchedAt, rating, comment);

        // Assert
        assertEquals(WatchStatus.WATCHED, episode.getWatchStatus());
        assertEquals(1, episode.getViewingRecords().size()); // Only the one we just added

        ViewingRecord record = episode.getViewingRecords().get(0);
        assertEquals(episodeId, record.getEpisodeId());
        assertEquals(watchedAt, record.getWatchedAt());
        assertEquals(rating, record.getRating());
        assertEquals(comment, record.getComment());

        verify(episodeRepository, times(1)).findById(episodeId);
        verify(episodeRepository, times(1)).save(episode);
    }

    @Test
    @DisplayName("should throw exception when adding to unwatched episode")
    void testAddViewingRecordToUnwatchedEpisodeFails() {
        // Arrange
        Long episodeId = 1L;
        Episode episode = Episode.create(1L, 1L, "Episode 1");
        // episode is UNWATCHED by default

        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);
        Integer rating = 4;
        String comment = "Great episode";

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                useCase.execute(episodeId, watchedAt, rating, comment),
                "Should not allow adding viewing record to unwatched episode"
        );

        verify(episodeRepository, never()).save(episode);
    }

    @Test
    @DisplayName("should throw exception for invalid rating")
    void testInvalidRating() {
        // Arrange
        Long episodeId = 1L;
        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);
        Integer rating = 10; // Invalid
        String comment = "Good episode";

        when(viewingRecordIdService.generateId()).thenReturn(1L);
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(episodeId, watchedAt, rating, comment),
                "Should not allow invalid rating"
        );

        verify(episodeRepository, never()).save(episode);
    }

    @Test
    @DisplayName("should throw exception for future watched date")
    void testFutureWatchedAt() {
        // Arrange
        Long episodeId = 1L;
        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        LocalDateTime watchedAt = LocalDateTime.now().plusHours(1); // Future
        Integer rating = 4;
        String comment = "Great episode";

        when(viewingRecordIdService.generateId()).thenReturn(1L);
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(episodeId, watchedAt, rating, comment),
                "Should not allow future watched date"
        );

        verify(episodeRepository, never()).save(episode);
    }

    @Test
    @DisplayName("should allow null comment")
    void testNullComment() {
        // Arrange
        Long episodeId = 1L;
        Episode episode = Episode.create(1L, 1L, "Episode 1");
        episode.markAsWatched();

        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);
        Integer rating = 5;
        String comment = null;

        when(viewingRecordIdService.generateId()).thenReturn(1L);
        when(episodeRepository.findById(episodeId)).thenReturn(Optional.of(episode));

        // Act
        useCase.execute(episodeId, watchedAt, rating, comment);

        // Assert
        assertEquals(1, episode.getViewingRecords().size());
        ViewingRecord record = episode.getViewingRecords().get(0);
        assertNull(record.getComment());

        verify(episodeRepository, times(1)).save(episode);
    }

    @Test
    @DisplayName("should throw exception if episode not found")
    void testEpisodeNotFound() {
        // Arrange
        Long episodeId = 999L;

        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);
        Integer rating = 4;
        String comment = "Great episode";

        when(episodeRepository.findById(episodeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(episodeId, watchedAt, rating, comment),
                "Should throw exception when episode not found"
        );

        verify(episodeRepository, never()).save(any());
    }
}
