package com.example.videowatchlog.application.usecase;

import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.repository.EpisodeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * GetViewingRecordsUseCase - Retrieves viewing records for an episode.
 *
 * Business rules:
 * - Returns viewing records sorted by recorded date in descending order (newest first)
 * - Returns empty list if episode has no records
 */
@Service
public class GetViewingRecordsUseCase {
    private final EpisodeRepository episodeRepository;

    public GetViewingRecordsUseCase(EpisodeRepository episodeRepository) {
        this.episodeRepository = Objects.requireNonNull(episodeRepository, "episodeRepository must not be null");
    }

    /**
     * Retrieves viewing records for an episode, sorted by recorded date (newest first).
     *
     * @param episodeId ID of the episode
     * @return List of viewing records sorted by recordedAt descending
     */
    public List<ViewingRecord> execute(Long episodeId) {
        // Validate input
        Objects.requireNonNull(episodeId, "episodeId must not be null");

        // Fetch viewing records from the episode aggregate
        List<ViewingRecord> records = episodeRepository.findById(episodeId)
                .map(episode -> new java.util.ArrayList<>(episode.getViewingRecords()))
                .orElse(new java.util.ArrayList<>());

        // Sort by recorded date descending (newest first)
        records.sort(Comparator.comparing(ViewingRecord::getRecordedAt).reversed());

        return records;
    }
}
