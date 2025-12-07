# Data Model: 視聴予定リスト

**Feature**: 001-watch-list
**Date**: 2025-11-20
**Purpose**: ドメインエンティティ、値オブジェクト、リレーション、バリデーションルールを定義

---

## ユビキタス言語（日本語 ↔ 英語）

| 日本語 | 英語（コード） | 説明 |
|--------|----------------|------|
| タイトル | Title | ドラマ・アニメ・映画の作品全体 |
| シリーズ | Series | 1つのタイトル配下のシーズン（Season 1, Season 2 等） |
| エピソード | Episode | 1つのシリーズ配下の個別エピソード（第1話、第2話 等） |
| 視聴履歴 | ViewingRecord | 1つのエピソードに対する視聴記録（日時、評価、感想） |
| 視聴状態 | WatchStatus | 未視聴（UNWATCHED）、視聴済み（WATCHED） |
| タイトル情報URL | TitleInfoUrl | Wikipedia、IMDb 等の情報ページリンク |
| 視聴ページURL | WatchPageUrl | 配信サービスの視聴ページリンク |

---

## ドメインエンティティ

### 1. Title（タイトル）

**集約ルート**: Title は集約ルートとして機能し、Series, Episode, ViewingRecord の整合性を保証します。

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| id | ID | Long | ✅ | タイトルの一意識別子（自動生成） |
| name | 作品名 | String | ✅ | ドラマ・アニメ・映画の名前 |
| titleInfoUrls | タイトル情報URL | List\<TitleInfoUrl\> | ❌ | 0件以上の情報ページリンク |
| series | シリーズ | List\<Series\> | ✅ | **内部的に必ず1件以上**のシリーズ（UI上では0件以上として表示可能） |
| createdAt | 作成日時 | ZonedDateTime | ✅ | タイトルを登録した日時（UTC） |
| updatedAt | 更新日時 | ZonedDateTime | ✅ | 最後に更新した日時（UTC） |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| 作品名の一意性 | 同一作品名の重複登録は拒否（完全一致チェック） |
| 作品名の長さ | 1文字以上、200文字以下 |
| シリーズの存在 | **内部的に最低1件のシリーズを持つ**。新規タイトル作成時に、デフォルトシリーズ（名前なし）を自動生成 |
| タイトル情報URL | 各URLは基本的なURL形式チェック（http/https で始まる） |
| タイトル情報URL重複 | 同一URL の重複は自動削除 |

#### 状態遷移

- **作成**: 作品名を指定して作成。デフォルトシリーズ（名前なし）とデフォルトエピソード（無名）を自動生成
- **更新**: 作品名の変更、タイトル情報URL の追加・削除
- **削除**: カスケード削除により、配下のすべての Series, Episode, ViewingRecord も削除（確認ダイアログ付き）

---

### 2. Series（シリーズ）

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| id | ID | Long | ✅ | シリーズの一意識別子（自動生成） |
| titleId | タイトルID | Long | ✅ | 親タイトルの ID（外部キー） |
| name | シーズン名 | String | ❌ | 「Season 1」「Season 2」等（空文字列の場合はデフォルトシリーズ） |
| episodes | エピソード | List\<Episode\> | ✅ | **内部的に必ず1件以上**のエピソード（UI上では0件以上として表示可能） |
| createdAt | 作成日時 | ZonedDateTime | ✅ | シリーズを追加した日時（UTC） |
| updatedAt | 更新日時 | ZonedDateTime | ✅ | 最後に更新した日時（UTC） |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| シーズン名の長さ | 0文字以上、100文字以下（空文字列の場合はデフォルトシリーズ） |
| エピソードの存在 | **内部的に最低1件のエピソードを持つ**。新規シリーズ作成時に、デフォルトエピソード（無名）を自動生成 |
| タイトルID の存在 | 親タイトルが存在すること |

#### 状態遷移

- **作成**: タイトル配下にシーズン名を指定して作成。デフォルトエピソード（無名）を自動生成
- **更新**: シーズン名の変更、配下のエピソード追加・削除時に updatedAt を更新
- **削除**: カスケード削除により、配下のすべての Episode, ViewingRecord も削除（確認ダイアログ付き）

---

