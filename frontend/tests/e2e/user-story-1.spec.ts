import { test, expect } from '@playwright/test';

test.describe('User Story 1: タイトルと複数シリーズを登録する', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('新しいタイトルを作成できる', async ({ page }) => {
    // トップページから新規作成ページへ
    await page.click('a:has-text("新規タイトル作成")');
    expect(page).toHaveURL('/titles/new');

    // タイトルを入力して送信
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    // トップページにリダイレクト
    expect(page).toHaveURL('/');
    await expect(page.locator('text=進撃の巨人')).toBeVisible();
  });

  test('シリーズを追加できる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    // タイトル詳細ページへ
    await page.click('a:has-text("進撃の巨人")');

    // シリーズ追加
    await page.click('button:has-text("シリーズ追加")');
    await page.fill('input[placeholder="例: Season 2"]', 'Season 2');
    await page.click('button:has-text("シリーズを追加")');

    // Season 2が表示される
    await expect(page.locator('text=Season 2')).toBeVisible();
  });

  test('エピソードを追加できる', async ({ page }) => {
    // タイトルとシリーズを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    await page.click('a:has-text("進撃の巨人")');

    // エピソード追加
    await page.fill('input[placeholder="例: 第1話"]', '第1話');
    await page.click('button:has-text("エピソードを追加")');

    // エピソードが表示される
    await expect(page.locator('text=第1話')).toBeVisible();
  });

  test('タイトルを削除できる', async ({ page }) => {
    // タイトルを作成
    await page.click('a:has-text("新規タイトル作成")');
    await page.fill('input[placeholder="例: 進撃の巨人"]', '進撃の巨人');
    await page.click('button:has-text("作成")');

    // 削除ボタンをクリック
    await page.click('button:has-text("削除")');
    page.on('dialog', dialog => dialog.accept());

    // タイトルが削除される
    await expect(page.locator('text=進撃の巨人')).not.toBeVisible();
  });
});
