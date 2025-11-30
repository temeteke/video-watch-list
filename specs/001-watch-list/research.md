# Research: 視聴予定リスト

**Feature**: 001-watch-list
**Date**: 2025-11-20
**Purpose**: Technical Context の NEEDS CLARIFICATION 項目を解決し、技術選定と設計方針を明確化

---

## 1. Java バージョン選定

### Decision
**Java 17** を採用

### Rationale
- **LTS バージョン**: Java 17 は長期サポート（LTS）バージョンであり、2029年9月まで Oracle のサポートが保証されている
- **Spring Boot 3.x 対応**: Spring Boot 3.x は Java 17+ を最小要件としており、最新の Spring Boot エコシステムを活用できる
- **モダンな言語機能**: Records, Pattern Matching, Sealed Classes など、ドメインモデル実装に有用な機能が利用可能
- **パフォーマンス**: Java 8/11 と比較して GC の改善、起動時間の短縮など、パフォーマンス向上が期待できる

### Alternatives Considered
- **Java 21（最新 LTS）**: より新しい機能が利用可能だが、エコシステムの成熟度とライブラリ互換性を考慮し、Java 17 を選択
- **Java 11（旧 LTS）**: 安定性は高いが、Spring Boot 3.x が Java 17+ を要求するため不適切

---

## 2. TypeScript バージョン選定

### Decision
**TypeScript 5.3** を採用

### Rationale
- **最新安定版**: TypeScript 5.3 は 2024年初頭時点での最新安定版であり、Next.js 14+ との互換性が確認されている
- **型安全性の向上**: Import Attributes, `satisfies` operator の改善など、型安全性を高める機能が充実
- **Next.js 互換性**: Next.js 14 公式ドキュメントで推奨されているバージョン
- **パフォーマンス**: TypeScript 5.x 系はコンパイル速度が大幅に改善されている

### Alternatives Considered
- **TypeScript 4.x**: 安定性は高いが、型推論の改善や新機能を活用するために 5.x を選択

---

## 3. PostgreSQL スキーマ設計とマイグレーション戦略

### Decision
**Flyway による宣言的マイグレーション** を採用

### Schema Design Principles
1. **正規化**: 第3正規形を基本とし、階層構造（Title → Series → Episode）を明確に表現
2. **外部キー制約**: CASCADE DELETE により、親エンティティ削除時に子エンティティも自動削除
3. **TIMESTAMPTZ**: 作成日時・更新日時は `TIMESTAMPTZ` 型で UTC 管理
4. **JSONB**: URL リストは `JSONB` 型で格納し、柔軟な配列管理を実現
5. **インデックス**: タイトル名検索、視聴状態フィルタに対してインデックスを作成

### Migration Strategy
- **Flyway**: SQL ベースのマイグレーションツールとして Flyway を採用
  - バージョン管理: `V1__initial_schema.sql`, `V2__add_viewing_status.sql` などのファイル命名規則
  - 冪等性: マイグレーションスクリプトは再実行しても安全なように設計
  - ロールバック: 必要に応じて `Undo` マイグレーションを作成（`U1__undo_initial_schema.sql` など）
- **開発フロー**: ローカル開発では docker compose で PostgreSQL コンテナを起動し、Flyway がマイグレーションを自動実行

### Rationale
- **Flyway の優位性**: SQL ベースで明示的、MyBatis との親和性が高い
- **Alternatives**: Liquibase も検討したが、XML/YAML 記法よりも SQL が直感的で、チーム学習コストが低い

---

## 4. E2E テストツール選定

### Decision
**Playwright** を採用

### Rationale
- **モダンな E2E フレームワーク**: Microsoft 開発、クロスブラウザ対応（Chromium, Firefox, WebKit）
- **Next.js 公式推奨**: Next.js 公式ドキュメントで Playwright が推奨されている
- **パフォーマンス**: Selenium と比較して起動速度・テスト実行速度が高速
- **TypeScript 統合**: TypeScript ファーストで、フロントエンドのコードベースとの親和性が高い
- **自動待機**: 要素の表示待機が自動化されており、flaky test が発生しにくい

