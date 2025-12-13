# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Video Watch List** is a web application for managing drama, anime, and movie watch lists. Users can organize titles hierarchically (Title → Series → Episode), track viewing status, record viewing history with ratings and comments, and search/filter by title or watch status.

- **Backend**: Spring Boot 3.2.0 + MyBatis + PostgreSQL (Java 17)
- **Frontend**: Next.js 14 + React 18 + TypeScript 5.3
- **Testing**: JUnit 5 (Backend), Jest + React Testing Library (Frontend), Playwright (E2E)
- **Database**: PostgreSQL 15 with Flyway migrations
- **Infrastructure**: Docker Compose (dev/prod environments)
- **Architecture**: Domain-Driven Design (DDD) + Onion Architecture + TDD

## Project Governance

This project follows the principles defined in `.specify/memory/constitution.md` (v1.2.0), which is the highest-level design document and takes precedence over all other documentation.

### Core Principles

1. **Specification-Driven Development**: All features follow **spec → plan → tasks → implementation** flow (Spec Kit framework)
2. **DDD + Onion Architecture**: Backend uses Domain-Driven Design with strict dependency inversion (inner layers never depend on outer layers)
3. **Test-First Approach**: TDD (Red-Green-Refactor) is mandatory for all features; 80% code coverage goal for business logic
4. **Simplicity Over Complexity**: Avoid over-engineering; prioritize maintainability and understandability
5. **Language Convention**:
   - **Documentation**: Japanese (specs, requirements, design docs)
   - **Code**: English (class names, methods, variables, comments)
   - **Ubiquitous Language**: Domain terms mapped explicitly in spec.md (e.g., "作品" = "Title", "視聴ステータス" = "WatchStatus")
6. **Docker Compose Mandatory**: Development environment **must** use Docker Compose (no local OS-dependent setup)

For detailed governance rules, version history, and compliance requirements, see `.specify/memory/constitution.md`.

## Project Structure

```
video-watch-list/
├── backend/
│   ├── src/main/java/com/example/videowatchlog/
│   │   ├── domain/                 # DDD domain layer (pure Java, framework-agnostic)
│   │   │   ├── model/              # Entities: Title, Series, Episode, ViewingRecord
│   │   │   ├── repository/         # Repository interfaces (dependency inversion)
│   │   │   └── service/            # Domain services (TitleDuplicationCheckService)
│   │   ├── application/            # Application layer (use cases, DTOs)
│   │   │   ├── usecase/            # Use cases: CreateTitle, GetAllTitles, etc.
│   │   │   └── dto/                # Data transfer objects
│   │   ├── infrastructure/         # Infrastructure layer (MyBatis implementations)
│   │   │   └── persistence/        # Repository implementations + MyBatis mappers
│   │   ├── presentation/           # Presentation layer (REST controllers)
│   │   │   └── controller/         # @RestController endpoints
│   │   ├── config/                 # Spring configuration (CORS, etc.)
│   │   └── VideoWatchLogApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml         # Main Spring Boot config
│   │   ├── application-test.yml    # Test config
│   │   ├── db/migration/           # Flyway migrations (V1__initial_schema.sql)
│   │   └── mybatis/mapper/         # MyBatis XML mappers
│   ├── src/test/java/              # JUnit 5 tests (domain, application, integration)
│   └── pom.xml                     # Maven build config (Spring Boot, MyBatis, PostgreSQL, Flyway, JUnit 5)
│
├── frontend/
│   ├── src/
│   │   ├── app/                    # Next.js App Router pages
│   │   │   ├── page.tsx            # Top page (title list)
│   │   │   ├── titles/[id]/        # Title detail page
│   │   │   └── episodes/[id]/      # Episode detail page
│   │   ├── components/             # React components
│   │   │   ├── title/              # Title-related components
│   │   │   ├── series/             # Series-related components
│   │   │   ├── episode/            # Episode-related components
│   │   │   └── common/             # Reusable components (HierarchicalView, SearchBar, etc.)
│   │   ├── lib/
│   │   │   ├── api/                # API client functions (titles.ts, series.ts, etc.)
│   │   │   └── utils/              # Utility functions
│   │   └── types/                  # TypeScript type definitions
│   ├── tests/
│   │   ├── unit/                   # Jest unit tests
│   │   ├── integration/            # Page integration tests
│   │   └── e2e/                    # Playwright E2E tests
│   ├── package.json                # npm dependencies
│   ├── tsconfig.json               # TypeScript config
│   ├── next.config.js              # Next.js config
│   ├── jest.config.js              # Jest config
│   └── playwright.config.ts        # Playwright E2E config
│
├── specs/001-watch-list/           # Feature specification (DDD-driven)
│   ├── spec.md                     # User requirements (technology-agnostic)
│   ├── plan.md                     # Implementation plan (architecture, tech stack)
│   ├── data-model.md               # Data model (entities, relationships)
│   ├── tasks.md                    # Task breakdown (TDD cycle, phases)
│   ├── contracts/                  # API contract (OpenAPI/Swagger)
│   ├── checklists/                 # Quality checklists
│   └── research.md                 # Technical research & decisions
│
├── docker-compose.yml              # Production services (db, backend, frontend)
├── docker-compose.override.yml     # Development overrides (backend-dev, volumes)
├── Makefile                        # Common development commands
├── .env.example                    # Environment template
└── .specify/                       # SpecKit framework files
```

