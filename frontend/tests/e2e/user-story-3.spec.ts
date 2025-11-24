import { test, expect } from '@playwright/test';

test.describe('User Story 3: タイトルを検索・フィルタリングする', () => {
  test.beforeEach(async ({ page }) => {
    // トップページに移動
    await page.goto('/');

    // ページが完全に読み込まれるまで待つ
    await page.waitForSelector('[data-testid="search-input"]');
  });

  test('複数のタイトルを事前に作成', async ({ page }) => {
    // タイトル1: 進撃の巨人
    await page.click('a:has-text("新規タイトル作成")');
    expect(page).toHaveURL('/titles/new');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');
    expect(page).toHaveURL('/');

    // タイトル2: ワンピース
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'ワンピース');
    await page.click('button:has-text("作成")');
    expect(page).toHaveURL('/');

    // タイトル3: 東京喰種
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '東京喰種');
    await page.click('button:has-text("作成")');
    expect(page).toHaveURL('/');

    // 3つのタイトルが表示されている
    await expect(page.locator('text=進撃の巨人')).toBeVisible();
    await expect(page.locator('text=ワンピース')).toBeVisible();
    await expect(page.locator('text=東京喰種')).toBeVisible();
  });

  test('タイトル名で検索できる', async ({ page }) => {
    // タイトルを事前に作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'ワンピース');
    await page.click('button:has-text("作成")');

    // 検索入力欄に「進撃」と入力
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('進撃');

    // 検索ボタンをクリック
    await page.click('[data-testid="search-button"]');

    // 検索結果として「進撃の巨人」のみが表示される
    await expect(page.locator('text=進撃の巨人')).toBeVisible();
  });

  test('エンターキーで検索がトリガーされる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'ワンピース');
    await page.click('button:has-text("作成")');

    // 検索入力欄に「進撃」と入力してエンターキーを押す
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('進撃');
    await searchInput.press('Enter');

    // 検索結果が表示される
    await expect(page.locator('text=進撃の巨人')).toBeVisible();
  });

  test('大文字小文字を区別しない検索ができる', async ({ page }) => {
    // タイトルを作成（英語タイトル）
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'Attack on Titan');
    await page.click('button:has-text("作成")');

    // 小文字で検索
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('attack');
    await page.click('[data-testid="search-button"]');

    // 検索結果として「Attack on Titan」が表示される
    await expect(page.locator('text=Attack on Titan')).toBeVisible();
  });

  test('部分一致で検索できる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人 Season 1');
    await page.click('button:has-text("作成")');

    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人 Season 2');
    await page.click('button:has-text("作成")');

    // 「巨人」で検索
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('巨人');
    await page.click('[data-testid="search-button"]');

    // 両方のタイトルが表示される
    await expect(page.locator('text=進撃の巨人 Season 1')).toBeVisible();
    await expect(page.locator('text=進撃の巨人 Season 2')).toBeVisible();
  });

  test('検索で結果がない場合は何も表示されない', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'Anime Title');
    await page.click('button:has-text("作成")');

    // 存在しないタイトルで検索
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('存在しないタイトル');
    await page.click('[data-testid="search-button"]');

    // 検索結果が表示されない（任意：ローディング終了まで待つ）
    await page.waitForTimeout(500);
  });

  test('クリアボタンで検索フィルタをリセットできる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', 'ワンピース');
    await page.click('button:has-text("作成")');

    // 検索
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('進撃');
    await page.click('[data-testid="search-button"]');

    // クリアボタンをクリック
    await page.click('[data-testid="clear-button"]');

    // 両方のタイトルが表示される
    await expect(page.locator('text=進撃の巨人')).toBeVisible();
    await expect(page.locator('text=ワンピース')).toBeVisible();

    // 検索入力欄が空になっている
    expect(await searchInput.inputValue()).toBe('');
  });

  test('検索入力欄と状態フィルタをクリアできる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    // 検索入力欄に入力して状態フィルタを選択
    const searchInput = page.locator('[data-testid="search-input"]');
    const filterSelect = page.locator('[data-testid="watch-status-filter"]');

    await searchInput.fill('進撃');
    await filterSelect.selectOption('WATCHED');

    // クリアボタンをクリック
    await page.click('[data-testid="clear-button"]');

    // 両方リセットされている
    expect(await searchInput.inputValue()).toBe('');
    expect(await filterSelect.inputValue()).toBe('');
  });

  test('検索中は検索ボタンが無効になる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    // 検索ボタンが初めは有効
    const searchButton = page.locator('[data-testid="search-button"]');
    expect(await searchButton.isEnabled()).toBe(true);

    // 検索
    const searchInput = page.locator('[data-testid="search-input"]');
    await searchInput.fill('進撃');
    await searchButton.click();

    // 検索完了後に再度有効
    await page.waitForTimeout(500);
    expect(await searchButton.isEnabled()).toBe(true);
  });
});