### 3. Episode（エピソード）

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| id | ID | Long | ✅ | エピソードの一意識別子（自動生成） |
| seriesId | シリーズID | Long | ✅ | 親シリーズの ID（外部キー） |
| episodeInfo | エピソード情報 | String | ❌ | 「第1話」「全12話」等（空文字列の場合はデフォルトエピソード） |
| watchPageUrls | 視聴ページURL | List\<WatchPageUrl\> | ❌ | 0件以上の視聴ページリンク |
| watchStatus | 視聴状態 | WatchStatus | ✅ | UNWATCHED（未視聴）/ WATCHED（視聴済み） |
| viewingRecords | 視聴履歴 | List\<ViewingRecord\> | ❌ | 0件以上の視聴記録 |
| createdAt | 作成日時 | ZonedDateTime | ✅ | エピソードを追加した日時（UTC） |
| updatedAt | 更新日時 | ZonedDateTime | ✅ | 最後に更新した日時（UTC） |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| エピソード情報の長さ | 0文字以上、200文字以下（空文字列の場合はデフォルトエピソード） |
| 視聴ページURL | 各URLは基本的なURL形式チェック（http/https で始まる） |
| 視聴ページURL重複 | 同一URL の重複は自動削除 |
| 視聴状態の初期値 | 新規作成時は UNWATCHED（未視聴） |
| 視聴状態の変更制限 | **一度 WATCHED（視聴済み）に変更されたら、状態変更は不可**（ただし、すべての視聴履歴を削除した場合は UNWATCHED に戻る） |

#### 状態遷移

- **作成**: シリーズ配下にエピソード情報を指定して作成。初期状態は UNWATCHED
- **未視聴 → 視聴済み**: 視聴完了時に WATCHED に変更し、ViewingRecord を追加
- **視聴済み → 未視聴**: **すべての ViewingRecord を削除した場合のみ**、UNWATCHED に戻る（状態変更が可能な唯一のケース）
- **削除**: 物理削除により、配下のすべての ViewingRecord も削除

---

### 4. ViewingRecord（視聴履歴）

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| id | ID | Long | ✅ | 視聴履歴の一意識別子（自動生成） |
| episodeId | エピソードID | Long | ✅ | 親エピソードの ID（外部キー） |
| watchedAt | 視聴完了日時 | ZonedDateTime | ✅ | 視聴を完了した日時（ユーザー入力、UTC） |
| rating | 評価 | Integer | ✅ | 1-5の数値スコア |
| comment | 感想 | String | ❌ | ユーザーが入力した自由形式のテキスト |
| recordedAt | 記録日時 | ZonedDateTime | ✅ | 視聴履歴を記録した日時（自動生成、UTC） |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| 評価の範囲 | 1-5の整数（必須） |
| 感想の長さ | 0文字以上、2000文字以下 |
| 視聴完了日時 | 現在日時より未来の日付は拒否 |
| エピソードID の存在 | 親エピソードが存在すること |

#### 状態遷移

- **作成**: エピソードを視聴済みに変更する際、または視聴済みエピソードに追加視聴を記録する際に作成
- **更新**: **視聴履歴は一度記録したら変更不可**（不変性を保証）
- **削除**: 視聴履歴を削除可能。すべての視聴履歴が削除された場合、親エピソードの状態を UNWATCHED に戻す

---

## 値オブジェクト

### TitleInfoUrl（タイトル情報URL）

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| url | URL | String | ✅ | Wikipedia、IMDb 等の情報ページリンク |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| URL 形式 | http/https で始まる有効なURL |
| URL 長さ | 10文字以上、2000文字以下 |
| 不変性 | 一度作成したら変更不可（値オブジェクト） |

---

### WatchPageUrl（視聴ページURL）

#### 属性

| 属性名（英語） | 属性名（日本語） | 型 | 必須 | 説明 |
|----------------|------------------|-----|------|------|
| url | URL | String | ✅ | 配信サービスの視聴ページリンク |

#### バリデーションルール

| ルール | 詳細 |
|--------|------|
| URL 形式 | http/https で始まる有効なURL |
| URL 長さ | 10文字以上、2000文字以下 |
| 不変性 | 一度作成したら変更不可（値オブジェクト） |

---

## 列挙型

### WatchStatus（視聴状態）

| 値 | 説明 |
|-----|------|
| UNWATCHED | 未視聴 |
| WATCHED | 視聴済み |

---

## エンティティ間のリレーション

### ER図（概念図）

```
Title (1) ──────< (N) Series (1) ──────< (N) Episode (1) ──────< (N) ViewingRecord
  │                                          │
  │                                          │
  └─< (N) TitleInfoUrl                       └─< (N) WatchPageUrl
```

