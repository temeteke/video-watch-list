package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Series エンティティ")
class SeriesTest {

    @Nested
    @DisplayName("作成")
    class Create {

        @Test
        @DisplayName("タイトルIDとシーズン名を指定してシリーズを作成できる")
        void shouldCreateSeriesWithName() {
            // Given
            Long titleId = 1L;
            String seriesName = "Season 1";

            // When
            Series series = Series.create(titleId, seriesName);

            // Then
            assertThat(series).isNotNull();
            assertThat(series.getTitleId()).isEqualTo(titleId);
            assertThat(series.getName()).isEqualTo(seriesName);
            assertThat(series.getCreatedAt()).isNotNull();
            assertThat(series.getUpdatedAt()).isNotNull();
            assertThat(series.getEpisodes()).isEmpty();
        }

        @Test
        @DisplayName("デフォルトシリーズ（空のシーズン名）を作成できる")
        void shouldCreateDefaultSeries() {
            // Given
            Long titleId = 1L;

            // When
            Series series = Series.createDefault(titleId);

            // Then
            assertThat(series.getTitleId()).isEqualTo(titleId);
            assertThat(series.getName()).isEmpty();
            assertThat(series.getEpisodes()).isEmpty();
        }

        @Test
        @DisplayName("新規シリーズ作成時は空のエピソードリストを持つ")
        void shouldCreateDefaultEpisodeAutomatically() {
            // Given
            Long titleId = 1L;

            // When
            Series series = Series.create(titleId, "Season 1");

            // Then
            assertThat(series.getEpisodes()).isEmpty();
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("100文字を超えるシーズン名では作成できない")
        void shouldNotCreateWithTooLongName() {
            String longName = "a".repeat(101);
            assertThatThrownBy(() -> Series.create(1L, longName))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("100文字のシーズン名で作成できる")
        void shouldCreateWithExactly100Characters() {
            String name = "a".repeat(100);
            Series series = Series.create(1L, name);
            assertThat(series.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("null のシーズン名ではなく空文字列に変換される")
        void shouldConvertNullNameToEmpty() {
            Series series = Series.create(1L, null);
            assertThat(series.getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("更新")
    class Update {

        @Test
        @DisplayName("シーズン名を変更できる")
        void shouldUpdateName() {
            // Given
            Series series = Series.create(1L, "Season 1");
            LocalDateTime originalUpdatedAt = series.getUpdatedAt();

            // When
            series.updateName("Season 1 (Complete)");

            // Then
            assertThat(series.getName()).isEqualTo("Season 1 (Complete)");
            assertThat(series.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("エピソード管理")
    class EpisodeManagement {

        @Test
        @DisplayName("エピソードを追加できる")
        void shouldAddEpisode() {
            // Given
            Long seriesId = 1L;
            Series series = new Series(seriesId, 1L, "Season 1", null, LocalDateTime.now(), LocalDateTime.now());
            int initialSize = series.getEpisodes().size();

            // When
            Episode episode = Episode.create(seriesId, "第2話");
            series.addEpisode(episode);

            // Then
            assertThat(series.getEpisodes()).hasSize(initialSize + 1);
        }
    }

    @Nested
    @DisplayName("エンティティの整合性")
    class Consistency {

        @Test
        @DisplayName("シリーズにエピソードを追加できる")
        void episodesShouldAlwaysHaveAtLeastOne() {
            // Given
            Long seriesId = 1L;
            Series series = new Series(seriesId, 1L, "Season 1", null, LocalDateTime.now(), LocalDateTime.now());

            // When
            Episode episode = Episode.create(seriesId, "第1話");
            series.addEpisode(episode);

            // Then
            assertThat(series.getEpisodes()).isNotEmpty();
        }
    }
}
