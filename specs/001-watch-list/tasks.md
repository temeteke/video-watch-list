# Implementation Tasks: 視聴予定リスト

**Feature**: 001-watch-list
**Branch**: `001-watch-list`
**Date**: 2025-11-20
**Status**: Ready for Implementation

**Spec**: [spec.md](./spec.md) | **Plan**: [plan.md](./plan.md) | **Data Model**: [data-model.md](./data-model.md) | **API Contract**: [contracts/openapi.yaml](./contracts/openapi.yaml)

---

## Overview

このドキュメントは、「視聴予定リスト」機能の実装タスクを定義します。TDD（テスト駆動開発）の Red-Green-Refactor サイクルに従い、各ユーザーストーリーを独立してテスト可能な単位で実装します。

### 実装戦略

- **MVP優先**: User Story 1（P1）を最優先で実装し、動作するMVPを早期に提供
- **独立性**: 各ユーザーストーリーは独立してテスト・デプロイ可能
- **TDD原則**: テストファースト（Red → Green → Refactor）
- **並列性**: 依存関係のないタスクは並列実行可能（[P]マーク付き）

### ユーザーストーリーの優先順位

| Priority | User Story | 説明 | 独立テスト可能性 |
|----------|------------|------|------------------|
| **P1** | User Story 1 | タイトルと複数シリーズを登録する | ✅ 独立MVP |
| **P1** | User Story 2 | 視聴状態と視聴履歴を管理する | ✅ Story 1 に依存 |
| **P2** | User Story 3 | リストを検索・フィルタする | ✅ Story 1-2 に依存 |

---

## Phase 1: Setup & Infrastructure (準備)

このフェーズでは、プロジェクトの初期化とインフラ設定を行います。

### Backend Setup

- [x] T001 プロジェクト構造を作成: backend/, frontend/, docker-compose.yml, .env.example
- [x] T002 [P] backend/pom.xml を作成し、依存関係を定義（Spring Boot 3.x, MyBatis, PostgreSQL Driver, Flyway, JUnit 5）
- [x] T003 [P] backend/src/main/resources/application.yml を作成し、DB接続・Flyway・JVM設定を定義
- [x] T004 [P] backend/src/main/java/com/example/videowatchlog/VideoWatchLogApplication.java を作成（Spring Boot エントリーポイント）

### Frontend Setup

- [x] T005 [P] frontend/package.json を作成し、依存関係を定義（Next.js 14, React 18, TypeScript 5.3, Jest, React Testing Library, Playwright）
- [x] T006 [P] frontend/tsconfig.json を作成し、TypeScript設定を定義
- [x] T007 [P] frontend/next.config.js を作成し、Next.js設定を定義（API_URL環境変数）
- [x] T008 [P] frontend/src/app/layout.tsx を作成（共通レイアウト）
- [x] T009 [P] frontend/src/lib/api/client.ts を作成（API クライアントのベース実装）

### Database Setup

- [x] T010 [P] backend/src/main/resources/db/migration/V1__initial_schema.sql を作成（titles, series, episodes, viewing_records, title_info_urls, watch_page_urls テーブル定義）

### Docker Compose Setup

- [x] T011 docker-compose.yml を作成（backend, frontend, PostgreSQL コンテナ定義）
- [x] T012 .env.example を作成（環境変数テンプレート: POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB, SPRING_PROFILES_ACTIVE, NEXT_PUBLIC_API_URL）

### Testing Setup

- [x] T013 [P] backend/src/test/resources/application-test.yml を作成（テスト用 DB 設定）
- [x] T014 [P] frontend/jest.config.js を作成（Jest設定）
- [x] T015 [P] frontend/playwright.config.ts を作成（Playwright設定）

---

## Phase 2: Foundational Components (基盤)

このフェーズでは、すべてのユーザーストーリーで共通利用される基盤コンポーネントを実装します。

### Domain Layer Foundation (Backend)

- [x] T016 backend/src/main/java/com/example/videowatchlog/domain/model/WatchStatus.java を作成（列挙型: UNWATCHED, WATCHED）
- [x] T017 [P] backend/src/test/java/com/example/videowatchlog/domain/model/TitleTest.java を作成（Title エンティティのテスト: 作成、バリデーション、集約ルート動作）
- [x] T018 backend/src/main/java/com/example/videowatchlog/domain/model/Title.java を作成（Title エンティティ: id, name, titleInfoUrls, series, createdAt, updatedAt）
- [x] T019 [P] backend/src/test/java/com/example/videowatchlog/domain/model/SeriesTest.java を作成（Series エンティティのテスト）
- [x] T020 backend/src/main/java/com/example/videowatchlog/domain/model/Series.java を作成（Series エンティティ: id, titleId, name, episodes, createdAt, updatedAt）
- [x] T021 [P] backend/src/test/java/com/example/videowatchlog/domain/model/EpisodeTest.java を作成（Episode エンティティのテスト: 視聴状態遷移を含む）
- [x] T022 backend/src/main/java/com/example/videowatchlog/domain/model/Episode.java を作成（Episode エンティティ: id, seriesId, episodeInfo, watchPageUrls, watchStatus, viewingRecords, createdAt, updatedAt）
- [x] T023 [P] backend/src/test/java/com/example/videowatchlog/domain/model/ViewingRecordTest.java を作成（ViewingRecord エンティティのテスト: 不変性確認）
- [x] T024 backend/src/main/java/com/example/videowatchlog/domain/model/ViewingRecord.java を作成（ViewingRecord エンティティ: id, episodeId, watchedAt, rating, comment, recordedAt）

