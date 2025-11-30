/**
 * API エラーハンドリングと ユーザーフレンドリーなエラーメッセージ変換
 */

/**
 * API エラーレスポンスをユーザーフレンドリーなメッセージに変換
 */
export const handleApiError = (error: unknown): string => {
  // ネットワークエラー
  if (error instanceof TypeError && error.message === 'Failed to fetch') {
    return 'ネットワークエラーが発生しました。インターネット接続を確認してください。';
  }

  // カスタムエラーメッセージ
  if (error instanceof Error && error.message) {
    return convertErrorMessage(error.message);
  }

  // 文字列エラー
  if (typeof error === 'string') {
    return convertErrorMessage(error);
  }

  // デフォルトエラーメッセージ
  return 'エラーが発生しました。しばらく経ってからお試しください。';
};

/**
 * エラーメッセージを変換
 */
const convertErrorMessage = (message: string): string => {
  const lowerMessage = message.toLowerCase();

  // ステータスコードベースのエラー判定
  if (lowerMessage.includes('404') || lowerMessage.includes('not found')) {
    return 'リソースが見つかりません。';
  }

  if (lowerMessage.includes('409') || lowerMessage.includes('conflict') || lowerMessage.includes('duplicate')) {
    return 'すでに存在するリソースです。';
  }

  if (lowerMessage.includes('400') || lowerMessage.includes('bad request') || lowerMessage.includes('validation')) {
    return '入力内容に誤りがあります。';
  }

  if (lowerMessage.includes('401') || lowerMessage.includes('unauthorized')) {
    return '認証が必要です。';
  }

  if (lowerMessage.includes('403') || lowerMessage.includes('forbidden')) {
    return 'アクセス権限がありません。';
  }

  if (lowerMessage.includes('500') || lowerMessage.includes('internal server error')) {
    return 'サーバーエラーが発生しました。';
  }

  if (lowerMessage.includes('503') || lowerMessage.includes('service unavailable')) {
    return 'サーバーがメンテナンス中です。';
  }

  // デフォルト
  return 'エラーが発生しました。';
};

/**
 * API レスポンスからエラーメッセージを抽出
 */
export const extractErrorMessage = (response: any): string => {
  // ErrorResponse 形式
  if (response?.message) {
    return response.message;
  }

  // 一般的なエラーレスポンス
  if (response?.error?.message) {
    return response.error.message;
  }

  // エラーメッセージ配列
  if (Array.isArray(response?.errors) && response.errors.length > 0) {
    return response.errors[0]?.message || 'エラーが発生しました。';
  }

  // デフォルト
  return 'エラーが発生しました。';
};

/**
 * HTTP ステータスコードからユーザーフレンドリーなメッセージを生成
 */
export const getStatusCodeMessage = (statusCode: number): string => {
  switch (statusCode) {
    case 400:
      return '入力内容に誤りがあります。';
    case 401:
      return '認証が必要です。';
    case 403:
      return 'アクセス権限がありません。';
    case 404:
      return 'リソースが見つかりません。';
    case 409:
      return 'すでに存在するリソースです。';
    case 500:
      return 'サーバーエラーが発生しました。';
    case 503:
      return 'サーバーがメンテナンス中です。';
    default:
      return 'エラーが発生しました。';
  }
};
