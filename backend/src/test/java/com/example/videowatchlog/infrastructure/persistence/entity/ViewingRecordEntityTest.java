package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.ViewingRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for ViewingRecordEntity (persistence model).
 * Tests the conversion logic between domain model and persistence model.
 */
@DisplayName("ViewingRecordEntity Test")
class ViewingRecordEntityTest {

    @Test
    @DisplayName("toDomain should convert ViewingRecordEntity to ViewingRecord correctly")
    void toDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime watchedAt = LocalDateTime.of(2024, 1, 15, 20, 30);
        LocalDateTime recordedAt = LocalDateTime.of(2024, 1, 15, 21, 0);

        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(1L);
        entity.setEpisodeId(100L);
        entity.setWatchedAt(watchedAt);
        entity.setRating(5);
        entity.setComment("素晴らしいエピソードでした！");
        entity.setRecordedAt(recordedAt);

        // When
        ViewingRecord domain = entity.toDomain();

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getEpisodeId()).isEqualTo(100L);
        assertThat(domain.getWatchedAt()).isEqualTo(watchedAt);
        assertThat(domain.getRating()).isEqualTo(5);
        assertThat(domain.getComment()).isEqualTo("素晴らしいエピソードでした！");
        assertThat(domain.getRecordedAt()).isEqualTo(recordedAt);
    }

    @Test
    @DisplayName("fromDomain should convert ViewingRecord to ViewingRecordEntity correctly")
    void fromDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime watchedAt = LocalDateTime.of(2024, 1, 15, 20, 30);
        ViewingRecord domain = ViewingRecord.create(100L, watchedAt, 5, "素晴らしいエピソードでした！");

        // Simulate persisted entity with ID
        ViewingRecord persistedDomain = new ViewingRecord(1L, 100L, watchedAt, 5,
                                                         "素晴らしいエピソードでした！",
                                                         domain.getRecordedAt());

        // When
        ViewingRecordEntity entity = ViewingRecordEntity.fromDomain(persistedDomain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getEpisodeId()).isEqualTo(100L);
        assertThat(entity.getWatchedAt()).isEqualTo(watchedAt);
        assertThat(entity.getRating()).isEqualTo(5);
        assertThat(entity.getComment()).isEqualTo("素晴らしいエピソードでした！");
        assertThat(entity.getRecordedAt()).isEqualTo(persistedDomain.getRecordedAt());
    }

    @Test
    @DisplayName("fromDomain and toDomain should be reversible (round-trip)")
    void fromDomain_toDomain_roundTrip() {
        // Given
        LocalDateTime watchedAt = LocalDateTime.of(2024, 1, 15, 20, 30);
        LocalDateTime recordedAt = LocalDateTime.now();
        ViewingRecord original = new ViewingRecord(1L, 100L, watchedAt, 5, "Test comment", recordedAt);

        // When: Convert domain -> entity -> domain
        ViewingRecordEntity entity = ViewingRecordEntity.fromDomain(original);
        ViewingRecord converted = entity.toDomain();

        // Then: All properties should be preserved
        assertThat(converted.getId()).isEqualTo(original.getId());
        assertThat(converted.getEpisodeId()).isEqualTo(original.getEpisodeId());
        assertThat(converted.getWatchedAt()).isEqualTo(original.getWatchedAt());
        assertThat(converted.getRating()).isEqualTo(original.getRating());
        assertThat(converted.getComment()).isEqualTo(original.getComment());
        assertThat(converted.getRecordedAt()).isEqualTo(original.getRecordedAt());
    }

    @Test
    @DisplayName("toDomain should handle null comment correctly")
    void toDomain_shouldHandleNullComment() {
        // Given
        LocalDateTime watchedAt = LocalDateTime.of(2024, 1, 15, 20, 30);
        LocalDateTime recordedAt = LocalDateTime.now();

        ViewingRecordEntity entity = new ViewingRecordEntity();
        entity.setId(1L);
        entity.setEpisodeId(100L);
        entity.setWatchedAt(watchedAt);
        entity.setRating(3);
        entity.setComment(null);
        entity.setRecordedAt(recordedAt);

        // When
        ViewingRecord domain = entity.toDomain();

        // Then
        assertThat(domain.getComment()).isNull();
    }

    @Test
    @DisplayName("fromDomain should handle null comment correctly")
    void fromDomain_shouldHandleNullComment() {
        // Given
        LocalDateTime watchedAt = LocalDateTime.of(2024, 1, 15, 20, 30);
        LocalDateTime recordedAt = LocalDateTime.now();
        ViewingRecord domain = new ViewingRecord(1L, 100L, watchedAt, 3, null, recordedAt);

        // When
        ViewingRecordEntity entity = ViewingRecordEntity.fromDomain(domain);

        // Then
        assertThat(entity.getComment()).isNull();
    }

    @Test
    @DisplayName("default constructor should initialize entity")
    void defaultConstructor_shouldInitialize() {
        // When
        ViewingRecordEntity entity = new ViewingRecordEntity();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getEpisodeId()).isNull();
    }
}
