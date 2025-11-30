# Quickstart: 視聴予定リスト

**Feature**: 001-watch-list
**Date**: 2025-11-20
**Purpose**: 開発環境のセットアップと最初のタイトル登録までのクイックスタートガイド

---

## 前提条件

- **Docker** と **Docker Compose** がインストールされていること
- **Java 17+** がインストールされていること（ローカル単体テスト実行時のみ）
- **Node.js 18+** と **npm** がインストールされていること（ローカル単体テスト実行時のみ）
- **Git** がインストールされていること

---

## 1. リポジトリのクローン

```bash
git clone https://github.com/your-org/video-watch-list.git
cd video-watch-list
```

---

## 2. 環境変数の設定

`.env.example` をコピーして `.env` ファイルを作成します。

```bash
cp .env.example .env
```

`.env` ファイルを編集し、必要な環境変数を設定します（デフォルト値で動作する場合は編集不要）。

```env
# PostgreSQL
POSTGRES_USER=videowatch
POSTGRES_PASSWORD=secret
POSTGRES_DB=videowatchlog

# Backend (Spring Boot)
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# Frontend (Next.js)
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

---

## 3. Docker Compose で起動

docker compose により、バックエンド・フロントエンド・PostgreSQL を一括起動します。

```bash
docker compose up -d
```

起動確認：

```bash
docker compose ps
```

以下のコンテナが起動していることを確認：

- `video-watch-list-backend` (Spring Boot)
- `video-watch-list-frontend` (Next.js)
- `video-watch-list-db` (PostgreSQL)

---

## 4. データベースマイグレーション

Flyway によるマイグレーションは、バックエンドコンテナ起動時に自動実行されます。
マイグレーション状態を確認するには、バックエンドコンテナのログを確認します。

```bash
docker compose logs backend
```

以下のようなログが表示されれば、マイグレーション成功です：

```
Flyway migration completed successfully.
```

---

## 5. API の動作確認

### 5-1. ヘルスチェック

バックエンド API が起動しているか確認します。

```bash
curl http://localhost:8080/actuator/health
```

レスポンス例：

```json
{
  "status": "UP"
}
```

### 5-2. タイトル一覧を取得（空の状態）

```bash
curl http://localhost:8080/api/v1/titles
```

レスポンス例（初期状態は空配列）：

```json
[]
```

---

## 6. 最初のタイトルを登録

### 6-1. タイトル作成リクエスト

```bash
curl -X POST http://localhost:8080/api/v1/titles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ワンピース",
    "titleInfoUrls": ["https://ja.wikipedia.org/wiki/ONE_PIECE"]
  }'
```

レスポンス例：

```json
{
  "id": 1,
  "name": "ワンピース",
  "titleInfoUrls": ["https://ja.wikipedia.org/wiki/ONE_PIECE"],
  "series": [
    {
      "id": 1,
      "titleId": 1,
      "name": "",
      "episodes": [
        {
          "id": 1,
          "seriesId": 1,
          "episodeInfo": "",
          "watchPageUrls": [],
          "watchStatus": "UNWATCHED",
          "viewingRecords": [],
          "createdAt": "2025-01-20T10:00:00Z",
          "updatedAt": "2025-01-20T10:00:00Z"
        }
      ],
      "createdAt": "2025-01-20T10:00:00Z",
      "updatedAt": "2025-01-20T10:00:00Z"
    }
  ],
  "createdAt": "2025-01-20T10:00:00Z",
  "updatedAt": "2025-01-20T10:00:00Z"
}
```

### 6-2. タイトル詳細を取得

```bash
curl http://localhost:8080/api/v1/titles/1
```

レスポンス例：上記と同様

---

## 7. フロントエンドへのアクセス

ブラウザで以下のURLにアクセスします。

```
http://localhost:3000
```

トップページが表示され、登録したタイトル「ワンピース」が一覧に表示されます。

---

## 8. シリーズとエピソードを追加

### 8-1. シリーズを追加

```bash
curl -X POST http://localhost:8080/api/v1/titles/1/series \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Season 1"
  }'
```

レスポンス例：

```json
{
  "id": 2,
  "titleId": 1,
  "name": "Season 1",
  "episodes": [
    {
      "id": 2,
      "seriesId": 2,
      "episodeInfo": "",
      "watchPageUrls": [],
      "watchStatus": "UNWATCHED",
      "viewingRecords": [],
      "createdAt": "2025-01-20T10:05:00Z",
      "updatedAt": "2025-01-20T10:05:00Z"
    }
  ],
  "createdAt": "2025-01-20T10:05:00Z",
  "updatedAt": "2025-01-20T10:05:00Z"
}
```

### 8-2. エピソードを追加

```bash
curl -X POST http://localhost:8080/api/v1/series/2/episodes \
  -H "Content-Type: application/json" \
  -d '{
    "episodeInfo": "第1話",
    "watchPageUrls": ["https://www.netflix.com/watch/12345"]
  }'
