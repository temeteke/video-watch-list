package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.WatchStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TitleMapper 統合テスト")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class TitleMapperTest {

    @Autowired
    private TitleMapper titleMapper;

    @Test
    @DisplayName("タイトルを保存し、IDで検索できる")
    void shouldSaveAndFindTitle() {
        // Given
        Title title = Title.create("進撃の巨人");

        // When
        titleMapper.insert(title);
        Optional<Title> found = titleMapper.findById(title.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("進撃の巨人");
    }

    @Test
    @DisplayName("すべてのタイトルを取得できる")
    void shouldFindAllTitles() {
        // Given
        Title title1 = Title.create("進撃の巨人");
        Title title2 = Title.create("鬼滅の刃");
        titleMapper.insert(title1);
        titleMapper.insert(title2);

        // When
        List<Title> titles = titleMapper.findAll();

        // Then
        assertThat(titles).hasSize(2);
    }

    @Test
    @DisplayName("タイトル名の存在確認（大文字小文字区別なし）")
    void shouldCheckExistenceByName() {
        // Given
        Title title = Title.create("進撃の巨人");
        titleMapper.insert(title);

        // When
        boolean exists = titleMapper.existsByName("進撃の巨人");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("タイトルを削除できる")
    void shouldDeleteTitle() {
        // Given
        Title title = Title.create("進撃の巨人");
        titleMapper.insert(title);

        // When
        titleMapper.delete(title.getId());
        Optional<Title> found = titleMapper.findById(title.getId());

        // Then
        assertThat(found).isEmpty();
    }
}
