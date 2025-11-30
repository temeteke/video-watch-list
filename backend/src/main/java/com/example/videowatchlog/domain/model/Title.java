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
    private Long id;
    private String name;
    private Set<TitleInfoUrl> titleInfoUrls;
    private List<Series> series;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * プライベートコンストラクタ（DDD: 集約ルートの完全性を保証するため）
     */
    private Title(String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.titleInfoUrls = new LinkedHashSet<>();
        this.series = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        // Note: デフォルトシリーズはリポジトリ層で保存時に作成されます
    }

    /**
     * MyBatis用デフォルトコンストラクタ（パッケージプライベート）
     * リフレクションによるインスタンス化のために使用されます
     */
    Title() {
        this.titleInfoUrls = new LinkedHashSet<>();
        this.series = new ArrayList<>();
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

    // Setters (for persistence)
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitleInfoUrls(Set<TitleInfoUrl> titleInfoUrls) {
        this.titleInfoUrls = titleInfoUrls != null ? titleInfoUrls : new LinkedHashSet<>();
    }

    public void setSeries(List<Series> series) {
        this.series = series != null ? series : new ArrayList<>();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