## Key Development Commands

All commands use `make` (Claude Code will be granted `make` permission):

### Development Workflow

```bash
make help            # Show all available commands
make dev             # Start dev environment (hot reload enabled)
make up              # Start production containers
make down            # Stop and remove all containers
make rebuild         # Clean and rebuild everything
```

### Testing

```bash
make test            # Run all tests (Backend JUnit + Frontend Jest)
make test-watch      # Run tests in watch mode (frontend)
```

### Monitoring

```bash
make ps              # Show container status (docker-compose ps)
make status          # Show detailed status (containers + images + disk)
make logs            # Show all service logs (follow mode)
make logs-backend    # Show backend logs only
make logs-frontend   # Show frontend logs only
make logs-db         # Show database logs only
```

### Interactive Access

```bash
make shell-backend   # Shell into backend container (for mvn, debugging)
make shell-frontend  # Shell into frontend container (for npm, debugging)
```

### Validation & Build

```bash
make validate        # Verify docker-compose.yml and project structure
make build           # Build production images
make clean           # Delete all containers/volumes
```

## Architecture Patterns

### Backend: DDD + Onion Architecture

**Dependency Flow** (inner → outer):
```
Domain (model, repository interfaces)
  ↑
Application (use cases, DTOs)
  ↑
Infrastructure (MyBatis, Spring)
  ↑
Presentation (REST controllers, @RestController)
```

**Core DDD Entities**:
- **Title**: Aggregate root, contains multiple Series
- **Series**: Belongs to Title, contains multiple Episodes
- **Episode**: Belongs to Series, has watch status and ViewingRecords
- **ViewingRecord**: Immutable record of a single viewing

**Key Pattern: Factory Methods**
- Entities use private constructors + static factory methods (e.g., `Title.create()`)
- Ensures encapsulation and validates state at creation time
- Tests must use factory methods, not setters

**Repository Pattern**:
- Interfaces in `domain/repository/` (dependency inversion)
- Implementations in `infrastructure/persistence/` with MyBatis
- All persistence logic lives in MyBatis XML mappers (explicit SQL control)

**Why MyBatis?**
- **Explicit SQL Control**: No auto-generated SQL (unlike JPA/Hibernate); easier optimization and debugging
- **Domain Layer Independence**: SQL mapping is completely separated in infrastructure layer, preserving domain purity
- **Flexible Persistence**: Simplifies complex domain model persistence (e.g., value objects mapped to JSONB)

### CQRS Read Model Placement

**Read Model配置原則**:
- Read Model (Query Model) は `application.readmodel` パッケージに配置
- Domain Model とは明確に分離 (CQRS 原則)

