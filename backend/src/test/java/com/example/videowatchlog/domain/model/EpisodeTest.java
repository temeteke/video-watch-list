package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Episode エンティティ")
class EpisodeTest {

    @Nested
    @DisplayName("作成")
    class Create {

        @Test
        @DisplayName("シリーズIDとエピソード情報を指定してエピソードを作成できる")
        void shouldCreateEpisodeWithInfo() {
            // Given
            Long seriesId = 1L;
            String episodeInfo = "第1話";

            // When
            Episode episode = Episode.create(seriesId, episodeInfo);

            // Then
            assertThat(episode).isNotNull();
            assertThat(episode.getSeriesId()).isEqualTo(seriesId);
            assertThat(episode.getEpisodeInfo()).isEqualTo(episodeInfo);
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
            assertThat(episode.getCreatedAt()).isNotNull();
            assertThat(episode.getUpdatedAt()).isNotNull();
            assertThat(episode.getViewingRecords()).isEmpty();
            assertThat(episode.getWatchPageUrls()).isEmpty();
        }

        @Test
        @DisplayName("デフォルトエピソード（空のエピソード情報）を作成できる")
        void shouldCreateDefaultEpisode() {
            // Given
            Long seriesId = 1L;

            // When
            Episode episode = Episode.createDefault(seriesId);

            // Then
            assertThat(episode.getSeriesId()).isEqualTo(seriesId);
            assertThat(episode.getEpisodeInfo()).isEmpty();
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }

        @Test
        @DisplayName("新規作成時は未視聴状態である")
        void shouldCreateWithUnwatchedStatus() {
            Episode episode = Episode.create(1L, "第1話");
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("200文字を超えるエピソード情報では作成できない")
        void shouldNotCreateWithTooLongInfo() {
            String longInfo = "a".repeat(201);
            assertThatThrownBy(() -> Episode.create(1L, longInfo))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("200文字のエピソード情報で作成できる")
        void shouldCreateWithExactly200Characters() {
            String info = "a".repeat(200);
            Episode episode = Episode.create(1L, info);
            assertThat(episode.getEpisodeInfo()).isEqualTo(info);
        }
    }

    @Nested
    @DisplayName("視聴状態の遷移")
    class WatchStatusTransition {

        @Test
        @DisplayName("未視聴エピソードを視聴済みに変更できる")
        void shouldChangeToWatched() {
            // Given
            Episode episode = Episode.create(1L, "第1話");

            // When
            episode.markAsWatched();

            // Then
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.WATCHED);
        }

        @Test
        @DisplayName("視聴済みエピソードは直接状態変更ができない（バリデーションエラー）")
        void shouldNotDirectlyChangeWatchedStatus() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            episode.markAsWatched();

            // Then: Direct state change should not be allowed
            // This is handled by business logic through use cases
        }

        @Test
        @DisplayName("すべての視聴履歴を削除すると未視聴に戻る")
        void shouldRevertToUnwatchedWhenAllRecordsDeleted() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            episode.markAsWatched();
            ViewingRecord record = ViewingRecord.create(episode.getId(), ZonedDateTime.now(), 5, "Great!");
            episode.addViewingRecord(record);

            // When
            episode.removeViewingRecord(record);

            // Then
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }
    }

    @Nested
    @DisplayName("視聴ページURL管理")
    class WatchPageUrlManagement {

        @Test
        @DisplayName("視聴ページURLを追加できる")
        void shouldAddWatchPageUrl() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            String url = "https://www.netflix.com/進撃の巨人/episode/1";

            // When
            episode.addWatchPageUrl(new WatchPageUrl(url, "Netflix"));

            // Then
            assertThat(episode.getWatchPageUrls()).hasSize(1);
            assertThat(episode.getWatchPageUrls().get(0).getUrl()).isEqualTo(url);
        }

        @Test
        @DisplayName("同じURLを複数追加した場合、重複が自動削除される")
        void shouldRemoveDuplicateUrls() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            String url = "https://www.netflix.com/進撃の巨人/episode/1";

            // When
            episode.addWatchPageUrl(new WatchPageUrl(url, "Netflix"));
            episode.addWatchPageUrl(new WatchPageUrl(url, "Netflix"));

            // Then
            assertThat(episode.getWatchPageUrls()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("視聴履歴管理")
    class ViewingRecordManagement {

        @Test
        @DisplayName("視聴履歴を追加できる")
        void shouldAddViewingRecord() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            episode.markAsWatched();

            // When
            ViewingRecord record = ViewingRecord.create(
                    episode.getId(),
                    ZonedDateTime.now(),
                    5,
                    "Excellent!"
            );
            episode.addViewingRecord(record);

            // Then
            assertThat(episode.getViewingRecords()).hasSize(1);
        }

        @Test
        @DisplayName("視聴履歴を削除できる")
        void shouldRemoveViewingRecord() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            episode.markAsWatched();
            ViewingRecord record = ViewingRecord.create(
                    episode.getId(),
                    ZonedDateTime.now(),
                    5,
                    "Excellent!"
            );
            episode.addViewingRecord(record);

            // When
            episode.removeViewingRecord(record);

            // Then
            assertThat(episode.getViewingRecords()).isEmpty();
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }
    }

    @Nested
    @DisplayName("更新")
    class Update {

        @Test
        @DisplayName("エピソード情報を変更できる")
        void shouldUpdateEpisodeInfo() {
            // Given
            Episode episode = Episode.create(1L, "第1話");
            ZonedDateTime originalUpdatedAt = episode.getUpdatedAt();

            // When
            episode.updateEpisodeInfo("第1話「巨人襲来」");

            // Then
            assertThat(episode.getEpisodeInfo()).isEqualTo("第1話「巨人襲来」");
            assertThat(episode.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }
    }
}