### Value Objects (Backend)

- [x] T025 [P] backend/src/test/java/com/example/videowatchlog/domain/model/TitleInfoUrlTest.java を作成（TitleInfoUrl 値オブジェクトのテスト）
- [x] T026 backend/src/main/java/com/example/videowatchlog/domain/model/TitleInfoUrl.java を作成（TitleInfoUrl 値オブジェクト: url, バリデーション）
- [x] T027 [P] backend/src/test/java/com/example/videowatchlog/domain/model/WatchPageUrlTest.java を作成（WatchPageUrl 値オブジェクトのテスト）
- [x] T028 backend/src/main/java/com/example/videowatchlog/domain/model/WatchPageUrl.java を作成（WatchPageUrl 値オブジェクト: url, バリデーション）

### Repository Interfaces (Backend)

- [x] T029 backend/src/main/java/com/example/videowatchlog/domain/repository/TitleRepository.java を作成（インターフェース: findById, findAll, save, delete, existsByName）
- [x] T030 [P] backend/src/main/java/com/example/videowatchlog/domain/repository/SeriesRepository.java を作成（インターフェース: findById, findByTitleId, save, delete）
- [x] T031 [P] backend/src/main/java/com/example/videowatchlog/domain/repository/EpisodeRepository.java を作成（インターフェース: findById, findBySeriesId, save, delete）
- [x] T032 [P] backend/src/main/java/com/example/videowatchlog/domain/repository/ViewingRecordRepository.java を作成（インターフェース: findById, findByEpisodeId, save, delete）

### Domain Services (Backend)

- [x] T033 [P] backend/src/test/java/com/example/videowatchlog/domain/service/TitleDuplicationCheckServiceTest.java を作成（タイトル重複チェックサービスのテスト）
- [x] T034 backend/src/main/java/com/example/videowatchlog/domain/service/TitleDuplicationCheckService.java を作成（isDuplicate メソッド: 大文字小文字区別なし完全一致チェック）

### Infrastructure Layer - MyBatis Mappers (Backend)

- [x] T035 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/TitleMapperTest.java を作成（TitleMapper の統合テスト: @MybatisTest使用）
- [x] T036 backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/TitleMapper.java を作成（MyBatis Mapper インターフェース）
- [x] T037 backend/src/main/resources/mybatis/mapper/TitleMapper.xml を作成（SQL マッピング: JOIN で階層構造取得、URL重複削除）
- [x] T038 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/SeriesMapperTest.java を作成
- [x] T039 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/SeriesMapper.java を作成
- [x] T040 [P] backend/src/main/resources/mybatis/mapper/SeriesMapper.xml を作成
- [x] T041 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/EpisodeMapperTest.java を作成
- [x] T042 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/EpisodeMapper.java を作成
- [x] T043 [P] backend/src/main/resources/mybatis/mapper/EpisodeMapper.xml を作成
- [x] T044 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/ViewingRecordMapperTest.java を作成
- [x] T045 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/ViewingRecordMapper.java を作成
- [x] T046 [P] backend/src/main/resources/mybatis/mapper/ViewingRecordMapper.xml を作成

### Repository Implementations (Backend)

- [x] T047 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImplTest.java を作成（リポジトリ実装の統合テスト）
- [x] T048 backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImpl.java を作成（@Repository, TitleMapper を使用）
- [x] T049 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/SeriesRepositoryImpl.java を作成
- [x] T050 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/EpisodeRepositoryImpl.java を作成
- [x] T051 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/ViewingRecordRepositoryImpl.java を作成

### DTOs (Backend)

- [x] T052 backend/src/main/java/com/example/videowatchlog/application/dto/TitleSummaryDTO.java を作成（id, name, createdAt, updatedAt）
- [x] T053 [P] backend/src/main/java/com/example/videowatchlog/application/dto/TitleDetailDTO.java を作成（階層構造すべて含む）
- [x] T054 [P] backend/src/main/java/com/example/videowatchlog/application/dto/CreateTitleRequestDTO.java を作成
- [x] T055 [P] backend/src/main/java/com/example/videowatchlog/application/dto/UpdateTitleRequestDTO.java を作成
- [x] T056 [P] backend/src/main/java/com/example/videowatchlog/application/dto/SeriesDetailDTO.java を作成
- [x] T057 [P] backend/src/main/java/com/example/videowatchlog/application/dto/CreateSeriesRequestDTO.java を作成
- [x] T058 [P] backend/src/main/java/com/example/videowatchlog/application/dto/UpdateSeriesRequestDTO.java を作成
- [x] T059 [P] backend/src/main/java/com/example/videowatchlog/application/dto/EpisodeDetailDTO.java を作成
- [x] T060 [P] backend/src/main/java/com/example/videowatchlog/application/dto/CreateEpisodeRequestDTO.java を作成
- [x] T061 [P] backend/src/main/java/com/example/videowatchlog/application/dto/UpdateEpisodeRequestDTO.java を作成
- [x] T062 [P] backend/src/main/java/com/example/videowatchlog/application/dto/CompleteEpisodeRequestDTO.java を作成
- [x] T063 [P] backend/src/main/java/com/example/videowatchlog/application/dto/ViewingRecordDetailDTO.java を作成
- [x] T064 [P] backend/src/main/java/com/example/videowatchlog/application/dto/CreateViewingRecordRequestDTO.java を作成
- [x] T065 [P] backend/src/main/java/com/example/videowatchlog/application/dto/ErrorResponseDTO.java を作成（code, message, details）