**配置理由**:
1. Read Model は「ビュー最適化」というアプリケーション層の関心事
2. Domain 層は「ビジネスルール」に集中（純粋性の維持）
3. Onion Architecture の依存方向を遵守 (Application → Domain)
4. Microsoft eShopOnContainers 等の業界標準パターンと一致

**Mapper配置**: MyBatis Mapper は `infrastructure.persistence.readmodel` に配置
- Read Model（DTO）: Application 層（ビュー最適化の関心事）
- Mapper（技術実装）: Infrastructure 層（永続化技術の詳細）
- 一貫性: Write 側 Mapper（`infrastructure.persistence`）と同じ層に配置

**Read Model vs Domain Model**:
| 観点 | Read Model (application.readmodel) | Domain Model (domain.model) |
|------|-----------------------------------|---------------------------|
| 目的 | ビュー最適化 (JOIN、denormalize) | ビジネスルール表現 |
| 振る舞い | なし (getter のみ) | あり (update, validate 等) |
| 不変性 | 完全不変 (final fields) | 部分的可変 (業務ロジック次第) |
| 永続化 | MyBatis JOIN Query | Repository 経由 |
| CQRS 役割 | Query Model (Read-side) | Command Model (Write-side) |

参考: [Microsoft CQRS Pattern](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/eshoponcontainers-cqrs-ddd-microservice)

### Frontend: Next.js App Router + Server Components

**Component Hierarchy**:
- **Page components** (in `app/`): Handle data fetching, layout
- **Feature components** (in `components/`): Reusable UI units
- **Shared components** (in `components/common/`): Cross-feature utilities
- **API clients** (in `lib/api/`): fetch-based HTTP wrappers

**Dynamic UI Pattern** (Hierarchical View):
- UI adapts to data depth: Single Series titles hide Series layer, Multi-series show it
- `HierarchicalView.tsx` dynamically renders based on Series/Episode count
- Reduces cognitive load and improves UX

## Testing Strategy

**Coverage Goal**: 80% code coverage for business logic (domain and application layers)

**Regression Prevention**: All new features and bug fixes must include corresponding tests; all existing tests must pass before merge

### Backend: TDD Cycle (Red-Green-Refactor)

**Test Hierarchy** (bottom-up):
1. **Domain Tests** (Unit): Entities, value objects, business logic
   - Location: `src/test/java/domain/model/*Test.java`
   - Pattern: Given-When-Then (no Spring context)

2. **Application Tests** (Unit): Use cases with mocked repositories
   - Location: `src/test/java/application/usecase/*Test.java`
   - Pattern: Mock repositories, test use case logic

3. **Infrastructure Tests** (Integration): MyBatis + database
   - Location: `src/test/java/infrastructure/persistence/*Test.java`
   - Use `@MybatisTest` annotation, test with real schema

4. **Presentation Tests** (Integration): REST API endpoints
   - Location: `src/test/java/integration/*ControllerIntegrationTest.java`
   - Full Spring context, test request/response

### Frontend: Jest + React Testing Library + Playwright

1. **Component Tests**: React component behavior (unit)
2. **Page Tests**: Integration tests of full pages
3. **E2E Tests**: Playwright user journey tests (acceptance scenarios)

### Running Tests

```bash
# Backend: Run all tests
docker-compose run --rm test mvn test

# Backend: Run specific test class
make shell-backend
cd /build && mvn test -Dtest=TitleTest

# Frontend: Run all tests (in container or locally)
npm test

# Frontend: E2E tests
npx playwright test
```

## Implementation Workflow

### For Each Feature (TDD):

1. **Red**: Write failing test
   - Clarify requirements through test structure
   - Test first ensures design is testable

2. **Green**: Minimal implementation
   - Make test pass with least code
   - Don't over-engineer yet

3. **Refactor**: Improve code quality
   - Extract duplication
   - Improve naming and organization
   - Ensure tests still pass

### Code Review Checklist

