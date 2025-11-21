package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ViewingRecord エンティティ")
class ViewingRecordTest {

    @Nested
    @DisplayName("作成")
    class Create {

        @Test
        @DisplayName("視聴履歴を作成できる")
        void shouldCreateViewingRecord() {
            // Given
            Long episodeId = 1L;
            ZonedDateTime watchedAt = ZonedDateTime.now().minusDays(1);
            int rating = 5;
            String comment = "Excellent episode!";

            // When
            ViewingRecord record = ViewingRecord.create(episodeId, watchedAt, rating, comment);

            // Then
            assertThat(record).isNotNull();
            assertThat(record.getEpisodeId()).isEqualTo(episodeId);
            assertThat(record.getWatchedAt()).isEqualTo(watchedAt);
            assertThat(record.getRating()).isEqualTo(rating);
            assertThat(record.getComment()).isEqualTo(comment);
            assertThat(record.getRecordedAt()).isNotNull();
        }

        @Test
        @DisplayName("コメントなしで視聴履歴を作成できる")
        void shouldCreateWithoutComment() {
            // Given
            Long episodeId = 1L;
            ZonedDateTime watchedAt = ZonedDateTime.now();
            int rating = 4;

            // When
            ViewingRecord record = ViewingRecord.create(episodeId, watchedAt, rating, null);

            // Then
            assertThat(record.getComment()).isEmpty();
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("評価が1未満では作成できない")
        void shouldNotCreateWithRatingLessThan1() {
            assertThatThrownBy(() -> ViewingRecord.create(
                    1L,
                    ZonedDateTime.now(),
                    0,
                    "Bad"
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("評価が5を超えると作成できない")
        void shouldNotCreateWithRatingGreaterThan5() {
            assertThatThrownBy(() -> ViewingRecord.create(
                    1L,
                    ZonedDateTime.now(),
                    6,
                    "Great"
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("評価が1から5であれば作成できる")
        void shouldCreateWithValidRating() {
            for (int rating = 1; rating <= 5; rating++) {
                ViewingRecord record = ViewingRecord.create(
                        1L,
                        ZonedDateTime.now(),
                        rating,
                        "Test"
                );
                assertThat(record.getRating()).isEqualTo(rating);
            }
        }

        @Test
        @DisplayName("2000文字を超えるコメントでは作成できない")
        void shouldNotCreateWithTooLongComment() {
            String longComment = "a".repeat(2001);
            assertThatThrownBy(() -> ViewingRecord.create(
                    1L,
                    ZonedDateTime.now(),
                    5,
                    longComment
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("2000文字のコメントで作成できる")
        void shouldCreateWithExactly2000Characters() {
            String comment = "a".repeat(2000);
            ViewingRecord record = ViewingRecord.create(
                    1L,
                    ZonedDateTime.now(),
                    5,
                    comment
            );
            assertThat(record.getComment()).isEqualTo(comment);
        }

        @Test
        @DisplayName("未来の日付では作成できない")
        void shouldNotCreateWithFutureDate() {
            assertThatThrownBy(() -> ViewingRecord.create(
                    1L,
                    ZonedDateTime.now().plusDays(1),
                    5,
                    "Future"
            )).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("不変性")
    class Immutability {

        @Test
        @DisplayName("視聴履歴は変更不可（作成後は変更できない）")
        void shouldBeImmutable() {
            // Given
            ViewingRecord record = ViewingRecord.create(
                    1L,
                    ZonedDateTime.now().minusDays(1),
                    5,
                    "Great"
            );

            // Then: 視聴履歴は読み取り専用フィールドのみ提供
            assertThat(record.getId()).isNull();  // 作成直後はID未設定
            assertThat(record.getRating()).isEqualTo(5);
            assertThat(record.getComment()).isEqualTo("Great");
        }
    }
}
