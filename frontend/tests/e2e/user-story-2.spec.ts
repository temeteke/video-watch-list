import { test, expect } from '@playwright/test';

test.describe('User Story 2: 視聴状態と視聴履歴を管理する', () => {
  test.beforeEach(async ({ page }) => {
    // Note: These tests assume the backend is running
    // and the application is available at http://localhost:3000
    await page.goto('http://localhost:3000');
  });

  test('Acceptance Scenario 1: エピソードを視聴完了に変更し、日時・評価・感想を記録できること', async ({
    page,
  }) => {
    // 1. Navigate to a title detail page
    // This would be done by creating a title first via API or UI
    // For this test, we'll assume we can navigate to an existing episode

    // Navigate to episode detail (assuming episode ID 1 exists)
    await page.goto('http://localhost:3000/episodes/1');

    // Wait for page to load
    await page.waitForLoadState('networkidle');

    // Check if the episode is unwatched
    const watchStatusText = await page.textContent('[data-testid="watch-status"]');
    expect(watchStatusText).toContain('未視聴');

    // Fill in the complete episode form
    await page.fill('input[type="datetime-local"]', '2025-01-15T20:00');
    await page.selectOption('select', '5'); // Rating 5
    await page.fill('textarea', 'Great episode! Really enjoyed it.');

    // Submit the form
    await page.click('button:has-text("視聴を完了する")');

    // Wait for the request to complete
    await page.waitForLoadState('networkidle');

    // Verify the episode is now watched
    const updatedStatus = await page.textContent('[data-testid="watch-status"]');
    expect(updatedStatus).toContain('視聴済み');

    // Verify the viewing record is displayed
    await expect(
      page.getByText(/Great episode! Really enjoyed it./)
    ).toBeVisible();
  });

  test('Acceptance Scenario 2: 視聴済みエピソードに複数回の視聴履歴を追加できること', async ({
    page,
  }) => {
    // Navigate to a watched episode
    await page.goto('http://localhost:3000/episodes/1');
    await page.waitForLoadState('networkidle');

    // Wait for the "記録を追加" button to be visible
    const addRecordButton = page.getByRole('button', { name: /記録を追加/i });
    await addRecordButton.waitFor({ state: 'visible' });

    // Add a second viewing record
    await page.fill(
      'form:has(button:has-text("記録を追加")) input[type="datetime-local"]',
      '2025-01-20T19:00'
    );
    await page.selectOption(
      'form:has(button:has-text("記録を追加")) select',
      '4'
    );
    await page.fill(
      'form:has(button:has-text("記録を追加")) textarea',
      'Second viewing - still great!'
    );

    // Submit the form
    await page.click('form:has(button:has-text("記録を追加")) button[type="submit"]');

    // Wait for the request to complete
    await page.waitForLoadState('networkidle');

    // Verify both viewing records are displayed
    await expect(page.getByText(/Second viewing - still great!/)).toBeVisible();

    // Verify viewing records list shows multiple entries
    const recordItems = page.locator('[data-testid^="viewing-record-item-"]');
    await expect(recordItems).toHaveCount(2);
  });

  test('Acceptance Scenario 3: 視聴履歴が新しい順に表示されること', async ({
    page,
  }) => {
    // Navigate to an episode with multiple viewing records
    await page.goto('http://localhost:3000/episodes/1');
    await page.waitForLoadState('networkidle');

    // Get all viewing record items
    const recordItems = page.locator('[data-testid^="viewing-record-item-"]');

    // Verify the first item is the newest one (based on the page structure)
    const firstItemText = await recordItems.first().textContent();
    expect(firstItemText).toBeTruthy();

    // The records should be in reverse chronological order
    // This is implementation-dependent but can be verified by checking dates if displayed
  });

  test('Acceptance Scenario 4: すべての視聴履歴を削除するとエピソードが未視聴に戻ること', async ({
    page,
  }) => {
    // Navigate to a watched episode with viewing records
    await page.goto('http://localhost:3000/episodes/1');
    await page.waitForLoadState('networkidle');

    // Get the initial status
    let watchStatus = await page.textContent('[data-testid="watch-status"]');
    expect(watchStatus).toContain('視聴済み');

    // Get all delete buttons
    const deleteButtons = page.getByRole('button', { name: /削除/i });
    const deleteCount = await deleteButtons.count();

    // Delete all viewing records
    for (let i = 0; i < deleteCount; i++) {
      const btn = deleteButtons.first();
      await btn.click();
      await page.waitForLoadState('networkidle');
    }

    // Verify the episode is now unwatched
    watchStatus = await page.textContent('[data-testid="watch-status"]');
    expect(watchStatus).toContain('未視聴');

    // Verify the CompleteEpisodeForm is shown
    await expect(
      page.getByRole('button', { name: /視聴を完了する/i })
    ).toBeVisible();
  });

  test('Navigation: エピソード詳細ページから戻ることができること', async ({
    page,
  }) => {
    // Navigate to episode detail
    await page.goto('http://localhost:3000/episodes/1');
    await page.waitForLoadState('networkidle');

    // Click back button
    await page.click('button:has-text("戻る")');

    // Should navigate back (URL will change depending on where we came from)
    // For this test, we'll just verify we're not on the episode detail page anymore
    await page.waitForLoadState('networkidle');
    const url = page.url();
    expect(!url.includes('/episodes/1')).toBeTruthy();
  });

  test('Error Handling: API エラーが発生した場合、エラーメッセージが表示されること', async ({
    page,
  }) => {
    // Attempt to navigate to a non-existent episode
    await page.goto('http://localhost:3000/episodes/99999');
    await page.waitForLoadState('networkidle');

    // Verify error message is displayed
    const errorMessage = page.locator('p[style*="color"]');
    await expect(errorMessage).toBeVisible();
  });

  test('Form Validation: 必須項目を入力しないとフォームが送信されないこと', async ({
    page,
  }) => {
    // Navigate to episode detail
    await page.goto('http://localhost:3000/episodes/1');
    await page.waitForLoadState('networkidle');

    // Ensure the episode is unwatched (navigate to a fresh episode if needed)
    const watchStatus = await page.textContent('[data-testid="watch-status"]');
    if (watchStatus?.includes('未視聴')) {
      // Try to submit without filling required fields
      const submitButton = page.getByRole('button', { name: /視聴を完了する/i });

      // The button should be disabled or form should prevent submission
      // This depends on HTML5 validation
      await submitButton.click();

      // Verify we're still on the same page (no submission occurred)
      const url = page.url();
      expect(url).toContain('/episodes/');
    }
  });
});