Before committing, verify:
- [ ] Tests written first (Red phase completed)
- [ ] Implementation passes tests (Green)
- [ ] Follows DDD principles (domain layer pure, no framework dependencies)
- [ ] MyBatis mappers use explicit SQL (no dynamic query generation)
- [ ] No setters on entities (use factory methods instead)
- [ ] DTOs use immutable constructor pattern
- [ ] API responses use proper HTTP status codes
- [ ] All new files have appropriate permissions (644 for source, 755 for scripts)

## Git Workflow

**Branch**: `001-watch-list` (feature branch for watch list functionality)

**Commit Message Format**:
```
<type>: <description>

- Explains why this change (not what)
- Lists related task IDs if applicable (T123)

🤖 Generated with Claude Code
Co-Authored-By: Claude <noreply@anthropic.com>
```

**Types**: `feat:` (new feature), `fix:` (bug fix), `improve:` (enhancement), `refactor:`, `test:`, `docs:`

## Database

**Schema**: Normalized (3NF) with JSONB arrays for URLs
**Migrations**: Flyway SQL-based (version-controlled in `db/migration/`)
**Key Tables**:
- `titles` (id, name, created_at, updated_at)
- `series` (id, title_id, name, created_at, updated_at)
- `episodes` (id, series_id, episode_info, watch_status, created_at, updated_at)
- `viewing_records` (id, episode_id, watched_at, rating, comment, recorded_at)
- `title_info_urls` (stored as JSONB in titles.title_info_urls)
- `watch_page_urls` (stored as JSONB in episodes.watch_page_urls)

**Access**: All queries through MyBatis mappers (explicit SQL control), no ORM abstractions

## Performance Targets

- Search/filter results: < 1s (p95)
- API response time: < 200ms (p95)
- Database query: < 50ms (p95)
- Supports 500+ titles with 3-12 episodes per series

## Environment Variables

See `.env.example`:
```
POSTGRES_USER=videowatchuser
POSTGRES_PASSWORD=password
POSTGRES_DB=videowatchlog
SPRING_PROFILES_ACTIVE=dev
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

## Common Issues & Solutions

### Backend Build Issues

**Problem**: `Cannot find symbol` for repository implementations
- **Solution**: Ensure MyBatis mappers are in `src/main/resources/mybatis/mapper/`
- **File Permissions**: Run `chmod 644` on new Java files if not included in JAR

**Problem**: `@MybatisTest` not finding schema
- **Solution**: Place migration in `src/test/resources/db/migration/` or use `spring.mybatis.mapper-locations`

### Frontend Issues

**Problem**: API calls failing (404 or CORS)
- **Check**: `NEXT_PUBLIC_API_URL` in `.env.local` (default: `http://localhost:8080/api/v1`)
- **Backend CORS**: Configured in `src/main/java/config/WebConfig.java` (allows `http://localhost:3000`)

**Problem**: Components not rendering dynamically
- **Solution**: Use `HierarchicalView` component for adaptive UI based on data structure

### Docker Issues

**Problem**: Volume persistence lost after `docker-compose down -v`
- **Solution**: Use `make rebuild` to recreate volumes cleanly

**Problem**: Port conflicts (8080, 5432, 3000)
- **Solution**: Check `docker-compose.yml` port mappings, adjust if needed

## Next Steps for New Features

1. Update `specs/001-watch-list/spec.md` with user requirements
2. Run `/speckit.plan` to generate implementation plan
3. Run `/speckit.tasks` to break down into actionable tasks
4. Follow TDD cycle for each task (Red-Green-Refactor)
5. Run `make test` to ensure all tests pass
6. Commit with proper message format

## Resources

- **Specification**: `specs/001-watch-list/spec.md` (user requirements)
- **Plan**: `specs/001-watch-list/plan.md` (technical approach)
- **Data Model**: `specs/001-watch-list/data-model.md` (entities, relationships)
- **API Contract**: `specs/001-watch-list/contracts/openapi.yaml`
- **Tasks**: `specs/001-watch-list/tasks.md` (detailed implementation breakdown)

---

**Last Updated**: 2025-11-23
**Current Phase**: 4 (User Story 2 - Viewing Record Management)
**Branch**: `001-watch-list`