```

レスポンス例：

```json
{
  "id": 3,
  "seriesId": 2,
  "episodeInfo": "第1話",
  "watchPageUrls": ["https://www.netflix.com/watch/12345"],
  "watchStatus": "UNWATCHED",
  "viewingRecords": [],
  "createdAt": "2025-01-20T10:10:00Z",
  "updatedAt": "2025-01-20T10:10:00Z"
}
```

---

## 9. エピソードを視聴済みに変更

### 9-1. 視聴完了リクエスト

```bash
curl -X POST http://localhost:8080/api/v1/episodes/3/complete \
  -H "Content-Type: application/json" \
  -d '{
    "watchedAt": "2025-01-20T20:00:00Z",
    "rating": 5,
    "comment": "とても面白かった!"
  }'
```

レスポンス例：

```json
{
  "id": 3,
  "seriesId": 2,
  "episodeInfo": "第1話",
  "watchPageUrls": ["https://www.netflix.com/watch/12345"],
  "watchStatus": "WATCHED",
  "viewingRecords": [
    {
      "id": 1,
      "episodeId": 3,
      "watchedAt": "2025-01-20T20:00:00Z",
      "rating": 5,
      "comment": "とても面白かった!",
      "recordedAt": "2025-01-20T20:05:00Z"
    }
  ],
  "createdAt": "2025-01-20T10:10:00Z",
  "updatedAt": "2025-01-20T20:05:00Z"
}
```

---

## 10. タイトル検索とフィルタリング

### 10-1. タイトル名で検索

```bash
curl "http://localhost:8080/api/v1/titles?query=ワンピース"
```

レスポンス例：

```json
[
  {
    "id": 1,
    "name": "ワンピース",
    "createdAt": "2025-01-20T10:00:00Z",
    "updatedAt": "2025-01-20T10:00:00Z"
  }
]
```

### 10-2. 視聴状態でフィルタ

未視聴のエピソードのみをフィルタ：

```bash
curl "http://localhost:8080/api/v1/titles?watchStatus=UNWATCHED"
```

視聴済みのエピソードのみをフィルタ：

```bash
curl "http://localhost:8080/api/v1/titles?watchStatus=WATCHED"
```

---

## 11. ローカル開発（docker compose を使用しない場合）

### 11-1. バックエンドのローカル起動

PostgreSQL を docker compose で起動（バックエンドのみローカル実行）：

```bash
docker compose up -d db
```

バックエンドをローカルで起動：

```bash
cd backend
./mvnw spring-boot:run
```

### 11-2. フロントエンドのローカル起動

```bash
cd frontend
npm install
npm run dev
```

ブラウザで `http://localhost:3000` にアクセス。

---

## 12. テストの実行

### 12-1. バックエンド単体テスト（JUnit）

```bash
cd backend
./mvnw test
```

### 12-2. フロントエンド単体テスト（Jest）

```bash
cd frontend
npm test
```

### 12-3. E2E テスト（Playwright）

```bash
cd frontend
npm run test:e2e
```

---

## 13. docker compose の停止

```bash
docker compose down
```

データベースのボリュームも削除する場合：

```bash
docker compose down -v
```

---

## トラブルシューティング

### Q1. バックエンドが起動しない

**A1**: ログを確認してください。

```bash
docker compose logs backend
```

PostgreSQL への接続エラーが表示される場合、`.env` ファイルの `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB` が正しいか確認してください。

### Q2. フロントエンドがバックエンドに接続できない

**A2**: `.env` ファイルの `NEXT_PUBLIC_API_URL` が正しいか確認してください。デフォルトは `http://localhost:8080/api/v1` です。

### Q3. Flyway マイグレーションが失敗する

**A3**: PostgreSQL コンテナが正常に起動しているか確認してください。

```bash
docker compose logs db
```

データベースが破損している場合、ボリュームを削除して再起動してください。

```bash
docker compose down -v
docker compose up -d
```

---

## 次のステップ

- **仕様書**: `specs/001-watch-list/spec.md` を参照し、全機能を確認
- **データモデル**: `specs/001-watch-list/data-model.md` を参照し、エンティティ構造を理解
- **API 契約**: `specs/001-watch-list/contracts/openapi.yaml` を参照し、すべてのエンドポイントを確認
- **実装タスク**: `specs/001-watch-list/tasks.md`（Phase 2 で生成）を参照し、実装手順を確認

Happy Coding! 🎬
