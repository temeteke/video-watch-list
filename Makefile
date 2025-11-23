.PHONY: help help-all test test-watch dev up down logs logs-backend logs-frontend logs-db \
         build clean rebuild ps status shell-backend shell-frontend restart validate

# デフォルトターゲット: ヘルプを表示
help:
	@echo "利用可能なコマンド:"
	@echo ""
	@echo "【起動・停止】"
	@echo "  make dev           - 開発サーバーを起動（ホットリロード有効）"
	@echo "  make up            - 本番用コンテナを起動"
	@echo "  make down          - すべてのコンテナを停止・削除"
	@echo "  make restart       - サービスを再起動"
	@echo ""
	@echo "【状態確認】"
	@echo "  make ps            - コンテナの状態を表示"
	@echo "  make status        - 詳しいサービス状態を表示"
	@echo ""
	@echo "【ログ】"
	@echo "  make logs          - すべてのログを表示"
	@echo "  make logs-backend  - バックエンドのログを表示"
	@echo "  make logs-frontend - フロントエンドのログを表示"
	@echo "  make logs-db       - データベースのログを表示"
	@echo ""
	@echo "【テスト】"
	@echo "  make test          - 単体テストを実行"
	@echo "  make test-watch    - テストをwatch モードで実行"
	@echo ""
	@echo "【ビルド・管理】"
	@echo "  make build         - 本番用イメージをビルド"
	@echo "  make validate      - 環境・設定ファイルを検証"
	@echo "  make clean         - ボリュームを含めてすべて削除"
	@echo "  make rebuild       - クリーン後に再ビルド"
	@echo ""
	@echo "【シェルアクセス】"
	@echo "  make shell-backend  - バックエンドコンテナにシェルで接続"
	@echo "  make shell-frontend - フロントエンドコンテナにシェルで接続"
	@echo ""
	@echo "詳細版は: make help-all"

# 詳細なヘルプを表示
help-all: help
	@echo ""
	@echo "【詳細説明】"
	@echo ""
	@echo "dev:"
	@echo "  開発環境でホットリロード有効でサーバーを起動します。"
	@echo "  コードの変更が自動的に反映されます。"
	@echo ""
	@echo "up:"
	@echo "  本番環境でコンテナを起動します。-d フラグで バックグラウンド実行。"
	@echo ""
	@echo "ps:"
	@echo "  docker-compose ps と同じ。コンテナの実行状態を確認できます。"
	@echo ""
	@echo "status:"
	@echo "  ps に加えて、各サービスのヘルスチェック状態なども確認できます。"
	@echo ""
	@echo "shell-backend/shell-frontend:"
	@echo "  コンテナ内のシェルに対話的にアクセス。デバッグやコマンド実行に便利。"
	@echo "  終了は: exit"
	@echo ""
	@echo "validate:"
	@echo "  docker-compose ファイルや環境設定を検証します。"
	@echo "  本番環境へのデプロイ前に実行推奨。"

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
	docker-compose up -d db backend frontend

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
	docker-compose build backend frontend

# ボリュームを含めてすべて削除
clean:
	@echo "すべてのコンテナ・ボリュームを削除中..."
	docker-compose down -v
	docker volume rm video-watch-list_maven_cache 2>/dev/null || true

# クリーン後に再ビルド
rebuild: clean build
	@echo "再ビルド完了"

# コンテナの状態表示
ps:
	docker-compose ps

# 詳しいステータス表示
status:
	@echo "=== コンテナ状態 ==="
	docker-compose ps
	@echo ""
	@echo "=== イメージ情報 ==="
	docker images | grep video-watch-list || echo "イメージが見つかりません"
	@echo ""
	@echo "=== ディスク使用量 ==="
	docker system df | head -5

# バックエンドコンテナにシェルで接続
shell-backend:
	docker-compose exec backend sh

# フロントエンドコンテナにシェルで接続
shell-frontend:
	docker-compose exec frontend sh

# サービスの再起動
restart:
	@echo "サービスを再起動中..."
	docker-compose restart

# 環境と設定ファイルの検証
validate:
	@echo "docker-compose ファイルを検証中..."
	docker-compose config > /dev/null && echo "✓ docker-compose.yml: OK" || echo "✗ docker-compose.yml: エラー"
	@if [ -f .env ]; then echo "✓ .env: 存在"; else echo "⚠ .env: 見つかりません"; fi
	@if [ -d backend/src ]; then echo "✓ backend/src: OK"; else echo "✗ backend/src: 見つかりません"; fi
	@if [ -d frontend/src ]; then echo "✓ frontend/src: OK"; else echo "✗ frontend/src: 見つかりません"; fi
	@echo ""
	@echo "検証完了"
