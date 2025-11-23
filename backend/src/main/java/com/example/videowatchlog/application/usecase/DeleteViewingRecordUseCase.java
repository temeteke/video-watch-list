package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.domain.repository.ViewingRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * DeleteViewingRecordUseCase - Deletes a viewing record from an episode.
 *
 * Business rules:
 * - Viewing record must exist
 * - Episode must exist and have the viewing record
 * - If all viewing records are deleted, episode reverts to UNWATCHED
 */
@Service
public class DeleteViewingRecordUseCase {
    private final ViewingRecordRepository viewingRecordRepository;
    private final EpisodeRepository episodeRepository;

    public DeleteViewingRecordUseCase(ViewingRecordRepository viewingRecordRepository, EpisodeRepository episodeRepository) {
        this.viewingRecordRepository = Objects.requireNonNull(viewingRecordRepository, "viewingRecordRepository must not be null");
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
    }

    /**
     * Deletes a viewing record.
     * If all viewing records are deleted, the episode reverts to UNWATCHED.
     *
     * @param recordId ID of the viewing record to delete
     * @throws IllegalArgumentException if record or episode not found
     */
    public void execute(Long recordId) {
        // Validate input
        Objects.requireNonNull(recordId, "recordId must not be null");

        // Fetch viewing record
        ViewingRecord record = viewingRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Viewing record not found: " + recordId));

        // Fetch episode
        Episode episode = episodeRepository.findById(record.getEpisodeId())
                .orElseThrow(() -> new IllegalArgumentException("Episode not found: " + record.getEpisodeId()));

        // Remove viewing record from episode
        // This handles the logic: if no records remain, episode reverts to UNWATCHED
        episode.removeViewingRecord(record);

        // Delete the record from repository
        viewingRecordRepository.delete(recordId);

        // Persist episode changes (status may have changed)
        episodeRepository.save(episode);
    }
}
