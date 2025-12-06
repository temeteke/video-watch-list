package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.service.ViewingRecordIdService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AddViewingRecordUseCase - Adds an additional viewing record to an already watched episode.
 *
 * Business rules:
 * - Episode must be in WATCHED state (cannot add to unwatched episodes)
 * - Viewing record with rating (1-5) and optional comment is created
 * - Watched date must not be in the future
 * - Episode status remains WATCHED
 */
@Service
public class AddViewingRecordUseCase {
    private final ViewingRecordIdService viewingRecordIdService;
    private final EpisodeRepository episodeRepository;

    public AddViewingRecordUseCase(ViewingRecordIdService viewingRecordIdService, EpisodeRepository episodeRepository) {
        this.viewingRecordIdService = Objects.requireNonNull(viewingRecordIdService, "viewingRecordIdService must not be null");
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
    }

    /**
     * Adds a viewing record to an already watched episode.
     *
     * @param episodeId ID of the episode
     * @param watchedAt Date and time when the episode was watched (must not be in future)
     * @param rating User rating (1-5)
     * @param comment User comment (optional, max 2000 characters)
     * @throws IllegalArgumentException if episode not found or validation fails
     * @throws IllegalStateException if episode is not watched
     */
    public void execute(Long episodeId, LocalDateTime watchedAt, Integer rating, String comment) {
        // Validate input
        Objects.requireNonNull(episodeId, "episodeId must not be null");
        Objects.requireNonNull(watchedAt, "watchedAt must not be null");
        Objects.requireNonNull(rating, "rating must not be null");

        // Fetch episode
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("Episode not found: " + episodeId));

        // Check episode is watched (can only add records to watched episodes)
        if (episode.getWatchStatus() != WatchStatus.WATCHED) {
            throw new IllegalStateException("Can only add viewing records to watched episodes");
        }

        // Create viewing record (validation happens in ViewingRecord constructor)
        Long id = viewingRecordIdService.generateId();
        ViewingRecord viewingRecord = ViewingRecord.create(id, episodeId, watchedAt, rating, comment);

        // Add viewing record to episode
        episode.addViewingRecord(viewingRecord);

        // Persist changes
        episodeRepository.save(episode);
    }
}