### リレーション詳細

| 親エンティティ | 子エンティティ | リレーション | カスケード削除 | 説明 |
|----------------|----------------|--------------|----------------|------|
| Title | Series | 1対多 | ✅ | タイトルは複数のシリーズを持つ（内部的に最低1件） |
| Title | TitleInfoUrl | 1対多 | ✅ | タイトルは複数のタイトル情報URLを持つ（0件以上） |
| Series | Episode | 1対多 | ✅ | シリーズは複数のエピソードを持つ（内部的に最低1件） |
| Episode | WatchPageUrl | 1対多 | ✅ | エピソードは複数の視聴ページURLを持つ（0件以上） |
| Episode | ViewingRecord | 1対多 | ✅ | エピソードは複数の視聴履歴を持つ（0件以上） |

### 外部キー制約

| テーブル | カラム | 参照先テーブル | 参照先カラム | ON DELETE |
|----------|--------|----------------|--------------|-----------|
| series | title_id | titles | id | CASCADE |
| episodes | series_id | series | id | CASCADE |
| title_info_urls | title_id | titles | id | CASCADE |
| watch_page_urls | episode_id | episodes | id | CASCADE |
| viewing_records | episode_id | episodes | id | CASCADE |

---

## データベーススキーマ（PostgreSQL）

### titles テーブル

```sql
CREATE TABLE titles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_titles_name ON titles(name);
```

### series テーブル

```sql
CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    name VARCHAR(100) DEFAULT '',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE
);

CREATE INDEX idx_series_title_id ON series(title_id);
```

### episodes テーブル

```sql
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT NOT NULL,
    episode_info VARCHAR(200) DEFAULT '',
    watch_status VARCHAR(20) NOT NULL DEFAULT 'UNWATCHED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE CASCADE
);

CREATE INDEX idx_episodes_series_id ON episodes(series_id);
CREATE INDEX idx_episodes_watch_status ON episodes(watch_status);
```

### viewing_records テーブル

```sql
CREATE TABLE viewing_records (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    watched_at TIMESTAMPTZ NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE
);

CREATE INDEX idx_viewing_records_episode_id ON viewing_records(episode_id);
CREATE INDEX idx_viewing_records_watched_at ON viewing_records(watched_at DESC);
```

### title_info_urls テーブル

```sql
CREATE TABLE title_info_urls (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    url VARCHAR(2000) NOT NULL,
    FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE,
    UNIQUE (title_id, url)
);

CREATE INDEX idx_title_info_urls_title_id ON title_info_urls(title_id);
```

### watch_page_urls テーブル

```sql
CREATE TABLE watch_page_urls (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    url VARCHAR(2000) NOT NULL,
    FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE,
    UNIQUE (episode_id, url)
);

CREATE INDEX idx_watch_page_urls_episode_id ON watch_page_urls(episode_id);
```

---

## ドメインサービス

### TitleDuplicationCheckService

**責務**: タイトル名の重複チェック

**メソッド**:
- `boolean isDuplicate(String titleName)`: 同一タイトル名が既に存在するかチェック

**ロジック**:
- データベースに同一タイトル名（完全一致）が存在する場合は `true` を返す
- 大文字小文字は区別しない（例: "Title" と "title" は重複とみなす）

---

## 集約の整合性ルール

### Title 集約

**集約ルート**: Title

**整合性保証**:
1. **シリーズの最小件数**: Title は必ず最低1件の Series を持つ。Title 作成時に、デフォルトシリーズを自動生成
2. **タイトル名の一意性**: 同一タイトル名の重複登録は拒否
3. **カスケード削除**: Title 削除時に、配下のすべての Series, Episode, ViewingRecord, TitleInfoUrl を削除

### Series 集約（Title 配下）

**整合性保証**:
1. **エピソードの最小件数**: Series は必ず最低1件の Episode を持つ。Series 作成時に、デフォルトエピソードを自動生成
2. **カスケード削除**: Series 削除時に、配下のすべての Episode, ViewingRecord, WatchPageUrl を削除

### Episode 集約（Series 配下）

**整合性保証**:
1. **視聴状態の不変性**: 一度 WATCHED に変更されたら、状態変更は不可（すべての ViewingRecord 削除時を除く）
2. **視聴履歴の整合性**: すべての ViewingRecord が削除された場合、Episode の状態を UNWATCHED に戻す
3. **カスケード削除**: Episode 削除時に、配下のすべての ViewingRecord, WatchPageUrl を削除