### Frontend Type Definitions

- [x] T066 frontend/src/types/title.ts を作成（TitleSummary, TitleDetail, CreateTitleRequest, UpdateTitleRequest）
- [x] T067 [P] frontend/src/types/series.ts を作成（SeriesDetail, CreateSeriesRequest, UpdateSeriesRequest）
- [x] T068 [P] frontend/src/types/episode.ts を作成（EpisodeDetail, CreateEpisodeRequest, UpdateEpisodeRequest, CompleteEpisodeRequest, WatchStatus）
- [x] T069 [P] frontend/src/types/viewing-record.ts を作成（ViewingRecordDetail, CreateViewingRecordRequest）
- [x] T070 [P] frontend/src/types/error.ts を作成（ErrorResponse）

---

## Phase 3: User Story 1 - タイトルと複数シリーズを登録する (P1) 🎯 MVP

このフェーズでは、最優先のユーザーストーリー「タイトルと複数シリーズを登録する」を実装します。これがMVPとして機能します。

### Goal
ユーザーは、タイトル・シリーズ・エピソードを階層構造で登録し、UI上では階層の深さに応じてレイヤーを動的に非表示にすることでUXを最適化します。

### Independent Test Criteria
- ✅ タイトルを作成し、デフォルトシリーズ・デフォルトエピソードが自動生成されること
- ✅ 単一シリーズ作品を登録し、UI上でシリーズレイヤーが非表示になること
- ✅ シリーズを追加し、UI上でシリーズレイヤーが表示されること
- ✅ エピソードを追加し、視聴ページURLを登録できること
- ✅ タイトル情報URL（Wikipedia等）を登録できること
- ✅ 同一タイトル名の重複登録が拒否されること

### Backend - Application Layer (User Story 1)

- [x] T071 [US1] backend/src/test/java/com/example/videowatchlog/application/usecase/CreateTitleUseCaseTest.java を作成（TDD: タイトル作成のテスト - デフォルトシリーズ・エピソード自動生成、重複チェック）
- [x] T072 [US1] backend/src/main/java/com/example/videowatchlog/application/usecase/CreateTitleUseCase.java を作成（execute メソッド: TitleDuplicationCheckService 使用、デフォルトシリーズ・エピソード自動生成）
- [x] T073 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/GetTitleDetailUseCaseTest.java を作成
- [x] T074 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/GetTitleDetailUseCase.java を作成
- [x] T075 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/UpdateTitleUseCaseTest.java を作成
- [x] T076 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/UpdateTitleUseCase.java を作成
- [x] T077 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/DeleteTitleUseCaseTest.java を作成（カスケード削除テスト）
- [x] T078 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/DeleteTitleUseCase.java を作成
- [x] T079 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/CreateSeriesUseCaseTest.java を作成（2つ目のシリーズ追加時のロジックテスト）
- [x] T080 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/CreateSeriesUseCase.java を作成
- [x] T081 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/UpdateSeriesUseCaseTest.java を作成
- [x] T082 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/UpdateSeriesUseCase.java を作成
- [x] T083 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/DeleteSeriesUseCaseTest.java を作成（シリーズ削除後の動的UI切り替えロジックテスト）
- [x] T084 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/DeleteSeriesUseCase.java を作成
- [x] T085 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/CreateEpisodeUseCaseTest.java を作成
- [x] T086 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/CreateEpisodeUseCase.java を作成
- [x] T087 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/UpdateEpisodeUseCaseTest.java を作成（URL重複削除テスト）
- [x] T088 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/UpdateEpisodeUseCase.java を作成
- [x] T089 [US1] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/DeleteEpisodeUseCaseTest.java を作成
- [x] T090 [US1] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/DeleteEpisodeUseCase.java を作成

### Backend - Presentation Layer (User Story 1)

- [x] T091 [US1] backend/src/test/java/com/example/videowatchlog/integration/TitleControllerIntegrationTest.java を作成（統合テスト: POST /titles, GET /titles/{id}, PUT /titles/{id}, DELETE /titles/{id}）
- [x] T092 [US1] backend/src/main/java/com/example/videowatchlog/presentation/controller/TitleController.java を作成（@RestController, @RequestMapping("/api/v1/titles")）
- [x] T093 [US1] [P] backend/src/test/java/com/example/videowatchlog/integration/SeriesControllerIntegrationTest.java を作成
- [x] T094 [US1] [P] backend/src/main/java/com/example/videowatchlog/presentation/controller/SeriesController.java を作成
- [x] T095 [US1] [P] backend/src/test/java/com/example/videowatchlog/integration/EpisodeControllerIntegrationTest.java を作成
- [x] T096 [US1] [P] backend/src/main/java/com/example/videowatchlog/presentation/controller/EpisodeController.java を作成

### Frontend - API Client (User Story 1)