### Alternatives Considered
- **Cypress**: 人気のある E2E ツールだが、クロスブラウザ対応が Playwright より劣る
- **Selenium**: 成熟しているが、セットアップが複雑で、実行速度が遅い

### Test Scope
- ユーザーストーリー全体（タイトル登録 → シリーズ追加 → エピソード追加 → 視聴完了 → 検索）を E2E テストでカバー
- 初期実装では P1 ユーザーストーリー（Story 1, 2）を優先的にカバー

---

## 5. パフォーマンス目標の具体化

### Decision
以下のパフォーマンス目標を設定：

| 指標 | 目標値 | 理由 |
|------|--------|------|
| 同時接続ユーザー数 | 10 ユーザー | 初期リリースは個人利用を想定、将来的に拡張 |
| リクエスト/秒 | 100 req/s | 10ユーザー × 平均10リクエスト/秒の余裕を持った設定 |
| レスポンスタイム（API） | p95 < 200ms | REST API のレスポンス時間。検索・フィルタを含む |
| レスポンスタイム（ページ表示） | p95 < 1秒 | フロントエンドのページ表示時間。Success Criteria SC-003 に準拠 |
| データベースクエリ | p95 < 50ms | 単一クエリの実行時間 |

### Rationale
- **個人利用ベース**: 初期リリースは個人または小規模チーム利用を想定し、過度なスケーラビリティ要求は避ける
- **拡張性**: 将来的に同時接続ユーザー数を100+に拡張できるよう、アーキテクチャは疎結合を維持
- **Success Criteria 準拠**: SC-003「検索・フィルタは1秒以内」を p95 < 1秒 として定量化

---

## 6. メモリ制約とリソース管理

### Decision
以下のメモリ制約を設定：

| コンポーネント | メモリ制約 | 理由 |
|----------------|------------|------|
| Backend (Spring Boot) | 最大 512MB | docker compose での開発環境リソース効率化 |
| Frontend (Next.js) | 最大 256MB | 開発環境での軽量化、本番環境では増加可 |
| PostgreSQL | 最大 256MB | 開発環境での軽量化、本番環境では増加可 |

### Rationale
- **開発環境最適化**: docker compose で複数コンテナを起動するため、各コンテナのメモリ使用量を抑える
- **本番環境での拡張**: 本番環境では、必要に応じてメモリ上限を引き上げる（Backend: 1GB+, DB: 1GB+）
- **JVM 設定**: Spring Boot アプリケーションは `-Xmx512m` で起動し、GC チューニングは初期段階では不要

---

## 7. データボリュームと想定ユーザー数

### Decision
以下のスケール/スコープを設定：

| 指標 | 初期目標 | 拡張目標 | 理由 |
|------|----------|----------|------|
| 想定ユーザー数 | 1-10 ユーザー | 100+ ユーザー | 個人利用から小規模チーム利用への拡張を想定 |
| タイトル数 | 500 タイトル | 1000+ タイトル | Success Criteria SC-004 に準拠 |
| 1タイトルあたりのシリーズ数 | 平均 2-3 シリーズ | 最大 20 シリーズ | アニメ・ドラマの複数シーズン対応 |
| 1シリーズあたりのエピソード数 | 平均 12 エピソード | 最大 100 エピソード | 長編アニメ・ドラマ対応 |
| 1エピソードあたりの視聴履歴数 | 平均 1-2 回 | 最大 10 回 | 複数回視聴の記録 |
| 1エピソードあたりの視聴ページURL数 | 平均 2-3 件 | 最大 10 件 | 複数配信サービス対応 |

### Rationale
- **Success Criteria 準拠**: SC-004「最小限500個のタイトル、各タイトルあたり複数のシリーズ・エピソード、複数のURL」を具体化
- **現実的なデータボリューム**: 個人利用で500タイトル、拡張時に1000+タイトルは現実的な範囲
- **パフォーマンステスト**: 1000タイトル × 3シリーズ × 12エピソード = 36,000 エピソードでパフォーマンステストを実施

---

## 8. DDD ドメインモデルのベストプラクティス

### Research Focus
DDD + オニオンアーキテクチャにおけるドメインモデル設計のベストプラクティスを調査

