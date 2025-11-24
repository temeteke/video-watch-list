# ビデオ視聴ログシステム - フロントエンド

Next.js 14 と React 18 で構築したモダンな UI フロントエンド

## セットアップ

### 前提条件

- Node.js 18.17 以上
- npm または yarn
- Docker（オプション）

### インストール手順

1. リポジトリをクローン
```bash
git clone https://github.com/your-repo/video-watch-list.git
cd video-watch-list/frontend
```

2. 依存関係をインストール
```bash
npm install
# または
yarn install
```

3. 環境変数を設定
```bash
# .env.local を作成
echo "NEXT_PUBLIC_API_BASE_URL=http://localhost:8080" > .env.local
```

4. 開発サーバーを起動
```bash
npm run dev
# または
yarn dev
```

アプリケーションは http://localhost:3000 で起動します。

## Docker での実行

```bash
docker-compose up frontend
```

## ビルド & デプロイ

### 本番ビルド
```bash
npm run build
npm start
```

### ビルドサイズ分析
```bash
npm run analyze
```

## テスト実行

### ユニットテスト
```bash
npm test
```

### 特定のテストファイルを実行
```bash
npm test -- SearchBar.test.tsx
```

### テストカバレッジ
```bash
npm test -- --coverage
```

### 統合テスト
```bash
npm run test:integration
```

### E2E テスト（Playwright）
```bash
npm run test:e2e
```

## プロジェクト構造

```
src/
├── app/                         # Next.js App Router
│   ├── page.tsx                # ホームページ（タイトル一覧）
│   ├── titles/
│   │   ├── [id]/
│   │   │   └── page.tsx       # タイトル詳細ページ
│   │   └── new/
│   │       └── page.tsx       # タイトル作成ページ
│   ├── episodes/
│   │   └── [id]/
│   │       └── page.tsx       # エピソード詳細ページ
│   └── layout.tsx             # ルートレイアウト
├── components/
│   ├── common/                 # 共通コンポーネント
│   │   ├── SearchBar.tsx
│   │   ├── ConfirmDialog.tsx
│   │   ├── ErrorBoundary.tsx
│   │   ├── Toast.tsx
│   │   └── ...
│   ├── title/                  # タイトル関連コンポーネント
│   │   ├── TitleList.tsx
│   │   ├── TitleForm.tsx
│   │   └── ...
│   └── episode/                # エピソード関連コンポーネント
│       ├── EpisodeList.tsx
│       ├── CompleteEpisodeForm.tsx
│       └── ...
├── lib/
│   ├── api/                    # API クライアント
│   │   ├── client.ts
│   │   ├── titles.ts
│   │   ├── series.ts
│   │   ├── episodes.ts
│   │   └── viewing-records.ts
│   └── utils/                  # ユーティリティ
│       ├── error-handler.ts    # エラーハンドリング
│       ├── debounce.ts         # デバウンス処理
│       └── ...
├── types/                      # TypeScript 型定義
│   ├── title.ts
│   ├── episode.ts
│   └── ...
└── tests/
    ├── unit/                   # ユニットテスト
    │   ├── api/
    │   └── components/
    ├── integration/            # 統合テスト
    │   └── app/
    └── e2e/                    # E2E テスト（Playwright）
        ├── user-story-1.spec.ts
        ├── user-story-2.spec.ts
        └── user-story-3.spec.ts
```

## コンポーネント

### SearchBar
タイトルの検索とフィルタリング機能

```tsx
<SearchBar
  onSearch={(query, watchStatus) => {
    // 検索処理
  }}
  isLoading={loading}
/>
```

**Props:**
- `onSearch`: (query: string | undefined, watchStatus: WatchStatus | undefined) => void
- `isLoading`: boolean (オプション、デフォルト: false)

### ConfirmDialog
削除確認などのダイアログ

```tsx
<ConfirmDialog
  title="削除確認"
  message="このタイトルを削除してもよろしいですか？"
  onConfirm={handleConfirm}
  onCancel={handleCancel}
  variant="danger"
/>
```

