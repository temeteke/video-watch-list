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
            Long id = 1L;
            Long titleId = 1L;
            String seriesName = "Season 1";

            // When
            Series series = Series.create(id, titleId, seriesName);

            // Then
            assertThat(series).isNotNull();
            assertThat(series.getTitleId()).isEqualTo(titleId);
            assertThat(series.getName()).isEqualTo(seriesName);
            assertThat(series.getCreatedAt()).isNotNull();
            assertThat(series.getUpdatedAt()).isNotNull();
            // Phase 7: getEpisodes() は削除されました（Episode は独立した集約）
        }

        @Test
        @DisplayName("デフォルトシリーズ（空のシーズン名）を作成できる")
        void shouldCreateDefaultSeries() {
            // Given
            Long id = 1L;
            Long titleId = 1L;

            // When
            Series series = Series.createDefault(id, titleId);

            // Then
            assertThat(series.getTitleId()).isEqualTo(titleId);
            assertThat(series.getName()).isEmpty();
            // Phase 7: getEpisodes() は削除されました
        }

        @Test
        @DisplayName("新規シリーズ作成時は空のエピソードリストを持つ")
        void shouldCreateDefaultEpisodeAutomatically() {
            // Given
            Long id = 1L;
            Long titleId = 1L;

            // When
            Series series = Series.create(id, titleId, "Season 1");

            // Then
            // Phase 7: getEpisodes() は削除されました
            assertThat(series.getId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("100文字を超えるシーズン名では作成できない")
        void shouldNotCreateWithTooLongName() {
            String longName = "a".repeat(101);
            assertThatThrownBy(() -> Series.create(1L, 1L, longName))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("100文字のシーズン名で作成できる")
        void shouldCreateWithExactly100Characters() {
            String name = "a".repeat(100);
            Series series = Series.create(1L, 1L, name);
            assertThat(series.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("null のシーズン名ではなく空文字列に変換される")
        void shouldConvertNullNameToEmpty() {
            Series series = Series.create(1L, 1L, null);
            assertThat(series.getName()).isEmpty();
        }

        @Test
        @DisplayName("titleIdがnullでは作成できない")
        void shouldNotCreateWithNullTitleId() {
            assertThatThrownBy(() -> new Series(
                    1L, null, "Season 1",
                    LocalDateTime.now(), LocalDateTime.now()
            )).isInstanceOf(NullPointerException.class)
              .hasMessageContaining("titleId must not be null");
        }

        @Test
        @DisplayName("createdAtがnullでは作成できない")
        void shouldNotCreateWithNullCreatedAt() {
            assertThatThrownBy(() -> new Series(
                    1L, 1L, "Season 1",
                    null, LocalDateTime.now()
            )).isInstanceOf(NullPointerException.class)
              .hasMessageContaining("createdAt must not be null");
        }

        @Test
        @DisplayName("updatedAtがnullでは作成できない")
        void shouldNotCreateWithNullUpdatedAt() {
            assertThatThrownBy(() -> new Series(
                    1L, 1L, "Season 1",
                    LocalDateTime.now(), null
            )).isInstanceOf(NullPointerException.class)
              .hasMessageContaining("updatedAt must not be null");
        }
    }

    @Nested
    @DisplayName("更新")
    class Update {

        @Test
        @DisplayName("シーズン名を変更できる")
        void shouldUpdateName() {
            // Given
            Series series = Series.create(1L, 1L, "Season 1");
            LocalDateTime originalUpdatedAt = series.getUpdatedAt();

            // When
            series.updateName("Season 1 (Complete)");

            // Then
            assertThat(series.getName()).isEqualTo("Season 1 (Complete)");
            assertThat(series.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }

        @Test
        @DisplayName("touch()でupdatedAtが更新される")
        void shouldUpdateUpdatedAtWhenTouched() {
            // Given
            Series series = Series.create(1L, 1L, "Season 1");
            LocalDateTime originalUpdatedAt = series.getUpdatedAt();

            // When
            series.touch();

            // Then
            assertThat(series.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("エピソード管理")
    class EpisodeManagement {

        @Test
        @DisplayName("Phase 7: Episode は独立した集約に昇格")
        void episodesAreManagedIndependently() {
            // Given
            Long seriesId = 1L;
            Series series = new Series(seriesId, 1L, "Season 1", LocalDateTime.now(), LocalDateTime.now());

            // Then
            // Phase 7: Series は Episode リストを保持しなくなりました
            assertThat(series.getTitleId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("エンティティの整合性")
    class Consistency {

        @Test
        @DisplayName("Phase 7: Series は独立した集約")
        void seriesIsNowAnIndependentAggregate() {
            // Given
            Long seriesId = 1L;
            Series series = new Series(seriesId, 1L, "Season 1", LocalDateTime.now(), LocalDateTime.now());

            // Then
            // Phase 7: Series は Episode リストを保持しなくなりました
            assertThat(series.getId()).isEqualTo(seriesId);
        }
    }
}
