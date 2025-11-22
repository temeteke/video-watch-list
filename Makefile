.PHONY: help test test-watch dev up down logs logs-backend logs-frontend logs-db build clean rebuild

# デフォルトターゲット: ヘルプを表示
help:
	@echo "利用可能なコマンド:"
	@echo "  make test          - 単体テストを実行"
	@echo "  make test-watch    - テストをwatch モードで実行"
	@echo "  make dev           - 開発サーバーを起動（ホットリロード有効）"
	@echo "  make up            - 本番用コンテナを起動"
	@echo "  make down          - すべてのコンテナを停止・削除"
	@echo "  make logs          - すべてのログを表示"
	@echo "  make logs-backend  - バックエンドのログを表示"
	@echo "  make logs-frontend - フロントエンドのログを表示"
	@echo "  make logs-db       - データベースのログを表示"
	@echo "  make build         - 本番用イメージをビルド"
	@echo "  make clean         - ボリュームを含めてすべて削除"
	@echo "  make rebuild       - クリーン後に再ビルド"

# テスト実行
test:
	@echo "テストを実行中..."
	docker-compose run --rm test mvn test

# テストをwatchモードで実行
test-watch:
	@echo "テストをwatchモードで実行中..."
	docker-compose run --rm test mvn test -Dtest.watch=true

# 開発サーバー起動（ホットリロード有効）
dev:
	@echo "開発サーバーを起動中..."
	docker-compose up db backend-dev frontend

# 本番用コンテナ起動
up:
	@echo "本番用コンテナを起動中..."
	docker-compose up -d

# コンテナ停止・削除
down:
	@echo "コンテナを停止中..."
	docker-compose down

# すべてのログを表示
logs:
	docker-compose logs -f

# バックエンドのログを表示
logs-backend:
	docker-compose logs -f backend

# フロントエンドのログを表示
logs-frontend:
	docker-compose logs -f frontend

# データベースのログを表示
logs-db:
	docker-compose logs -f db

# 本番用イメージをビルド
build:
	@echo "本番用イメージをビルド中..."
	docker-compose build

# ボリュームを含めてすべて削除
clean:
	@echo "すべてのコンテナ・ボリュームを削除中..."
	docker-compose down -v
	docker volume rm video-watch-list_maven_cache 2>/dev/null || true

# クリーン後に再ビルド
rebuild: clean build
	@echo "再ビルド完了"