### Key Findings
1. **エンティティの識別性**: Title, Series, Episode, ViewingRecord は ID により識別されるエンティティ
2. **値オブジェクト**: URL は値オブジェクトとして扱い、不変性を保証
3. **集約ルート**: Title を集約ルートとし、Series, Episode, ViewingRecord は Title 経由でアクセス
4. **ドメインサービス**: 重複チェック（同一タイトル名）などのビジネスルールはドメインサービスで実装
5. **リポジトリパターン**: domain/repository はインターフェース定義のみ、infrastructure/persistence で MyBatis 実装

### Application to This Feature
- **Title 集約**: Title, Series, Episode, ViewingRecord を1つの集約として扱い、整合性を保証
- **不変性**: ViewingRecord の評価・感想は一度記録したら変更不可（ただし削除は可能）
- **ユビキタス言語**: 日本語（仕様書）↔ 英語（コード）の用語対応を data-model.md で定義

---

## 9. MyBatis + PostgreSQL 統合のベストプラクティス

### Research Focus
MyBatis と PostgreSQL の統合におけるベストプラクティス、特に階層構造のマッピング方法

### Key Findings
1. **1対多マッピング**: MyBatis の `<collection>` タグで Title → Series → Episode の階層構造をマッピング
2. **JSONB 活用**: URL リストを JSONB 型で格納し、MyBatis のカスタム TypeHandler で Java List とマッピング
3. **N+1 問題回避**: JOIN クエリで関連エンティティを一括取得し、N+1 問題を回避
4. **動的クエリ**: MyBatis Dynamic SQL で検索・フィルタ条件を柔軟に構築
5. **トランザクション管理**: Spring の `@Transactional` で宣言的トランザクション管理

### Application to This Feature
- **階層構造マッピング**: `TitleMapper.xml` で Title, Series, Episode, ViewingRecord を1クエリで取得
- **JSONB TypeHandler**: カスタム TypeHandler で URL リスト（`List<String>`）と JSONB を相互変換
- **検索最適化**: タイトル名検索、視聴状態フィルタに対してインデックスを作成し、クエリパフォーマンスを最適化

---

## 10. Next.js + React のベストプラクティス

### Research Focus
Next.js App Router + React における階層構造 UI の実装ベストプラクティス

### Key Findings
1. **Server Components**: Next.js 14 の Server Components をデフォルトで使用し、クライアント側の JavaScript を削減
2. **Client Components**: 状態管理が必要な箇所のみ `'use client'` で Client Components を使用
3. **データフェッチング**: Server Components で `fetch()` を使用し、API からデータを取得
4. **動的ルーティング**: `app/titles/[id]/page.tsx` で動的なタイトル詳細ページを実装
5. **状態管理**: React Context または Zustand で軽量な状態管理を実現

### Application to This Feature
- **階層構造 UI**: Title → Series → Episode の階層を Accordion または Expandable List で表現
- **条件付き表示**: シリーズ数・エピソード数に応じて、レイヤーを動的に非表示（CSS `display: none` ではなく、コンポーネント自体をレンダリングしない）
- **Optimistic UI**: 視聴状態変更時に、サーバーレスポンスを待たずに UI を更新し、UX を向上

---

## Summary

すべての NEEDS CLARIFICATION 項目を解決し、技術選定と設計方針を明確化しました：

| 項目 | 決定事項 |
|------|----------|
| Java バージョン | Java 17（LTS） |
| TypeScript バージョン | TypeScript 5.3 |
| DB スキーマ設計 | 正規化 + JSONB + 外部キー制約 + CASCADE DELETE |
| マイグレーション | Flyway（SQL ベース） |
| E2E テスト | Playwright |
| 同時接続ユーザー数 | 初期: 10 ユーザー、拡張: 100+ ユーザー |
| リクエスト/秒 | 100 req/s |
| レスポンスタイム | API: p95 < 200ms, ページ: p95 < 1秒 |
| メモリ制約 | Backend: 512MB, Frontend: 256MB, DB: 256MB（開発環境） |
| データボリューム | 初期: 500 タイトル、拡張: 1000+ タイトル |
| 想定ユーザー数 | 初期: 1-10 ユーザー、拡張: 100+ ユーザー |

次のステップ（Phase 1）では、これらの決定事項を基に data-model.md, contracts/, quickstart.md を生成します。
