package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * DeleteViewingRecordUseCase - Deletes a viewing record from an episode.
 *
 * Business rules:
 * - Viewing record must exist
 * - Episode must exist and have the viewing record
 * - If all viewing records are deleted, episode reverts to UNWATCHED
 *
 * Note: ViewingRecord is part of the Episode aggregate.
 * Deletion is handled through EpisodeRepository only.
 *
 * Architecture Decision:
 * This UseCase depends on Domain layer repository interfaces only:
 * - EpisodeRepository (domain.repository)
 * - ViewingRecordRepository (domain.repository)
 * This ensures strict adherence to Onion Architecture and dependency inversion.
 */
@Service
public class DeleteViewingRecordUseCase {
    private final EpisodeRepository episodeRepository;
    private final ViewingRecordRepository viewingRecordRepository;

    public DeleteViewingRecordUseCase(EpisodeRepository episodeRepository, ViewingRecordRepository viewingRecordRepository) {
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
        this.viewingRecordRepository = Objects.requireNonNull(viewingRecordRepository, "viewingRecordRepository must not be null");
    }

    /**
     * Deletes a viewing record.
     * If all viewing records are deleted, the episode reverts to UNWATCHED.
     *
     * @param recordId ID of the viewing record to delete
     * @throws IllegalArgumentException if record or episode not found
     */
    @Transactional
    public void execute(Long recordId) {
        // Validate input
        Objects.requireNonNull(recordId, "recordId must not be null");

        // Fetch viewing record to get episode ID (ViewingRecord is part of Episode aggregate)
        ViewingRecord record = viewingRecordRepository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("Viewing record not found: " + recordId));

        // Fetch episode
        Episode episode = episodeRepository.findById(record.getEpisodeId())
                .orElseThrow(() -> new IllegalArgumentException("Episode not found: " + record.getEpisodeId()));

        // Remove viewing record from episode
        // This handles the logic: if no records remain, episode reverts to UNWATCHED
        episode.removeViewingRecord(record);

        // Persist episode changes (also deletes the record via cascade)
        episodeRepository.save(episode);
    }
}
