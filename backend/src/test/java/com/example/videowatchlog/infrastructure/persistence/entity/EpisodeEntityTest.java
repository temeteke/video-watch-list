package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchPageUrl;
import com.example.videowatchlog.domain.model.WatchStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for EpisodeEntity (persistence model).
 * Tests the conversion logic between domain model and persistence model.
 */
@DisplayName("EpisodeEntity Test")
class EpisodeEntityTest {

    @Test
    @DisplayName("toDomain should convert EpisodeEntity to Episode correctly")
    void toDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 16, 12, 0);

        EpisodeEntity entity = new EpisodeEntity();
        entity.setId(1L);
        entity.setSeriesId(100L);
        entity.setEpisodeInfo("第1話");
        entity.setWatchStatus(WatchStatus.WATCHED);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        List<WatchPageUrl> watchPageUrls = List.of(
            new WatchPageUrl("https://example.com/watch/1", "Example")
        );
        List<ViewingRecord> viewingRecords = List.of(
            ViewingRecord.create(1L, 1L, LocalDateTime.of(2024, 1, 15, 20, 0), 5, "Great!")
        );

        // When
        Episode domain = entity.toDomain(watchPageUrls, viewingRecords);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getSeriesId()).isEqualTo(100L);
        assertThat(domain.getEpisodeInfo()).isEqualTo("第1話");
        assertThat(domain.getWatchStatus()).isEqualTo(WatchStatus.WATCHED);
        assertThat(domain.getWatchPageUrls()).hasSize(1);
        assertThat(domain.getViewingRecords()).hasSize(1);
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("fromDomain should convert Episode to EpisodeEntity correctly")
    void fromDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Episode domain = new Episode(
            1L,
            100L,
            "第1話",
            new ArrayList<>(),
            WatchStatus.WATCHED,
            new ArrayList<>(),
            now,
            now
        );

        // When
        EpisodeEntity entity = EpisodeEntity.fromDomain(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getSeriesId()).isEqualTo(100L);
        assertThat(entity.getEpisodeInfo()).isEqualTo("第1話");
        assertThat(entity.getWatchStatus()).isEqualTo(WatchStatus.WATCHED);
    }

    @Test
    @DisplayName("fromDomain and toDomain should be reversible (round-trip)")
    void fromDomain_toDomain_roundTrip() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Episode original = new Episode(
            2L,
            100L,
            "第2話",
            new ArrayList<>(),
            WatchStatus.UNWATCHED,
            new ArrayList<>(),
            now,
            now
        );

        // When: Convert domain -> entity -> domain
        EpisodeEntity entity = EpisodeEntity.fromDomain(original);
        Episode converted = entity.toDomain(new ArrayList<>(), new ArrayList<>());

        // Then: All properties should be preserved
        assertThat(converted.getId()).isEqualTo(original.getId());
        assertThat(converted.getSeriesId()).isEqualTo(original.getSeriesId());
        assertThat(converted.getEpisodeInfo()).isEqualTo(original.getEpisodeInfo());
        assertThat(converted.getWatchStatus()).isEqualTo(original.getWatchStatus());
    }

    @Test
    @DisplayName("toDomain should handle empty episode info correctly")
    void toDomain_shouldHandleEmptyEpisodeInfo() {
        // Given
        EpisodeEntity entity = new EpisodeEntity();
        entity.setId(1L);
        entity.setSeriesId(100L);
        entity.setEpisodeInfo("");
        entity.setWatchStatus(WatchStatus.UNWATCHED);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        Episode domain = entity.toDomain(new ArrayList<>(), new ArrayList<>());

        // Then
        assertThat(domain.getEpisodeInfo()).isEmpty();
    }

    @Test
    @DisplayName("toDomain should handle empty collections correctly")
    void toDomain_shouldHandleEmptyCollections() {
        // Given
        EpisodeEntity entity = new EpisodeEntity();
        entity.setId(1L);
        entity.setSeriesId(100L);
        entity.setEpisodeInfo("第3話");
        entity.setWatchStatus(WatchStatus.UNWATCHED);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        Episode domain = entity.toDomain(new ArrayList<>(), new ArrayList<>());

        // Then
        assertThat(domain.getWatchPageUrls()).isEmpty();
        assertThat(domain.getViewingRecords()).isEmpty();
    }

    @Test
    @DisplayName("default constructor should initialize entity")
    void defaultConstructor_shouldInitialize() {
        // When
        EpisodeEntity entity = new EpisodeEntity();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getSeriesId()).isNull();
    }
}