---

## UI 表示ロジック（内部構造 vs UI表示分離）

### 条件付き非表示ルール

| 条件 | UI 表示 | 内部データ |
|------|---------|-----------|
| シリーズが1つだけ | シリーズレイヤーを非表示。エピソードをタイトル直下に表示 | Series は1件存在（name が空文字列の場合あり） |
| エピソードが1つだけ | エピソードレイヤーを非表示。単純なエントリとして表示 | Episode は1件存在（episodeInfo が空文字列の場合あり） |
| シリーズが複数 | シリーズレイヤーを表示。完全な階層構造を表示 | Series は2件以上存在 |

### 動的切り替え

- **シリーズ追加時**: 1件 → 2件になる際、UI をシリーズ階層表示に自動切り替え
- **シリーズ削除時**: 2件 → 1件になる際、UI をシリーズレイヤー非表示に自動切り替え

---

---

## Phase 3: アーキテクチャ改善 - CQRS + 独立集約パターン

**状態**: 計画段階（実装前）

### 背景と課題

現在のデータモデル（Phase 1 設計）では、Title が集約ルートとして Series と Episode を内包する **Giant Aggregate** パターンを採用しており、以下の課題が発生しています：

1. **N+1 クエリ問題**: GetAllTitles で 50 タイトル取得時に 101 クエリ（1 + 50 Series + 50 TitleInfoUrls）
2. **スケーラビリティの低下**: Series や Episode が増えると、Title のロード時間が増加
3. **データ整合性の問題**: CreateSeriesUseCase で `title.getSeries().add(series)` をしても、Series テーブルへの保存が確実ではない

### 改善方針

**案A（推奨）: 3つの独立集約に再設計**:
- Title、Series、Episode をそれぞれ独立した集約ルートに昇格
- オブジェクト参照を廃止し、ID 参照のみに変更
- CQRS パターンを適用し、Read Model で JOIN を使用して N+1 問題を解決

### Phase 3 設計の集約構造

#### 新しい集約境界（実装予定）

```
Write Model（書き込み用）:
├── Title（集約ルート）
│   ├── id: Long
│   ├── name: String
│   └── titleInfoUrls: Set<TitleInfoUrl>
│
├── Series（独立集約ルート）
│   ├── id: Long
│   ├── titleId: Long ← ID 参照のみ
│   └── name: String
│
└── Episode（独立集約ルート）
    ├── id: Long
    ├── seriesId: Long ← ID 参照のみ
    ├── episodeInfo: String
    ├── watchPageUrls: List<WatchPageUrl>
    └── viewingRecords: List<ViewingRecord>

Read Model（読み取り用）:
├── TitleListReadModel（タイトル一覧用、JOIN不要）
├── TitleDetailReadModel（タイトル詳細用、Series + Episode を JOIN で取得）
├── SeriesReadModel（Series 詳細用）
└── EpisodeReadModel（Episode 詳細用）
```

#### 集約ごとの責任

| 集約 | 現在の設計 | Phase 3 の設計 |
|-----|----------|--------------|
| **Title** | Series をオブジェクト参照で内包 | Series は titleId で ID 参照のみ |
| **Series** | Episode をオブジェクト参照で内包 | Episode は seriesId で ID 参照のみ |
| **Episode** | ViewingRecord を内包 | ViewingRecord は内包のまま（変更なし） |

### CQRS パターン適用の詳細

#### Write Model（書き込み操作用）

**使用場面**:
- CreateTitle, CreateSeries, CreateEpisode, UpdateTitle など
- ドメインロジック（バリデーション、ビジネスルール）を実装

**Repository**:
- TitleRepository: Title + TitleInfoUrl のみ管理
- SeriesRepository: Series のみ管理（Episode を含まない）
- EpisodeRepository: Episode + ViewingRecord を管理

