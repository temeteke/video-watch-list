package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.service.EntityIdentityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CompleteEpisodeUseCase - Marks an episode as watched and creates a viewing record.
 *
 * Business rules:
 * - Episode must be in UNWATCHED state (cannot complete already watched episodes)
 * - Viewing record with rating (1-5) and optional comment is created
 * - Watched date must not be in the future
 * - Episode status is updated to WATCHED
 */
@Service
public class CompleteEpisodeUseCase {
    private final EntityIdentityService identityService;
    private final EpisodeRepository episodeRepository;

    public CompleteEpisodeUseCase(EntityIdentityService identityService, EpisodeRepository episodeRepository) {
        this.identityService = Objects.requireNonNull(identityService, "identityService must not be null");
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
    }

    /**
     * Marks an episode as watched and records a viewing record.
     *
     * @param episodeId ID of the episode to mark as watched
     * @param watchedAt Date and time when the episode was watched (must not be in future)
     * @param rating User rating (1-5)
     * @param comment User comment (optional, max 2000 characters)
     * @throws IllegalArgumentException if episode not found or validation fails
     * @throws IllegalStateException if episode is already watched
     */
    public void execute(Long episodeId, LocalDateTime watchedAt, Integer rating, String comment) {
        // Validate input
        Objects.requireNonNull(episodeId, "episodeId must not be null");
        Objects.requireNonNull(watchedAt, "watchedAt must not be null");
        Objects.requireNonNull(rating, "rating must not be null");

        // Fetch episode
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("Episode not found: " + episodeId));

        // Check episode is not already watched
        if (episode.getWatchStatus() == WatchStatus.WATCHED) {
            throw new IllegalStateException("Cannot complete episode that is already watched");
        }

        // Create viewing record (validation happens in ViewingRecord constructor)
        Long id = identityService.generateId();
        ViewingRecord viewingRecord = ViewingRecord.create(id, episodeId, watchedAt, rating, comment);

        // Update episode state
        episode.markAsWatched();
        episode.addViewingRecord(viewingRecord);

        // Persist changes
        episodeRepository.save(episode);
    }
}
