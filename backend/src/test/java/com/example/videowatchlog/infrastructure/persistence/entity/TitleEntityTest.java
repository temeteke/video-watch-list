package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.TitleInfoUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for TitleEntity (persistence model).
 * Tests the conversion logic between domain model and persistence model.
 */
@DisplayName("TitleEntity Test")
class TitleEntityTest {

    @Test
    @DisplayName("toDomain should convert TitleEntity to Title correctly")
    void toDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 16, 12, 0);

        TitleEntity entity = new TitleEntity();
        entity.setId(1L);
        entity.setName("進撃の巨人");
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        Set<TitleInfoUrl> titleInfoUrls = new LinkedHashSet<>();
        titleInfoUrls.add(new TitleInfoUrl("https://example.com/title/1"));

        // When
        // Phase 7: toDomain(titleInfoUrls) のみ使用
        Title domain = entity.toDomain(titleInfoUrls);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("進撃の巨人");
        assertThat(domain.getTitleInfoUrls()).hasSize(1);
        // Phase 7: Series は独立した集約
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("fromDomain should convert Title to TitleEntity correctly")
    void fromDomain_shouldConvertCorrectly() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title domain = new Title(2L, "鬼滅の刃", new LinkedHashSet<>(),
                                now, now);

        // When
        TitleEntity entity = TitleEntity.fromDomain(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getName()).isEqualTo("鬼滅の刃");
    }

    @Test
    @DisplayName("fromDomain and toDomain should be reversible (round-trip)")
    void fromDomain_toDomain_roundTrip() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Title original = new Title(3L, "呪術廻戦", new LinkedHashSet<>(),
                                  now, now);

        // When: Convert domain -> entity -> domain
        TitleEntity entity = TitleEntity.fromDomain(original);
        // Phase 7: toDomain シグネチャが変更されました
        Title converted = entity.toDomain(new LinkedHashSet<>());

        // Then: All properties should be preserved
        assertThat(converted.getId()).isEqualTo(original.getId());
        assertThat(converted.getName()).isEqualTo(original.getName());
    }

    @Test
    @DisplayName("toDomain should handle empty collections correctly")
    void toDomain_shouldHandleEmptyCollections() {
        // Given
        TitleEntity entity = new TitleEntity();
        entity.setId(1L);
        entity.setName("Test Title");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        // Phase 7: toDomain シグネチャが変更
        Title domain = entity.toDomain(new LinkedHashSet<>());

        // Then
        assertThat(domain.getTitleInfoUrls()).isEmpty();
        // Phase 7: Series は独立した集約
    }

    @Test
    @DisplayName("default constructor should initialize entity")
    void defaultConstructor_shouldInitialize() {
        // When
        TitleEntity entity = new TitleEntity();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
    }
}