**実装例**:
```java
// Title から Series フィールドを削除
public class Title {
    private final Long id;
    private String name;
    private final Set<TitleInfoUrl> titleInfoUrls;
    // private List<Series> series; ← 削除
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Series から Episode フィールドを削除
public class Series {
    private final Long id;
    private final Long titleId;  // ID 参照のみ
    private String name;
    // private List<Episode> episodes; ← 削除
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### Read Model（読み取り操作用）

**使用場面**:
- GetAllTitles, GetTitleDetail など
- ビジネスロジック不要。データ転送の最適化に特化

**リードモデル**:
```java
// タイトル一覧用（シンプル）
public class TitleListReadModel {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// タイトル詳細用（Series + Episode を含む）
public class TitleDetailReadModel {
    private Long id;
    private String name;
    private List<String> titleInfoUrls;
    private List<SeriesReadModel> series;  // JOIN で取得
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

public class SeriesReadModel {
    private Long id;
    private String name;
    private List<EpisodeReadModel> episodes;  // JOIN で取得
}

public class EpisodeReadModel {
    private Long id;
    private String episodeInfo;
    private String watchStatus;
    private List<String> watchPageUrls;
    private int viewingRecordCount;
}
```

**SQL による JOIN 最適化**:
```sql
-- GetAllTitles（1 クエリ）
SELECT id, name, created_at, updated_at FROM titles ORDER BY created_at DESC;

-- GetTitleDetail（1 クエリ、JOIN）
SELECT
    t.id, t.name,
    s.id, s.name,
    e.id, e.episode_info, e.watch_status,
    tiu.url,
    COUNT(vr.id) as viewing_record_count
FROM titles t
LEFT JOIN title_info_urls tiu ON t.id = tiu.title_id
LEFT JOIN series s ON t.id = s.title_id
LEFT JOIN episodes e ON s.id = e.series_id
LEFT JOIN viewing_records vr ON e.id = vr.episode_id
WHERE t.id = ?
GROUP BY t.id, s.id, e.id, tiu.url
ORDER BY s.created_at, e.created_at;
```

### パフォーマンス改善の期待値

| 操作 | 現在（Phase 1） | Phase 3 後 | 削減率 |
|------|----------------|----------|-------|
| GetAllTitles（50タイトル） | 101 クエリ | 1 クエリ | 99% |
| GetTitleDetail | 2-3 クエリ | 1 クエリ | 50-66% |
| CreateSeries | Title ロード + Series 保存 | Series 保存のみ | データ整合性向上 |
| CreateEpisode | Series ロード + Episode 保存 | Episode 保存のみ | データ整合性向上 |

### ER 図の変更（Phase 3）

**現在（Phase 1）**:
```
Title (1) ──────< (N) Series (1) ──────< (N) Episode (1) ──────< (N) ViewingRecord
```

**Phase 3 後（独立集約）**:
```
Title (集約ルート)          Series (集約ルート)         Episode (集約ルート)
├── id                      ├── id                      ├── id
├── name                     ├── titleId (FK)            ├── seriesId (FK)
└── titleInfoUrls           └── name                    ├── episodeInfo
                                                        ├── watchPageUrls
                                                        └── viewingRecords
```

### 実装計画（Milestone 構成）

#### Milestone 1: Write Model の分離
- Title から Series フィールドを削除
- Series から Episode フィールドを削除
- TitleRepositoryImpl, SeriesRepositoryImpl を修正（ロード時に子集約をロードしない）

#### Milestone 2: Read Model の追加
- TitleListReadModel, TitleDetailReadModel, SeriesReadModel, EpisodeReadModel を作成
- TitleReadMapper（MyBatis）で JOIN クエリを実装
- TitleReadService を作成

#### Milestone 3: UseCase 修正
- GetAllTitlesUseCase を Read Model に切り替え
- GetTitleDetailUseCase を Read Model に切り替え
- CreateSeriesUseCase, CreateEpisodeUseCase を簡潔化（親集約をロードしない）

#### Milestone 4: テスト検証
- 全140テストが通過することを確認
- パフォーマンステスト（N+1 クエリ削減を確認）

---

## まとめ

このデータモデルは、以下の原則に従って設計されています：

1. **DDD 準拠**:
   - **Phase 1**: Title を集約ルートとし、Series, Episode, ViewingRecord の整合性を保証
   - **Phase 3**（計画）: Title、Series、Episode を独立集約に昇格し、スケーラビリティ向上

2. **不変性**: ViewingRecord は一度記録したら変更不可（削除は可能）

3. **ユビキタス言語**: 日本語（仕様書）↔ 英語（コード）の用語対応を明確化

4. **内部構造の統一性**: すべてのタイトルが最低1件のシリーズ、すべてのシリーズが最低1件のエピソードを持つ

5. **UI 最適化**: 内部構造とは独立したUI層で、条件に応じてレイヤーを動的に非表示

6. **CQRS パターン準備**（Phase 3）: Read と Write を分離し、パフォーマンス最適化（N+1 クエリ削減）と保守性向上を実現
