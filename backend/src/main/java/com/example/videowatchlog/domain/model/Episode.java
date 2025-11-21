package com.example.videowatchlog.domain.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Episode entity.
 * Represents an individual episode within a Series.
 *
 * Key business rules:
 * - Episode info can be empty (for default episodes)
 * - Episode info must not exceed 200 characters
 * - Watch status starts as UNWATCHED
 * - Once WATCHED, cannot be changed back to UNWATCHED (except when all viewing records are deleted)
 * - Deleting an episode cascades to all ViewingRecords
 */
public class Episode {
    private final Long id;
    private final Long seriesId;
    private String episodeInfo;
    private final List<WatchPageUrl> watchPageUrls;
    private WatchStatus watchStatus;
    private final List<ViewingRecord> viewingRecords;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Constructor for Episode entity.
     *
     * @param id Unique identifier (null for new entities)
     * @param seriesId Parent series ID (required)
     * @param episodeInfo Episode information (can be empty, max 200 characters)
     * @param watchPageUrls List of watch page URLs (0 or more)
     * @param watchStatus Watch status (UNWATCHED or WATCHED)
     * @param viewingRecords List of viewing records (0 or more)
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     * @throws IllegalArgumentException if validation fails
     */
    public Episode(Long id, Long seriesId, String episodeInfo, List<WatchPageUrl> watchPageUrls,
                   WatchStatus watchStatus, List<ViewingRecord> viewingRecords,
                   ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.seriesId = Objects.requireNonNull(seriesId, "seriesId must not be null");
        validateEpisodeInfo(episodeInfo);
        this.episodeInfo = episodeInfo != null ? episodeInfo : "";
        this.watchPageUrls = watchPageUrls != null ? new ArrayList<>(watchPageUrls) : new ArrayList<>();
        this.watchStatus = Objects.requireNonNull(watchStatus, "watchStatus must not be null");
        this.viewingRecords = viewingRecords != null ? new ArrayList<>(viewingRecords) : new ArrayList<>();
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    /**
     * Validates episode info according to business rules.
     *
     * @param episodeInfo Episode info to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateEpisodeInfo(String episodeInfo) {
        if (episodeInfo != null && episodeInfo.length() > 200) {
            throw new IllegalArgumentException("Episode info must not exceed 200 characters");
        }
    }

    /**
     * Marks this episode as watched.
     * Can only transition from UNWATCHED to WATCHED.
     */
    public void markAsWatched() {
        this.watchStatus = WatchStatus.WATCHED;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Marks this episode as unwatched.
     * This method should not be called directly - use markAsUnwatchedIfNoRecords instead.
     *
     * @throws IllegalStateException always, as direct transition is not allowed
     */
    public void markAsUnwatched() {
        throw new IllegalStateException("Cannot mark as unwatched directly. Use markAsUnwatchedIfNoRecords instead.");
    }

    /**
     * Marks this episode as unwatched if there are no viewing records.
     * This is the only valid way to transition from WATCHED back to UNWATCHED.
     *
     * @throws IllegalStateException if viewing records exist
     */
    public void markAsUnwatchedIfNoRecords() {
        if (!this.viewingRecords.isEmpty()) {
            throw new IllegalStateException("Cannot mark as unwatched when viewing records exist");
        }
        this.watchStatus = WatchStatus.UNWATCHED;
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Adds a watch page URL.
     *
     * @param url Watch page URL to add
     */
    public void addWatchPageUrl(WatchPageUrl url) {
        Objects.requireNonNull(url, "WatchPageUrl must not be null");
        if (!this.watchPageUrls.contains(url)) {
            this.watchPageUrls.add(url);
        }
    }

    /**
     * Removes a watch page URL.
     *
     * @param url Watch page URL to remove
     */
    public void removeWatchPageUrl(WatchPageUrl url) {
        this.watchPageUrls.remove(url);
    }

    /**
     * Adds a viewing record.
     *
     * @param record Viewing record to add
     */
    public void addViewingRecord(ViewingRecord record) {
        Objects.requireNonNull(record, "ViewingRecord must not be null");
        if (!this.viewingRecords.contains(record)) {
            this.viewingRecords.add(record);
        }
    }

    /**
     * Removes a viewing record.
     *
     * @param record Viewing record to remove
     */
    public void removeViewingRecord(ViewingRecord record) {
        this.viewingRecords.remove(record);
    }

    /**
     * Updates the episode info.
     *
     * @param episodeInfo New episode info
     */
    public void updateEpisodeInfo(String episodeInfo) {
        validateEpisodeInfo(episodeInfo);
        this.episodeInfo = episodeInfo != null ? episodeInfo : "";
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Updates the updatedAt timestamp.
     */
    public void touch() {
        this.updatedAt = ZonedDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getEpisodeInfo() {
        return episodeInfo;
    }

    public List<WatchPageUrl> getWatchPageUrls() {
        return new ArrayList<>(watchPageUrls);
    }

    public WatchStatus getWatchStatus() {
        return watchStatus;
    }

    public List<ViewingRecord> getViewingRecords() {
        return new ArrayList<>(viewingRecords);
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(id, episode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", seriesId=" + seriesId +
                ", episodeInfo='" + episodeInfo + '\'' +
                ", watchPageUrls=" + watchPageUrls.size() +
                ", watchStatus=" + watchStatus +
                ", viewingRecords=" + viewingRecords.size() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
