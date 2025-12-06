package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import com.example.videowatchlog.infrastructure.persistence.ViewingRecordMapper;
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
 */
@Service
public class DeleteViewingRecordUseCase {
    private final EpisodeRepository episodeRepository;
    private final ViewingRecordMapper viewingRecordMapper;

    public DeleteViewingRecordUseCase(EpisodeRepository episodeRepository, ViewingRecordMapper viewingRecordMapper) {
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
        this.viewingRecordMapper = Objects.requireNonNull(viewingRecordMapper, "viewingRecordMapper must not be null");
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
        com.example.videowatchlog.infrastructure.persistence.entity.ViewingRecordEntity recordEntity =
            viewingRecordMapper.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Viewing record not found: " + recordId));

        // Fetch episode
        Episode episode = episodeRepository.findById(recordEntity.getEpisodeId())
                .orElseThrow(() -> new IllegalArgumentException("Episode not found: " + recordEntity.getEpisodeId()));

        // Remove viewing record from episode
        // This handles the logic: if no records remain, episode reverts to UNWATCHED
        ViewingRecord record = recordEntity.toDomain();
        episode.removeViewingRecord(record);

        // Persist episode changes (also deletes the record via cascade)
        episodeRepository.save(episode);
    }
}