- [x] T097 [US1] frontend/src/tests/unit/api/titles.test.ts を作成（API クライアントのテスト: createTitle, getTitleDetail, updateTitle, deleteTitle）
- [x] T098 [US1] frontend/src/lib/api/titles.ts を作成（API クライアント実装: fetch使用）
- [x] T099 [US1] [P] frontend/src/tests/unit/api/series.test.ts を作成
- [x] T100 [US1] [P] frontend/src/lib/api/series.ts を作成
- [x] T101 [US1] [P] frontend/src/tests/unit/api/episodes.test.ts を作成
- [x] T102 [US1] [P] frontend/src/lib/api/episodes.ts を作成

### Frontend - Components (User Story 1)

- [x] T103 [US1] frontend/src/tests/unit/components/title/TitleForm.test.tsx を作成（タイトル作成・編集フォームのテスト）
- [x] T104 [US1] frontend/src/components/title/TitleForm.tsx を作成（タイトル名、タイトル情報URL入力フォーム）
- [x] T105 [US1] [P] frontend/src/tests/unit/components/title/TitleList.test.tsx を作成
- [x] T106 [US1] [P] frontend/src/components/title/TitleList.tsx を作成（タイトル一覧表示）
- [x] T107 [US1] [P] frontend/src/tests/unit/components/series/SeriesForm.test.tsx を作成
- [x] T108 [US1] [P] frontend/src/components/series/SeriesForm.tsx を作成
- [x] T109 [US1] [P] frontend/src/tests/unit/components/episode/EpisodeForm.test.tsx を作成
- [x] T110 [US1] [P] frontend/src/components/episode/EpisodeForm.tsx を作成
- [x] T111 [US1] [P] frontend/src/tests/unit/components/common/HierarchicalView.test.tsx を作成（階層表示コンポーネントのテスト: 動的レイヤー非表示ロジック）
- [x] T112 [US1] [P] frontend/src/components/common/HierarchicalView.tsx を作成（シリーズ数・エピソード数に応じた条件付き非表示ロジック）

### Frontend - Pages (User Story 1)

- [x] T113 [US1] frontend/src/tests/integration/app/page.test.tsx を作成（トップページ統合テスト）
- [x] T114 [US1] frontend/src/app/page.tsx を作成（トップページ: タイトル一覧表示）
- [x] T115 [US1] [P] frontend/src/tests/integration/app/titles/new/page.test.tsx を作成
- [x] T116 [US1] [P] frontend/src/app/titles/new/page.tsx を作成（タイトル作成ページ）
- [x] T117 [US1] [P] frontend/src/tests/integration/app/titles/[id]/page.test.tsx を作成
- [x] T118 [US1] [P] frontend/src/app/titles/[id]/page.tsx を作成（タイトル詳細ページ: シリーズ・エピソード階層表示、シリーズ追加ボタン）

### E2E Tests (User Story 1)

- [x] T119 [US1] frontend/tests/e2e/user-story-1.spec.ts を作成（E2E テスト: Acceptance Scenario 1-7 をカバー）

---

## Phase 4: User Story 2 - 視聴状態と視聴履歴を管理する (P1)

このフェーズでは、視聴状態の管理と複数回視聴履歴の記録機能を実装します。

### Goal
ユーザーは、エピソードを「未視聴」から「視聴済み」に変更し、視聴完了日時・評価・感想を記録できます。同じエピソードの複数回視聴履歴も記録できます。

### Independent Test Criteria
- ✅ エピソードを「未視聴」から「視聴済み」に変更できること
- ✅ 視聴完了時に日時・評価（1-5）・感想を記録できること
- ✅ 視聴済みエピソードに複数回の視聴履歴を追加できること
- ✅ 視聴履歴が新しい順（降順）で表示されること
- ✅ すべての視聴履歴を削除すると、エピソードが「未視聴」に戻ること
- ✅ 一度「視聴済み」になったエピソードは状態変更不可（視聴履歴削除時を除く）

### Backend - Application Layer (User Story 2)

- [x] T120 [US2] backend/src/test/java/com/example/videowatchlog/application/usecase/CompleteEpisodeUseCaseTest.java を作成（TDD: 視聴完了のテスト - UNWATCHED → WATCHED遷移、ViewingRecord作成、状態変更不可の確認）
- [x] T121 [US2] backend/src/main/java/com/example/videowatchlog/application/usecase/CompleteEpisodeUseCase.java を作成（execute メソッド: Episode.watchStatus = WATCHED, ViewingRecord作成）
- [x] T122 [US2] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/AddViewingRecordUseCaseTest.java を作成（複数回視聴追加のテスト）
- [x] T123 [US2] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/AddViewingRecordUseCase.java を作成（WATCHED状態のエピソードにのみ追加可能）
- [x] T124 [US2] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/DeleteViewingRecordUseCaseTest.java を作成（視聴履歴削除のテスト: すべて削除時にUNWATCHED復帰）
- [x] T125 [US2] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/DeleteViewingRecordUseCase.java を作成（削除後、Episode.viewingRecords.isEmpty() なら watchStatus = UNWATCHED）
- [x] T126 [US2] [P] backend/src/test/java/com/example/videowatchlog/application/usecase/GetViewingRecordsUseCaseTest.java を作成（視聴履歴一覧取得のテスト: 新しい順ソート確認）
- [x] T127 [US2] [P] backend/src/main/java/com/example/videowatchlog/application/usecase/GetViewingRecordsUseCase.java を作成（recordedAt DESC でソート）

