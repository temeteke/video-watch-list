package com.example.videowatchlog.domain.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Series entity.
 * Represents a season within a Title (e.g., Season 1, Season 2).
 *
 * Key business rules:
 * - Series name can be empty (for default series)
 * - Series name must not exceed 100 characters
 * - Series must have at least one Episode internally (enforced at use case layer)
 * - Deleting a series cascades to all Episodes and ViewingRecords
 */
public class Series {
    private final Long id;
    private final Long titleId;
    private String name;
    private final List<Episode> episodes;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    /**
     * Constructor for Series entity.
     *
     * @param id Unique identifier (null for new entities)
     * @param titleId Parent title ID (required)
     * @param name Series name (can be empty, max 100 characters)
     * @param episodes List of episodes (internally must have at least 1)
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     * @throws IllegalArgumentException if validation fails
     */
    public Series(Long id, Long titleId, String name, List<Episode> episodes,
                  ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.titleId = Objects.requireNonNull(titleId, "titleId must not be null");
        validateName(name);
        this.name = name != null ? name : "";
        this.episodes = episodes != null ? new ArrayList<>(episodes) : new ArrayList<>();
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    /**
     * Validates series name according to business rules.
     *
     * @param name Series name to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateName(String name) {
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("Series name must not exceed 100 characters");
        }
    }

    /**
     * Adds an episode to this series.
     *
     * @param episode Episode to add
     */
    public void addEpisode(Episode episode) {
        Objects.requireNonNull(episode, "Episode must not be null");
        if (!this.episodes.contains(episode)) {
            this.episodes.add(episode);
        }
    }

    /**
     * Removes an episode from this series.
     *
     * @param episode Episode to remove
     */
    public void removeEpisode(Episode episode) {
        this.episodes.remove(episode);
    }

    /**
     * Updates the series name.
     *
     * @param name New series name
     */
    public void updateName(String name) {
        validateName(name);
        this.name = name != null ? name : "";
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

    public Long getTitleId() {
        return titleId;
    }

    public String getName() {
        return name;
    }

    public List<Episode> getEpisodes() {
        return new ArrayList<>(episodes);
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
        Series series = (Series) o;
        return Objects.equals(id, series.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", titleId=" + titleId +
                ", name='" + name + '\'' +
                ", episodes=" + episodes.size() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
