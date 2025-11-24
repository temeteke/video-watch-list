# ビデオ視聴ログシステム - バックエンド

Spring Boot 3.x と MyBatis を使用した RESTful API バックエンド

## セットアップ

### 前提条件

- Java 17 以上
- Maven 3.8 以上
- PostgreSQL 14 以上
- Docker（オプション）

### インストール手順

1. リポジトリをクローン
```bash
git clone https://github.com/your-repo/video-watch-list.git
cd video-watch-list/backend
```

2. 依存関係をインストール
```bash
mvn clean install
```

3. 環境変数を設定
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/video_watch_log
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=password
```

4. データベースマイグレーション
```bash
mvn flyway:migrate
```

5. アプリケーションを起動
```bash
mvn spring-boot:run
```

アプリケーションは http://localhost:8080 で起動します。

## Docker での実行

```bash
docker-compose up backend
```

## テスト実行

### 全テストを実行
```bash
mvn test
```

### 特定のテストクラスを実行
```bash
mvn test -Dtest=SearchTitlesUseCaseTest
```

### テストカバレッジを確認
```bash
mvn jacoco:report
```

## プロジェクト構造

```
src/main/java/com/example/videowatchlog/
├── domain/              # ドメイン層（ビジネスロジック）
│   ├── model/           # ドメインモデル
│   ├── repository/      # リポジトリインターフェース
│   └── exception/       # ドメイン例外
├── application/         # アプリケーション層（ユースケース）
│   ├── usecase/         # ユースケース
│   └── dto/             # データ転送オブジェクト
├── presentation/        # プレゼンテーション層（API）
│   ├── controller/      # REST コントローラー
│   ├── exception/       # グローバルエラーハンドラー
│   └── dto/             # リクエスト/レスポンス DTO
└── infrastructure/      # インフラストラクチャー層
    └── persistence/     # データベースアクセス
```

## API エンドポイント

### タイトル関連

#### 全タイトルを取得
```
GET /titles
```

#### タイトルを検索
```
GET /titles/search?query=進撃&watchStatus=WATCHED
```

#### タイトルを取得（詳細）
```
GET /titles/{id}
```

#### タイトルを作成
```
POST /titles
Content-Type: application/json

{
  "name": "進撃の巨人",
  "titleInfoUrls": ["https://example.com"]
}
```

#### タイトルを更新
```
PUT /titles/{id}
Content-Type: application/json

{
  "name": "進撃の巨人 Season 2"
}
```

#### タイトルを削除
```
DELETE /titles/{id}
```

### シリーズ関連

#### シリーズを作成
```
POST /titles/{titleId}/series
Content-Type: application/json

{
  "name": "Season 1"
}
```

#### シリーズを取得
```
GET /titles/{titleId}/series
```

### エピソード関連

#### エピソードを作成
```
POST /series/{seriesId}/episodes
Content-Type: application/json

{
  "episodeInfo": "第1話",
  "watchPageUrls": ["https://example.com/ep1"]
}
```

#### エピソードを取得
```
GET /series/{seriesId}/episodes
GET /episodes/{episodeId}
```

#### エピソードを視聴完了
```
POST /episodes/{episodeId}/complete
Content-Type: application/json

{
  "watchedAt": "2025-01-15T20:00:00",
  "rating": 5,
  "comment": "素晴らしい回だった"
}
```

### 視聴記録関連

#### 視聴記録を追加
```
POST /episodes/{episodeId}/viewing-records
Content-Type: application/json

{
  "watchedAt": "2025-01-20T19:00:00",
  "rating": 4,
  "comment": "二度目の視聴"
}
```

#### 視聴記録を削除
```
DELETE /viewing-records/{recordId}
```

## エラーハンドリング

エラーレスポンスの形式：

```json
{
  "message": "エラーメッセージ",
  "code": "ERROR_CODE",
  "timestamp": "2025-01-15T20:30:00",
  "path": "/titles/1"
}
```

### エラーコード

- `TITLE_DUPLICATE` - タイトルが既に存在（409）
- `TITLE_NOT_FOUND` - タイトルが見つからない（404）
- `VALIDATION_FAILED` - バリデーション失敗（400）
- `INTERNAL_SERVER_ERROR` - サーバーエラー（500）

## データベーススキーマ

### titles テーブル
```sql
CREATE TABLE titles (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_titles_name ON titles(LOWER(name));
```

### series テーブル
```sql
CREATE TABLE series (
  id BIGSERIAL PRIMARY KEY,
  title_id BIGINT NOT NULL REFERENCES titles(id) ON DELETE CASCADE,
  name VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### episodes テーブル
```sql
CREATE TABLE episodes (
  id BIGSERIAL PRIMARY KEY,
  series_id BIGINT NOT NULL REFERENCES series(id) ON DELETE CASCADE,
  episode_info VARCHAR(200),
  watch_status VARCHAR(20) DEFAULT 'UNWATCHED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_episodes_watch_status ON episodes(watch_status);
```

### viewing_records テーブル
```sql
CREATE TABLE viewing_records (
  id BIGSERIAL PRIMARY KEY,
  episode_id BIGINT NOT NULL REFERENCES episodes(id) ON DELETE CASCADE,
  watched_at TIMESTAMP NOT NULL,
  rating INTEGER CHECK (rating >= 1 AND rating <= 5),
  comment TEXT,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## アーキテクチャ

### レイヤード・アーキテクチャ

```
Presentation Layer (REST API)
        ↓
Application Layer (Use Cases)
        ↓
Domain Layer (Business Logic)
        ↓
Infrastructure Layer (Database)
```

### 主要なコンポーネント

- **Entity**: ドメインモデル
- **DTO**: データ転送用オブジェクト
- **UseCase**: ビジネスロジック（トランザクション管理）
- **Repository**: データアクセス層
- **Mapper**: MyBatis SQL マッピング

## テスト戦略

### ユニットテスト
- ユースケースのロジック検証
- 予期しい例外処理の確認

### 統合テスト
- エンドツーエンドの API テスト
- トランザクション管理の確認

### テスト実行プロファイル
```bash
mvn test -P test
```

## パフォーマンス最適化

### クエリ最適化
- `SELECT DISTINCT` で N+1 問題を回避
- インデックス活用（name, watch_status など）
- WHERE 句で早期フィルタリング

### キャッシング戦略
- Spring Cache を使用（将来実装予定）
- データベース接続プーリング

## ホットキー/ベストプラクティス

1. **バリデーション**: すべての入力は DTO レベルで検証
2. **例外処理**: GlobalExceptionHandler で一元管理
3. **トランザクション**: `@Transactional` で自動管理
4. **ロギング**: Spring Boot logging で詳細記録
5. **セキュリティ**: バリデーション、入力検証（OWASP Top 10 対応）

## トラブルシューティング

### データベース接続エラー
```bash
# PostgreSQL の確認
psql -U postgres -c "SELECT version();"

# マイグレーションの再実行
mvn flyway:clean flyway:migrate
```

### テスト失敗時
```bash
# ログをクリアして再実行
mvn clean test

# デバッグモードで実行
mvn -X test
```

## 今後の改善

- [ ] スプリングセキュリティ実装（認証・認可）
- [ ] API レート制限
- [ ] キャッシング戦略の改善
- [ ] メトリクス収集（Micrometer）
- [ ] ドキュメント自動生成（Swagger/OpenAPI）
