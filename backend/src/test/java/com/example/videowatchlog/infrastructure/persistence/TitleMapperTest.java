package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        // Clean up test data from previous runs
        jdbcTemplate.execute("TRUNCATE TABLE viewing_records, episodes, series, title_info_urls, titles RESTART IDENTITY CASCADE");
    }

    @Test
    @DisplayName("タイトルを保存し、IDで検索できる")
    void shouldSaveAndFindTitle() {
        // Given
        Title title = Title.create(1L, "進撃の巨人");
        TitleEntity entity = TitleEntity.fromDomain(title);

        // When
        titleMapper.insert(entity);
        Optional<TitleEntity> found = titleMapper.findById(entity.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("進撃の巨人");
    }

    @Test
    @DisplayName("すべてのタイトルを取得できる")
    void shouldFindAllTitles() {
        // Given
        Title title1 = Title.create(1L, "進撃の巨人");
        Title title2 = Title.create(2L, "鬼滅の刃");
        TitleEntity entity1 = TitleEntity.fromDomain(title1);
        TitleEntity entity2 = TitleEntity.fromDomain(title2);
        titleMapper.insert(entity1);
        titleMapper.insert(entity2);

        // When
        List<TitleEntity> entities = titleMapper.findAll();

        // Then
        assertThat(entities).hasSize(2);
    }

    @Test
    @DisplayName("タイトル名の存在確認（大文字小文字区別なし）")
    void shouldCheckExistenceByName() {
        // Given
        Title title = Title.create(1L, "進撃の巨人");
        TitleEntity entity = TitleEntity.fromDomain(title);
        titleMapper.insert(entity);

        // When
        boolean exists = titleMapper.existsByName("進撃の巨人");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("タイトルを削除できる")
    void shouldDeleteTitle() {
        // Given
        Title title = Title.create(1L, "進撃の巨人");
        TitleEntity entity = TitleEntity.fromDomain(title);
        titleMapper.insert(entity);

        // When
        titleMapper.delete(entity.getId());
        Optional<TitleEntity> found = titleMapper.findById(entity.getId());

        // Then
        assertThat(found).isEmpty();
    }
}
