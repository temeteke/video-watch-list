# Video Watch List

ドラマ・アニメ・映画の視聴予定リストと視聴記録を管理するWebアプリケーション

## 概要

作品を階層的に管理（作品 → シリーズ → エピソード）し、視聴ステータスの追跡、評価・コメント付きの視聴記録、検索・フィルタリング機能を提供します。

## 技術スタック

### バックエンド
- **言語**: Java 17
- **フレームワーク**: Spring Boot 3.2.0
- **ORM**: MyBatis
- **データベース**: PostgreSQL 15
- **マイグレーション**: Flyway
- **テスト**: JUnit 5

### フロントエンド
- **言語**: TypeScript 5.3
- **フレームワーク**: Next.js 14 (App Router)
- **ライブラリ**: React 18
- **テスト**: Jest, React Testing Library, Playwright

### インフラ
- **コンテナ**: Docker Compose
- **開発環境**: ホットリロード対応

### アーキテクチャ
- **設計手法**: Domain-Driven Design (DDD)
- **構造**: Onion Architecture
- **開発手法**: Test-Driven Development (TDD)

## セットアップ

### 前提条件

- Docker & Docker Compose がインストールされていること
- Git がインストールされていること

### 環境構築

1. リポジトリをクローン

```bash
git clone <repository-url>
cd video-watch-list
```

2. 環境変数を設定（オプション）

```bash
cp .env.example .env
# 必要に応じて .env を編集
```

デフォルト値:
- `POSTGRES_USER=videowatchuser`
- `POSTGRES_PASSWORD=password`
- `POSTGRES_DB=videowatchlog`

3. 開発環境を起動

```bash
make dev
```

これにより以下のサービスが起動します:
- **バックエンド**: http://localhost:8080
- **フロントエンド**: http://localhost:3000
- **データベース**: localhost:5432

## 開発コマンド

### 起動・停止

```bash
make dev             # 開発サーバーを起動（ホットリロード有効）
make up              # 本番用コンテナを起動
make down            # すべてのコンテナを停止・削除
make restart         # サービスを再起動
```

### テスト

```bash
make test            # 単体テストを実行
make test-watch      # テストをwatchモードで実行
```

### 状態確認

```bash
make ps              # コンテナの状態を表示
make status          # 詳しいサービス状態を表示
make logs            # すべてのログを表示
make logs-backend    # バックエンドのログを表示
make logs-frontend   # フロントエンドのログを表示
make logs-db         # データベースのログを表示
```

### デバッグ

```bash
make shell-backend   # バックエンドコンテナにシェルで接続
make shell-frontend  # フロントエンドコンテナにシェルで接続
```

### ビルド・管理

```bash
make build           # 本番用イメージをビルド
make validate        # 環境・設定ファイルを検証
make clean           # ボリュームを含めてすべて削除
make rebuild         # クリーン後に再ビルド
```

### その他

```bash
make help            # 利用可能なコマンドを表示
make help-all        # 詳細なヘルプを表示
```

## プロジェクト構造

```
video-watch-list/
├── backend/                 # Spring Boot アプリケーション
│   ├── src/main/java/
│   │   └── com/example/videowatchlog/
│   │       ├── domain/              # ドメイン層（純粋なビジネスロジック）
│   │       ├── application/         # アプリケーション層（ユースケース）
│   │       ├── infrastructure/      # インフラ層（MyBatis実装）
│   │       └── presentation/        # プレゼンテーション層（REST API）
│   └── src/test/               # テストコード
│
├── frontend/                # Next.js アプリケーション
│   ├── src/
│   │   ├── app/            # ページコンポーネント（App Router）
│   │   ├── components/     # 再利用可能なコンポーネント
│   │   └── lib/           # APIクライアント・ユーティリティ
│   └── tests/             # テストコード
│
├── specs/001-watch-list/    # 仕様・設計ドキュメント
│   ├── spec.md             # 要件定義
│   ├── plan.md             # 実装計画
│   ├── tasks.md            # タスク分解
│   └── data-model.md       # データモデル
│
├── docker-compose.yml       # 本番用コンテナ定義
├── docker-compose.override.yml  # 開発用オーバーライド
├── Makefile                # 開発コマンド定義
└── CLAUDE.md               # AI開発支援用ガイド
```

## データベーススキーマ

主要テーブル:
- **titles**: 作品情報
- **series**: シリーズ情報（作品に属する）
- **episodes**: エピソード情報（シリーズに属する）
- **viewing_records**: 視聴記録（エピソードに属する）

詳細は `backend/src/main/resources/db/migration/` を参照してください。

## API仕様

- **ベースURL**: `http://localhost:8080/api/v1`
- **CORS**: `http://localhost:3000` からのアクセスを許可

主要エンドポイント:
- `GET /titles` - 作品一覧取得
- `POST /titles` - 作品作成
- `GET /titles/{id}` - 作品詳細取得
- `POST /series/{seriesId}/episodes` - エピソード作成
- その他詳細は `specs/001-watch-list/contracts/` を参照

## テスト戦略

### バックエンド（TDD）

1. **ドメインテスト**: エンティティ・値オブジェクトの単体テスト
2. **アプリケーションテスト**: ユースケースの単体テスト（モック使用）
3. **インフラテスト**: MyBatis統合テスト
4. **プレゼンテーションテスト**: REST API統合テスト

### フロントエンド

1. **コンポーネントテスト**: React コンポーネントの単体テスト
2. **ページテスト**: ページ全体の統合テスト
3. **E2Eテスト**: Playwright によるユーザージャーニーテスト

**カバレッジ目標**: ビジネスロジック部分で 80% 以上

## 開発ワークフロー

1. **仕様定義**: `specs/001-watch-list/spec.md` に要件を記述
2. **実装計画**: `/speckit.plan` で計画生成
3. **タスク分解**: `/speckit.tasks` でタスク分解
4. **TDD実装**: Red → Green → Refactor サイクル
5. **テスト実行**: `make test` で全テスト実行
6. **コミット**: 適切なコミットメッセージでコミット

詳細は `CLAUDE.md` および `.specify/memory/constitution.md` を参照してください。

## トラブルシューティング

### ポート競合

ポート 8080, 5432, 3000 が既に使用されている場合:
- `docker-compose.yml` のポートマッピングを変更
- または既存のプロセスを停止

### ボリューム削除後のデータ消失

```bash
make clean  # ボリュームも削除されるため注意
make down   # ボリュームは保持される
```

### バックエンドビルドエラー

```bash
make rebuild  # クリーンビルド
make logs-backend  # ログを確認
```

### CORS エラー

`backend/src/main/java/com/example/videowatchlog/config/WebConfig.java` で許可オリジンを確認してください。

## ライセンス

（ライセンス情報を記載）

## 貢献

（コントリビューションガイドを記載）

## 連絡先

（連絡先情報を記載）

---

**最終更新**: 2025-11-23
**現在のフェーズ**: Phase 4 (User Story 2 - 視聴記録管理)
**ブランチ**: `001-watch-list`