**Props:**
- `title`: string
- `message`: string
- `onConfirm`: () => void
- `onCancel`: () => void
- `isLoading`: boolean (オプション)
- `confirmButtonLabel`: string (オプション)
- `cancelButtonLabel`: string (オプション)
- `variant`: 'danger' | 'warning' | 'info' (オプション)

### Toast
メッセージ表示

```tsx
import Toast, { showToast } from '@/components/common/Toast';

// コンポーネント内で使用
showToast('成功しました', 'success');
showToast('エラーが発生しました', 'error');
```

**メソッド:**
- `showToast(message: string, type?: 'success' | 'error' | 'warning' | 'info', duration?: number)`

### ErrorBoundary
エラーハンドリング

```tsx
<ErrorBoundary>
  <YourComponent />
</ErrorBoundary>
```

## API クライアント

### titlesApi

```typescript
import { titlesApi } from '@/lib/api/titles';

// 全タイトルを取得
const titles = await titlesApi.getAllTitles();

// タイトルを検索
const results = await titlesApi.searchTitles('進撃', 'WATCHED');

// タイトルを作成
const newTitle = await titlesApi.createTitle({
  name: '進撃の巨人',
  titleInfoUrls: ['https://example.com']
});

// タイトルを更新
await titlesApi.updateTitle(1, {
  name: '進撃の巨人 Season 2'
});

// タイトルを削除
await titlesApi.deleteTitle(1);
```

## ユーティリティ

### debounce
入力のデバウンス処理

```typescript
import { debounce } from '@/lib/utils/debounce';

const debouncedSearch = debounce((query: string) => {
  fetchSearchResults(query);
}, 300);

<input onChange={(e) => debouncedSearch(e.target.value)} />
```

### error-handler
エラーメッセージの変換

```typescript
import { handleApiError, extractErrorMessage } from '@/lib/utils/error-handler';

try {
  // API 呼び出し
} catch (error) {
  const message = handleApiError(error);
  showToast(message, 'error');
}
```

## スタイリング

### CSS-in-JS
すべてのコンポーネントはインラインスタイルまたは CSS モジュールを使用

### 色の統一
- Primary: `#007bff`
- Success: `#28a745`
- Danger: `#dc3545`
- Warning: `#ffc107`
- Info: `#17a2b8`

## パフォーマンス最適化

### コード分割
- Next.js の自動コード分割（ページごと）
- `dynamic()` による遅延ロード

### 画像最適化
- `next/image` コンポーネント使用
- 自動的なサイズ最適化

### デバウンス処理
- 検索入力に 300ms のデバウンスを適用
- API リクエストの削減

## テスト戦略

### ユニットテスト（Jest）
- コンポーネントのロジック検証
- ユーティリティ関数のテスト
- API クライアントのモック

### 統合テスト
- ページ全体のエンドツーエンドテスト
- ユーザーインタラクションのシミュレーション
- API モックとの統合

### E2E テスト（Playwright）
- 実ブラウザでの動作確認
- ユーザーシナリオの検証
- クロスブラウザ対応確認

## ベストプラクティス

1. **型安全性**: すべてのコンポーネントと関数に型定義
2. **エラーハンドリング**: グローバル例外処理と ユーザーフレンドリーなメッセージ
3. **アクセシビリティ**: `data-testid` 属性の使用、ラベル付きフォーム
4. **パフォーマンス**: デバウンス、遅延ロード、メモ化
5. **テストカバレッジ**: 重要なロジックは 80% 以上をカバー

## トラブルシューティング

### ビルドエラー
```bash
# キャッシュをクリア
npm run clean
npm run build
```

### 開発サーバー接続エラー
```bash
# バックエンド API の確認
curl http://localhost:8080/titles

# .env.local の API URL を確認
cat .env.local
```

### テスト失敗
```bash
# キャッシュをクリア
npm test -- --clearCache

# 詳細ログで実行
npm test -- --verbose
```

## 今後の改善

- [ ] ダークモード対応
- [ ] 多言語対応（i18n）
- [ ] PWA 化（オフライン対応）
- [ ] パフォーマンス監視（Web Vitals）
- [ ] UI コンポーネント ライブラリ統合（Material-UI など）
- [ ] 状態管理改善（Redux, Zustand など）