### Backend - Presentation Layer (User Story 2)

- [x] T128 [US2] backend/src/test/java/com/example/videowatchlog/integration/ViewingRecordControllerIntegrationTest.java を作成（統合テスト: POST /episodes/{id}/complete, POST /episodes/{id}/viewing-records, DELETE /viewing-records/{id}）
- [x] T129 [US2] backend/src/main/java/com/example/videowatchlog/presentation/controller/ViewingRecordController.java を作成（@RestController）

### Frontend - API Client (User Story 2)

- [x] T130 [US2] frontend/src/tests/unit/api/viewing-records.test.ts を作成（API クライアントのテスト: completeEpisode, addViewingRecord, deleteViewingRecord）
- [x] T131 [US2] frontend/src/lib/api/viewing-records.ts を作成（API クライアント実装）

### Frontend - Components (User Story 2)

- [x] T132 [US2] frontend/src/tests/unit/components/episode/CompleteEpisodeForm.test.tsx を作成（視聴完了フォームのテスト: 日時・評価・感想入力）
- [x] T133 [US2] frontend/src/components/episode/CompleteEpisodeForm.tsx を作成（日時ピッカー、評価（1-5）セレクタ、感想テキストエリア）
- [x] T134 [US2] [P] frontend/src/tests/unit/components/episode/ViewingRecordList.test.tsx を作成（視聴履歴一覧のテスト: 新しい順表示確認）
- [x] T135 [US2] [P] frontend/src/components/episode/ViewingRecordList.tsx を作成（視聴履歴一覧表示、削除ボタン）
- [x] T136 [US2] [P] frontend/src/tests/unit/components/episode/AddViewingRecordForm.test.tsx を作成
- [x] T137 [US2] [P] frontend/src/components/episode/AddViewingRecordForm.tsx を作成（追加視聴記録フォーム）

### Frontend - Pages (User Story 2)

- [x] T138 [US2] frontend/src/tests/integration/app/episodes/[id]/page.test.tsx を作成（エピソード詳細ページ統合テスト: 視聴完了、視聴履歴表示）
- [x] T139 [US2] frontend/src/app/episodes/[id]/page.tsx を作成（エピソード詳細ページ: 視聴完了ボタン、視聴履歴一覧）

### E2E Tests (User Story 2)

- [x] T140 [US2] frontend/tests/e2e/user-story-2.spec.ts を作成（E2E テスト: Acceptance Scenario 1-4 をカバー）

---

## Phase 5: User Story 3 - リストを検索・フィルタする (P2)

このフェーズでは、タイトル検索と視聴状態フィルタリング機能を実装します。

### Goal
ユーザーは、タイトル名で部分一致検索し、視聴状態（未視聴/視聴済み）でフィルタできます。

### Independent Test Criteria
- ✅ タイトル名で部分一致検索ができること
- ✅ 「未視聴」でフィルタすると未視聴エピソードを含むタイトルのみ表示されること
- ✅ 「視聴済み」でフィルタすると視聴済みエピソードを含むタイトルのみ表示されること
- ✅ フィルタをクリアするとすべてのタイトルが表示されること
- ✅ 検索・フィルタ結果が1秒以内に表示されること（SC-003）

### Backend - Application Layer (User Story 3)

- [x] T141 [US3] backend/src/test/java/com/example/videowatchlog/application/usecase/SearchTitlesUseCaseTest.java を作成（TDD: タイトル検索のテスト - 部分一致、視聴状態フィルタ、パフォーマンス確認）
- [x] T142 [US3] backend/src/main/java/com/example/videowatchlog/application/usecase/SearchTitlesUseCase.java を作成（execute メソッド: TitleRepository.search(query, watchStatus)）

### Backend - Repository Extension (User Story 3)

