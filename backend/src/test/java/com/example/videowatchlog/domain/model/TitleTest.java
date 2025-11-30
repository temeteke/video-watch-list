package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Title エンティティ")
class TitleTest {

    @Nested
    @DisplayName("作成")
    class Create {

        @Test
        @DisplayName("タイトル名を指定してタイトルを作成できる")
        void shouldCreateTitleWithName() {
            // Given
            String titleName = "進撃の巨人";

            // When
            Title title = Title.create(titleName);

            // Then
            assertThat(title).isNotNull();
            assertThat(title.getName()).isEqualTo(titleName);
            assertThat(title.getCreatedAt()).isNotNull();
            assertThat(title.getUpdatedAt()).isNotNull();
            assertThat(title.getTitleInfoUrls()).isEmpty();
            assertThat(title.getSeries()).isEmpty(); // デフォルトシリーズはRepositoryで作成される
        }

        @Test
        @DisplayName("新規作成時はシリーズが空の状態")
        void shouldCreateWithoutDefaultSeries() {
            // Given
            String titleName = "鬼滅の刃";

            // When
            Title title = Title.create(titleName);

            // Then
            // デフォルトシリーズはRepository層で保存時に作成される
            assertThat(title.getSeries()).isEmpty();
        }
    }

    @Nested
    @DisplayName("バリデーション")
    class Validation {

        @Test
        @DisplayName("空のタイトル名では作成できない")
        void shouldNotCreateWithEmptyName() {
            assertThatThrownBy(() -> Title.create(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("null タイトル名では作成できない")
        void shouldNotCreateWithNullName() {
            assertThatThrownBy(() -> Title.create(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("200文字を超えるタイトル名では作成できない")
        void shouldNotCreateWithTooLongName() {
            String longName = "a".repeat(201);
            assertThatThrownBy(() -> Title.create(longName))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("200文字のタイトル名で作成できる")
        void shouldCreateWithExactly200Characters() {
            String name = "a".repeat(200);
            Title title = Title.create(name);
            assertThat(title.getName()).isEqualTo(name);
        }
    }

    @Nested
    @DisplayName("タイトル情報URL管理")
    class TitleInfoUrlManagement {

        @Test
        @DisplayName("タイトル情報URLを追加できる")
        void shouldAddTitleInfoUrl() {
            // Given
            Title title = Title.create("進撃の巨人");
            String url = "https://www.wikipedia.org/進撃の巨人";

            // When
            title.addTitleInfoUrl(new TitleInfoUrl(url));

            // Then
            assertThat(title.getTitleInfoUrls()).hasSize(1);
            assertThat(title.getTitleInfoUrls().stream().findFirst().get().getUrl()).isEqualTo(url);
        }

        @Test
        @DisplayName("同じURLを複数追加した場合、重複が自動削除される")
        void shouldRemoveDuplicateUrls() {
            // Given
            Title title = Title.create("進撃の巨人");
            String url = "https://www.wikipedia.org/進撃の巨人";

            // When
            title.addTitleInfoUrl(new TitleInfoUrl(url));
            title.addTitleInfoUrl(new TitleInfoUrl(url));

            // Then
            assertThat(title.getTitleInfoUrls()).hasSize(1);
        }

        @Test
        @DisplayName("複数のタイトル情報URLを管理できる")
        void shouldManageMultipleTitleInfoUrls() {
            // Given
            Title title = Title.create("進撃の巨人");
            String url1 = "https://www.wikipedia.org/進撃の巨人";
            String url2 = "https://www.imdb.com/title/進撃の巨人";

            // When
            title.addTitleInfoUrl(new TitleInfoUrl(url1));
            title.addTitleInfoUrl(new TitleInfoUrl(url2));

            // Then
            assertThat(title.getTitleInfoUrls()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("更新")
    class Update {

        @Test
        @DisplayName("タイトル名を変更できる")
        void shouldUpdateName() {
            // Given
            Title title = Title.create("進撃の巨人");
            LocalDateTime originalUpdatedAt = title.getUpdatedAt();

            // When
            title.updateName("進撃の巨人 (改題)");

            // Then
            assertThat(title.getName()).isEqualTo("進撃の巨人 (改題)");
            assertThat(title.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("集約ルートとしての整合性")
    class AggregateRootConsistency {

        @Test
        @DisplayName("新規作成時はシリーズが空だが、Repository保存時にデフォルトシリーズが追加される")
        void seriesShouldBeEmptyUntilSaved() {
            // Given & When
            Title title = Title.create("進撃の巨人");

            // Then
            // ドメインモデル作成時は空
            assertThat(title.getSeries()).isEmpty();
            // 注: デフォルトシリーズはTitleRepository.save()で作成される
        }
    }
}
