package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for SeriesEntity (persistence model).
 * Tests the conversion logic between domain model and persistence model.
 */
@DisplayName("SeriesEntity Test")
class SeriesEntityTest {

    @Test
    @DisplayName("toDomain should convert SeriesEntity to Series correctly")
    void toDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 16, 12, 0);

        SeriesEntity entity = new SeriesEntity();
        entity.setId(1L);
        entity.setTitleId(100L);
        entity.setName("Season 1");
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        List<Episode> episodes = List.of(
            Episode.create(1L, "Episode 1")
        );

        // When
        Series domain = entity.toDomain(episodes);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getTitleId()).isEqualTo(100L);
        assertThat(domain.getName()).isEqualTo("Season 1");
        assertThat(domain.getEpisodes()).hasSize(1);
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("fromDomain should convert Series to SeriesEntity correctly")
    void fromDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Series domain = new Series(
            1L,
            100L,
            "Season 1",
            new ArrayList<>(),
            now,
            now
        );

        // When
        SeriesEntity entity = SeriesEntity.fromDomain(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTitleId()).isEqualTo(100L);
        assertThat(entity.getName()).isEqualTo("Season 1");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("fromDomain and toDomain should be reversible (round-trip)")
    void fromDomain_toDomain_roundTrip() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Series original = new Series(
            2L,
            100L,
            "Season 2",
            new ArrayList<>(),
            now,
            now
        );

        // When: Convert domain -> entity -> domain
        SeriesEntity entity = SeriesEntity.fromDomain(original);
        Series converted = entity.toDomain(new ArrayList<>());

        // Then: All properties should be preserved
        assertThat(converted.getId()).isEqualTo(original.getId());
        assertThat(converted.getTitleId()).isEqualTo(original.getTitleId());
        assertThat(converted.getName()).isEqualTo(original.getName());
        assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }

    @Test
    @DisplayName("toDomain should handle empty series name correctly")
    void toDomain_shouldHandleEmptySeriesName() {
        // Given
        SeriesEntity entity = new SeriesEntity();
        entity.setId(1L);
        entity.setTitleId(100L);
        entity.setName("");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        Series domain = entity.toDomain(new ArrayList<>());

        // Then
        assertThat(domain.getName()).isEmpty();
    }

    @Test
    @DisplayName("toDomain should handle empty episodes list correctly")
    void toDomain_shouldHandleEmptyEpisodes() {
        // Given
        SeriesEntity entity = new SeriesEntity();
        entity.setId(1L);
        entity.setTitleId(100L);
        entity.setName("Season 3");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        Series domain = entity.toDomain(new ArrayList<>());

        // Then
        assertThat(domain.getEpisodes()).isEmpty();
    }

    @Test
    @DisplayName("default constructor should initialize entity")
    void defaultConstructor_shouldInitialize() {
        // When
        SeriesEntity entity = new SeriesEntity();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitleId()).isNull();
    }
}