- [x] T143 [US3] backend/src/main/java/com/example/videowatchlog/domain/repository/TitleRepository.java に search メソッドを追加（String query, WatchStatus watchStatus）
- [x] T144 [US3] backend/src/main/resources/mybatis/mapper/TitleMapper.xml に search クエリを追加（LIKE検索、watchStatus JOIN、インデックス活用）
- [x] T145 [US3] [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImplTest.java に search テストを追加
- [x] T146 [US3] [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImpl.java に search 実装を追加

### Backend - Presentation Layer (User Story 3)

- [x] T147 [US3] backend/src/test/java/com/example/videowatchlog/integration/TitleControllerIntegrationTest.java に GET /titles?query=&watchStatus= テストを追加
- [x] T148 [US3] backend/src/main/java/com/example/videowatchlog/presentation/controller/TitleController.java に getTitles メソッドを追加（@GetMapping, @RequestParam query, @RequestParam watchStatus）

### Frontend - API Client (User Story 3)

- [x] T149 [US3] frontend/src/tests/unit/api/titles.test.ts に searchTitles テストを追加
- [x] T150 [US3] frontend/src/lib/api/titles.ts に searchTitles 実装を追加（URLSearchParams使用）

### Frontend - Components (User Story 3)

- [x] T151 [US3] frontend/src/tests/unit/components/common/SearchBar.test.tsx を作成（検索バーのテスト: 部分一致入力、フィルタ選択、クリアボタン）
- [x] T152 [US3] frontend/src/components/common/SearchBar.tsx を作成（検索入力、視聴状態フィルタドロップダウン、クリアボタン）

### Frontend - Pages (User Story 3)

- [x] T153 [US3] frontend/src/tests/integration/app/page.test.tsx に検索・フィルタ機能のテストを追加
- [x] T154 [US3] frontend/src/app/page.tsx に SearchBar コンポーネントを統合

### E2E Tests (User Story 3)

- [x] T155 [US3] frontend/tests/e2e/user-story-3.spec.ts を作成（E2E テスト: Acceptance Scenario 1-4 をカバー、パフォーマンス確認）

---

## Phase 6: Polish & Cross-Cutting Concerns (仕上げ)

このフェーズでは、エラーハンドリング、バリデーション、パフォーマンス最適化など、横断的な懸念事項を実装します。

### Error Handling (Backend)

- [x] T156 backend/src/main/java/com/example/videowatchlog/presentation/exception/GlobalExceptionHandler.java を作成（@ControllerAdvice, ErrorResponseDTO生成）
- [x] T157 [P] backend/src/main/java/com/example/videowatchlog/domain/exception/TitleDuplicateException.java を作成
- [x] T158 [P] backend/src/main/java/com/example/videowatchlog/domain/exception/TitleNotFoundException.java を作成
- [x] T159 [P] backend/src/main/java/com/example/videowatchlog/domain/exception/InvalidWatchStatusTransitionException.java を作成

### Validation (Backend)

- [x] T160 backend/src/main/java/com/example/videowatchlog/application/dto/CreateTitleRequestDTO.java に @Valid, @NotBlank, @Size アノテーションを追加
- [x] T161 [P] すべての DTO に適切なバリデーションアノテーションを追加（@Valid, @Min, @Max, @Pattern, @Future）

### Error Handling (Frontend)

- [x] T162 frontend/src/lib/utils/error-handler.ts を作成（API エラーハンドリング、ユーザーフレンドリーなエラーメッセージ変換）
- [x] T163 [P] frontend/src/components/common/ErrorBoundary.tsx を作成（React Error Boundary）
- [x] T164 [P] frontend/src/components/common/Toast.tsx を作成（エラー・成功メッセージ表示用トースト通知）

### Confirmation Dialogs (Frontend)

- [x] T165 frontend/src/components/common/ConfirmDialog.tsx を作成（削除確認ダイアログ: タイトル・シリーズ削除時に使用）

### Performance Optimization

- [x] T166 backend/src/main/resources/mybatis/mapper/TitleMapper.xml のクエリを最適化（N+1問題回避、JOIN最適化）
- [x] T167 [P] frontend/src/lib/utils/debounce.ts を作成（検索入力のデバウンス処理）

### Documentation

- [x] T168 backend/README.md を作成（セットアップ手順、API エンドポイント一覧、テスト実行方法）
- [x] T169 [P] frontend/README.md を作成（セットアップ手順、コンポーネント構造、テスト実行方法）

### Docker & Deployment

- [x] T170 docker-compose.yml を検証し、開発環境で正常に起動することを確認
- [x] T171 [P] .github/workflows/ci.yml を作成（CI/CD パイプライン: テスト自動実行、ビルド確認）

---

---

## Phase 7: Architecture Improvement - CQRS + Independent Aggregates (アーキテクチャ改善)

このフェーズでは、DDD ベストプラクティスに準拠し、N+1 クエリ問題を解決するアーキテクチャ改善を実装します。

### Milestone 1: Write Model の分離

**概要**: Title、Series、Episode を独立した集約に昇格し、オブジェクト参照を ID 参照のみに変更します。

#### Domain Model の修正

- [ ] T172 [P] backend/src/main/java/com/example/videowatchlog/domain/model/Title.java から Series フィールドを削除し、ID 参照のみに修正
- [ ] T173 [P] backend/src/test/java/com/example/videowatchlog/domain/model/TitleTest.java を更新（Series フィールドが存在しないことを検証）
- [ ] T174 [P] backend/src/main/java/com/example/videowatchlog/domain/model/Series.java から Episode フィールドを削除し、ID 参照のみに修正
- [ ] T175 [P] backend/src/test/java/com/example/videowatchlog/domain/model/SeriesTest.java を更新（Episode フィールドが存在しないことを検証）

#### Entity・Repository の修正

- [ ] T176 backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/entity/TitleEntity.java の toDomain() シグネチャを変更（Series パラメータを削除）
- [ ] T177 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/entity/SeriesEntity.java の toDomain() シグネチャを変更（Episode パラメータを削除）
- [ ] T178 backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImpl.java を修正（Series をロードしない、N+1 を 51 クエリに削減）
- [ ] T179 [P] backend/src/main/java/com/example/videowatchlog/infrastructure/persistence/SeriesRepositoryImpl.java を修正（Episode をロードしない）
- [ ] T180 [P] backend/src/test/java/com/example/videowatchlog/infrastructure/persistence/TitleRepositoryImplTest.java を更新

#### UseCase の簡潔化

- [ ] T181 backend/src/main/java/com/example/videowatchlog/application/usecase/CreateSeriesUseCase.java を修正（Title をロードしない、Series のみ保存）
- [ ] T182 [P] backend/src/test/java/com/example/videowatchlog/application/usecase/CreateSeriesUseCaseTest.java を更新
- [ ] T183 [P] backend/src/main/java/com/example/videowatchlog/application/usecase/CreateEpisodeUseCase.java を修正（Series をロードしない、Episode のみ保存）
- [ ] T184 [P] backend/src/test/java/com/example/videowatchlog/application/usecase/CreateEpisodeUseCaseTest.java を更新

### Milestone 2: Read Model の追加

**概要**: CQRS パターンを適用し、読み取り専用リードモデルで JOIN を使用して N+1 問題を完全解決します。

#### Read Model クラスの作成

- [ ] T185 backend/src/main/java/com/example/videowatchlog/application/readmodel/TitleListReadModel.java を作成（タイトル一覧用リードモデル）
- [ ] T186 [P] backend/src/main/java/com/example/videowatchlog/application/readmodel/TitleDetailReadModel.java を作成（タイトル詳細用リードモデル）
- [ ] T187 [P] backend/src/main/java/com/example/videowatchlog/application/readmodel/SeriesReadModel.java を作成
- [ ] T188 [P] backend/src/main/java/com/example/videowatchlog/application/readmodel/EpisodeReadModel.java を作成

#### MyBatis リードマッパー の実装

- [ ] T189 backend/src/main/java/com/example/videowatchlog/application/readmodel/mapper/TitleReadMapper.java を作成（MyBatis Mapper インターフェース）
- [ ] T190 backend/src/main/resources/mybatis/mapper/readmodel/TitleReadMapper.xml を作成（JOIN SQL 実装: 1 クエリで全階層取得）
- [ ] T191 [P] backend/src/test/java/com/example/videowatchlog/application/readmodel/mapper/TitleReadMapperTest.java を作成（統合テスト: JOIN が正しく動作することを検証）

#### リードサービス の実装

- [ ] T192 backend/src/main/java/com/example/videowatchlog/application/readmodel/service/TitleReadService.java を作成（getAllTitles, getTitleDetail メソッド）
- [ ] T193 [P] backend/src/test/java/com/example/videowatchlog/application/readmodel/service/TitleReadServiceTest.java を作成（モックテスト）

### Milestone 3: UseCase の修正（Read と Write の分離）

**概要**: GetAllTitles・GetTitleDetail を Read Model に切り替え、データ整合性を向上させます。

#### Read 系 UseCase の修正

- [ ] T194 backend/src/main/java/com/example/videowatchlog/application/usecase/GetAllTitlesUseCase.java を修正（TitleReadService を使用、1 クエリに削減）
- [ ] T195 [P] backend/src/test/java/com/example/videowatchlog/application/usecase/GetAllTitlesUseCaseTest.java を更新（TitleReadService を使用することを検証）
- [ ] T196 backend/src/main/java/com/example/videowatchlog/application/usecase/GetTitleDetailUseCase.java を修正（TitleReadService を使用、1 クエリ（JOIN）に削減）
- [ ] T197 [P] backend/src/test/java/com/example/videowatchlog/application/usecase/GetTitleDetailUseCaseTest.java を更新

#### Controller の API レスポンスが変わらないことを検証

- [ ] T198 backend/src/test/java/com/example/videowatchlog/integration/TitleControllerIntegrationTest.java を更新（API レスポンスが変わらないことを確認）

### Milestone 4: パフォーマンス検証

**概要**: N+1 クエリ削減、全テスト通過、パフォーマンス改善を確認します。

#### パフォーマンス テスト

- [ ] T199 backend/src/test/java/com/example/videowatchlog/performance/N1QueryReductionTest.java を作成（パフォーマンステスト: GetAllTitles で 101 → 1 クエリ削減を確認）
- [ ] T200 [P] backend/src/test/java/com/example/videowatchlog/performance/GetTitleDetailPerformanceTest.java を作成（GetTitleDetail が 1 クエリ（JOIN）で実行されることを確認）

#### 全テスト検証

- [ ] T201 すべてのテスト（140+ テスト）が通過することを確認（`mvn test`）
- [ ] T202 [P] パフォーマンス要件が満たされていることを確認（API p95 < 200ms, ページ p95 < 1秒）

### Milestone 5: ドキュメント更新

**概要**: アーキテクチャ改善をドキュメント反映させます。

#### ドキュメント更新

- [ ] T203 data-model.md の「集約の整合性ルール」セクションを更新（独立集約の説明）
- [ ] T204 [P] plan.md の Phase 3 セクションに実装完了マーク（✅ COMPLETED）を追記
- [ ] T205 [P] backend/README.md の「アーキテクチャ」セクションに CQRS パターンの説明を追加

---

## Dependencies Between User Stories

```
Phase 1 (Setup) → Phase 2 (Foundational)
                    ↓
Phase 3 (User Story 1: P1) 🎯 MVP
                    ↓
Phase 4 (User Story 2: P1)
                    ↓
Phase 5 (User Story 3: P2)
                    ↓
Phase 6 (Polish & Cross-Cutting)
                    ↓
Phase 7 (Architecture Improvement: CQRS + Independent Aggregates)
```

**依存関係の説明**:
- **Phase 1-2**: すべてのユーザーストーリーで共通利用される基盤を構築
- **Phase 3 (User Story 1)**: 独立MVP。タイトル・シリーズ・エピソード登録機能
- **Phase 4 (User Story 2)**: User Story 1 に依存。視聴状態・視聴履歴管理
- **Phase 5 (User Story 3)**: User Story 1-2 に依存。検索・フィルタ機能
- **Phase 6**: 全ユーザーストーリー完了後の仕上げ
- **Phase 7**: 全機能実装完了後のアーキテクチャ改善（スケーラビリティ・パフォーマンス向上）

---

## Parallel Execution Examples

### Phase 2 (Foundational) での並列実行例

**同時実行可能なタスク群**:
```
# グループA: Domain Model テスト・実装
T017 (TitleTest) || T019 (SeriesTest) || T021 (EpisodeTest) || T023 (ViewingRecordTest)
↓
T018 (Title) || T020 (Series) || T022 (Episode) || T024 (ViewingRecord)

# グループB: Value Objects
T025 (TitleInfoUrlTest) || T027 (WatchPageUrlTest)
↓
T026 (TitleInfoUrl) || T028 (WatchPageUrl)

# グループC: Repository Interfaces
T029 (TitleRepository) || T030 (SeriesRepository) || T031 (EpisodeRepository) || T032 (ViewingRecordRepository)

# グループD: MyBatis Mappers（各エンティティ独立）
T035-T037 (TitleMapper) || T038-T040 (SeriesMapper) || T041-T043 (EpisodeMapper) || T044-T046 (ViewingRecordMapper)
```

**推奨実行順序**:
1. グループA（Domain Model）を最優先で完了
2. グループB-Dを並列実行

### Phase 3 (User Story 1) での並列実行例

**同時実行可能なタスク群**:
```
# グループA: Application Layer UseCases（異なるエンティティ）
T073-T074 (GetTitleDetail) || T079-T080 (CreateSeries) || T085-T086 (CreateEpisode)

# グループB: Controllers（異なるエンドポイント）
T091-T092 (TitleController) || T093-T094 (SeriesController) || T095-T096 (EpisodeController)

# グループC: Frontend API Clients
T097-T098 (titles API) || T099-T100 (series API) || T101-T102 (episodes API)

# グループD: Frontend Components
T103-T104 (TitleForm) || T107-T108 (SeriesForm) || T109-T110 (EpisodeForm)
```

---

## Task Execution Checklist

実装時の各タスクは以下の手順で実行してください（TDD原則）：

### For Test Tasks (T0XX with "Test" suffix)

1. **Red**: 失敗するテストを書く
   - Given-When-Then 形式でテストケースを記述
   - アサーションを明確に定義
   - テストを実行し、失敗を確認

2. **Green**: 最小限の実装でテストを通す
   - テストが通る最小限のコードを書く
   - リファクタリングはまだ行わない

3. **Refactor**: コードを改善する
   - 重複を削除
   - 命名を改善
   - テストが引き続き通ることを確認

### For Implementation Tasks (T0XX without "Test" suffix)

1. **実装**: 対応するテスト（T0XX-1）を参照し、仕様通りに実装
2. **テスト実行**: すべてのテストが通ることを確認
3. **コードレビュー**: DDD原則、SOLID原則に準拠しているか確認

---

## Summary

**総タスク数**: 205 タスク（Phase 1-7）

**タスク内訳**:
- Phase 1 (Setup): 15 タスク
- Phase 2 (Foundational): 51 タスク
- Phase 3 (User Story 1 - P1): 49 タスク
- Phase 4 (User Story 2 - P1): 21 タスク
- Phase 5 (User Story 3 - P2): 15 タスク
- Phase 6 (Polish & Cross-Cutting): 20 タスク
- Phase 7 (Architecture Improvement: CQRS + Independent Aggregates): 34 タスク

**完了状況**: Phase 1-6 (171 タスク) は実装済み。Phase 7 (34 タスク) は設計完了、実装待ち

**並列実行可能タスク**:
- Phase 1-6: 約70% のタスクが [P] マーク付き
- Phase 7: 約70% のタスクが [P] マーク付き（Write Model・Read Model の並列開発可能）

**MVP スコープ**: Phase 1-3 (115 タスク) で User Story 1 を完成させ、動作するMVPを提供

**推奨実装順序**:
1. **Sprint 1**: Phase 1-2 (Setup & Foundational) - 基盤構築 ✅ 完了
2. **Sprint 2**: Phase 3 (User Story 1) - MVP リリース ✅ 完了
3. **Sprint 3**: Phase 4 (User Story 2) - 視聴管理機能追加 ✅ 完了
4. **Sprint 4**: Phase 5 (User Story 3) - 検索・フィルタ機能追加 ✅ 完了
5. **Sprint 5**: Phase 6 (Polish & Cross-Cutting) - 仕上げ・リリース準備 ✅ 完了
6. **Sprint 6**: Phase 7 (Architecture Improvement) - CQRS導入・パフォーマンス最適化 🚀 実装開始

**Phase 7 実装の主要改善**:
- **GetAllTitles パフォーマンス**: 101 クエリ → 1 クエリ（99% 削減）
- **GetTitleDetail パフォーマンス**: 2-3 クエリ → 1 クエリ（50-66% 削減）
- **アーキテクチャ改善**: CQRS パターン導入、独立した集約への分割
- **スケーラビリティ向上**: 読み取りモデル最適化により大規模データセット対応

---

**次のステップ**: `/speckit.implement` コマンドを実行するか、Phase 1 の T001 から手動実装を開始してください。
