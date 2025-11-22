package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration test for TitleRepositoryImpl.
 * Tests the critical business logic of creating default Series and Episode when saving a new Title.
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("TitleRepositoryImpl Integration Test")
class TitleRepositoryImplTest {

    @Autowired
    private TitleMapper titleMapper;

    @Autowired
    private SeriesMapper seriesMapper;

    @Autowired
    private EpisodeMapper episodeMapper;

    @Test
    @DisplayName("新しいタイトルを保存すると、デフォルトシリーズとエピソードが自動作成される")
    void shouldCreateDefaultSeriesAndEpisodeWhenSavingNewTitle() {
        // Given: Create a new title
        Title title = Title.create("進撃の巨人");
        TitleRepositoryImpl repository = new TitleRepositoryImpl(titleMapper, seriesMapper, episodeMapper);

        // When: Save the title
        Title savedTitle = repository.save(title);

        // Then: Title should have ID assigned
        assertThat(savedTitle.getId()).isNotNull();
        assertThat(savedTitle.getName()).isEqualTo("進撃の巨人");

        // And: Default series should be created
        var seriesList = seriesMapper.findByTitleId(savedTitle.getId());
        assertThat(seriesList).hasSize(1);
        assertThat(seriesList.get(0).getName()).isEmpty(); // Default series has empty name
        assertThat(seriesList.get(0).getTitleId()).isEqualTo(savedTitle.getId());

        // And: Default episode should be created under the default series
        var defaultSeries = seriesList.get(0);
        var episodeList = episodeMapper.findBySeriesId(defaultSeries.getId());
        assertThat(episodeList).hasSize(1);
        assertThat(episodeList.get(0).getEpisodeInfo()).isEmpty(); // Default episode has empty info
        assertThat(episodeList.get(0).getSeriesId()).isEqualTo(defaultSeries.getId());
    }

    @Test
    @DisplayName("既存のタイトルを更新してもデフォルトシリーズは追加作成されない")
    void shouldNotCreateDefaultSeriesWhenUpdatingExistingTitle() {
        // Given: Create and save a new title first
        Title title = Title.create("鬼滅の刃");
        TitleRepositoryImpl repository = new TitleRepositoryImpl(titleMapper, seriesMapper, episodeMapper);
        Title savedTitle = repository.save(title);

        // Verify default series was created
        var seriesListBefore = seriesMapper.findByTitleId(savedTitle.getId());
        assertThat(seriesListBefore).hasSize(1);

        // When: Update the existing title
        savedTitle.updateName("鬼滅の刃 - 完結編");
        Title updatedTitle = repository.save(savedTitle);

        // Then: No additional series should be created
        var seriesListAfter = seriesMapper.findByTitleId(updatedTitle.getId());
        assertThat(seriesListAfter).hasSize(1);
        assertThat(seriesListAfter.get(0).getId()).isEqualTo(seriesListBefore.get(0).getId());
    }

    @Test
    @DisplayName("タイトルを保存後、findByIdで取得できる")
    void shouldFindTitleByIdAfterSaving() {
        // Given
        Title title = Title.create("呪術廻戦");
        TitleRepositoryImpl repository = new TitleRepositoryImpl(titleMapper, seriesMapper, episodeMapper);
        Title savedTitle = repository.save(title);

        // When
        var foundTitle = repository.findById(savedTitle.getId());

        // Then
        assertThat(foundTitle).isPresent();
        assertThat(foundTitle.get().getId()).isEqualTo(savedTitle.getId());
        assertThat(foundTitle.get().getName()).isEqualTo("呪術廻戦");
    }

    @Test
    @DisplayName("タイトルを削除すると、デフォルトシリーズとエピソードもカスケード削除される")
    void shouldCascadeDeleteSeriesAndEpisodesWhenDeletingTitle() {
        // Given: Create and save a new title
        Title title = Title.create("東京リベンジャーズ");
        TitleRepositoryImpl repository = new TitleRepositoryImpl(titleMapper, seriesMapper, episodeMapper);
        Title savedTitle = repository.save(title);

        // Verify default series and episode exist
        var seriesList = seriesMapper.findByTitleId(savedTitle.getId());
        assertThat(seriesList).hasSize(1);
        var episodeList = episodeMapper.findBySeriesId(seriesList.get(0).getId());
        assertThat(episodeList).hasSize(1);

        // When: Delete the title
        repository.delete(savedTitle.getId());

        // Then: Title should be deleted
        var foundTitle = repository.findById(savedTitle.getId());
        assertThat(foundTitle).isEmpty();

        // And: Series and episodes should also be deleted (cascade)
        var seriesListAfter = seriesMapper.findByTitleId(savedTitle.getId());
        assertThat(seriesListAfter).isEmpty();
    }
}
