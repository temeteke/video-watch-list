package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

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
            Long id = 1L;
            Long seriesId = 1L;
            String episodeInfo = "第1話";

            // When
            Episode episode = Episode.create(id, seriesId, episodeInfo);

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
            Long id = 1L;
            Long seriesId = 1L;

            // When
            Episode episode = Episode.createDefault(id, seriesId);

            // Then
            assertThat(episode.getSeriesId()).isEqualTo(seriesId);
            assertThat(episode.getEpisodeInfo()).isEmpty();
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }

        @Test
        @DisplayName("新規作成時は未視聴状態である")
        void shouldCreateWithUnwatchedStatus() {
            Episode episode = Episode.create(1L, 1L, "第1話");
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
            assertThatThrownBy(() -> Episode.create(1L, 1L, longInfo))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("200文字のエピソード情報で作成できる")
        void shouldCreateWithExactly200Characters() {
            String info = "a".repeat(200);
            Episode episode = Episode.create(1L, 1L, info);
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
            Episode episode = Episode.create(1L, 1L, "第1話");

            // When
            episode.markAsWatched();

            // Then
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.WATCHED);
        }

        @Test
        @DisplayName("視聴済みエピソードは直接状態変更ができない（IllegalStateException）")
        void shouldNotDirectlyChangeWatchedStatus() {
            // Given
            Episode episode = Episode.create(1L, 1L, "第1話");
            episode.markAsWatched();

            // When & Then
            assertThatThrownBy(() -> episode.markAsUnwatched())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot mark as unwatched directly");
        }

        @Test
        @DisplayName("すべての視聴履歴を削除すると未視聴に戻る")
        void shouldRevertToUnwatchedWhenAllRecordsDeleted() {
            // Given
            Long episodeId = 1L;
            Episode episode = new Episode(episodeId, 1L, "第1話", null, WatchStatus.UNWATCHED, null, LocalDateTime.now(), LocalDateTime.now());
            episode.markAsWatched();
            ViewingRecord record = ViewingRecord.create(1L, episode.getId(), LocalDateTime.now(), 5, "Great!");
            episode.addViewingRecord(record);

            // When
            episode.removeViewingRecord(record);

            // Then
            assertThat(episode.getWatchStatus()).isEqualTo(WatchStatus.UNWATCHED);
        }

        @Test
        @DisplayName("視聴履歴がある場合、markAsUnwatchedIfNoRecords()は例外を投げる")
        void shouldThrowExceptionWhenMarkingUnwatchedWithRecords() {
            // Given
            Long episodeId = 1L;
            Episode episode = new Episode(episodeId, 1L, "第1話", null, WatchStatus.UNWATCHED, null, LocalDateTime.now(), LocalDateTime.now());
            episode.markAsWatched();
            ViewingRecord record = ViewingRecord.create(1L, episode.getId(), LocalDateTime.now(), 5, "Great!");
            episode.addViewingRecord(record);

            // When & Then
            assertThatThrownBy(() -> episode.markAsUnwatchedIfNoRecords())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot mark as unwatched when viewing records exist");
        }

        @Test
        @DisplayName("視聴履歴がない場合、markAsUnwatchedIfNoRecords()で未視聴に戻る")
        void shouldMarkAsUnwatchedWhenNoRecords() {
            // Given
            Long episodeId = 1L;
            Episode episode = new Episode(episodeId, 1L, "第1話", null, WatchStatus.WATCHED, null, LocalDateTime.now(), LocalDateTime.now());

            // When
            episode.markAsUnwatchedIfNoRecords();

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
            Episode episode = Episode.create(1L, 1L, "第1話");
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
            Episode episode = Episode.create(1L, 1L, "第1話");
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
            Long episodeId = 1L;
            Episode episode = new Episode(episodeId, 1L, "第1話", null, WatchStatus.UNWATCHED, null, LocalDateTime.now(), LocalDateTime.now());
            episode.markAsWatched();

            // When
            ViewingRecord record = ViewingRecord.create(
                    1L,
                    episode.getId(),
                    LocalDateTime.now(),
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
            Long episodeId = 1L;
            Episode episode = new Episode(episodeId, 1L, "第1話", null, WatchStatus.UNWATCHED, null, LocalDateTime.now(), LocalDateTime.now());
            episode.markAsWatched();
            ViewingRecord record = ViewingRecord.create(
                    1L,
                    episode.getId(),
                    LocalDateTime.now(),
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
            Episode episode = Episode.create(1L, 1L, "第1話");
            LocalDateTime originalUpdatedAt = episode.getUpdatedAt();

            // When
            episode.updateEpisodeInfo("第1話「巨人襲来」");

            // Then
            assertThat(episode.getEpisodeInfo()).isEqualTo("第1話「巨人襲来」");
            assertThat(episode.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }
    }
}
