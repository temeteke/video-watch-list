package com.example.videowatchlog.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * タイトル エンティティ（集約ルート）
 *
 * ドラマ・アニメ・映画作品全体を表します。
 * 内部的には必ず1件以上のシリーズを持ちます。
 */
public class Title {
    private final Long id;
    private String name;
    private Set<TitleInfoUrl> titleInfoUrls;
    private List<Series> series;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * プライベートコンストラクタ（DDD: 集約ルートの完全性を保証するため）
     */
    private Title(String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = null;  // 新規作成時はnull
        this.name = name;
        this.titleInfoUrls = new LinkedHashSet<>();
        this.series = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        // Note: デフォルトシリーズはリポジトリ層で保存時に作成されます
    }

    /**
     * Public constructor for creating Title instances.
     * Used by persistence layer (Entity conversion).
     *
     * @param id Unique identifier (null for new entities)
     * @param name Title name (1-200 characters)
     * @param titleInfoUrls Set of title info URLs
     * @param series List of series
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     * @throws IllegalArgumentException if validation fails
     */
    public Title(Long id, String name, Set<TitleInfoUrl> titleInfoUrls, List<Series> series,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        validateName(name);
        this.name = name;
        this.titleInfoUrls = titleInfoUrls != null ? new LinkedHashSet<>(titleInfoUrls) : new LinkedHashSet<>();
        this.series = series != null ? new ArrayList<>(series) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    /**
     * タイトルを作成します（ファクトリメソッド）
     *
     * @param name タイトル名（1～200文字）
     * @return 新規作成されたTitle
     * @throws IllegalArgumentException タイトル名が無効な場合
     */
    public static Title create(String name) {
        validateName(name);
        LocalDateTime now = LocalDateTime.now();
        return new Title(name, now, now);
    }

    /**
     * タイトル名を更新します
     *
     * @param newName 新しいタイトル名
     * @throws IllegalArgumentException 新しいタイトル名が無効な場合
     */
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * タイトル情報URLを追加します（重複は自動削除）
     *
     * @param titleInfoUrl 追加するURL
     */
    public void addTitleInfoUrl(TitleInfoUrl titleInfoUrl) {
        this.titleInfoUrls.add(titleInfoUrl);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * タイトル情報URLを削除します
     *
     * @param titleInfoUrl 削除するURL
     */
    public void removeTitleInfoUrl(TitleInfoUrl titleInfoUrl) {
        this.titleInfoUrls.remove(titleInfoUrl);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * タイトル名を検証します
     *
     * @param name 検証するタイトル名
     * @throws IllegalArgumentException 無効な場合
     */
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("タイトル名は1文字以上である必要があります");
        }
        if (name.length() > 200) {
            throw new IllegalArgumentException("タイトル名は200文字以下である必要があります");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<TitleInfoUrl> getTitleInfoUrls() {
        return titleInfoUrls;
    }

    public List<Series> getSeries() {
        return series;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
