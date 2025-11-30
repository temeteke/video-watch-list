/**
 * Error（エラー）関連の型定義
 */

export interface ErrorResponse {
  code: number;
  message: string;
  details?: Record<string, string>;
  timestamp: string;
}

export class ApiError extends Error {
  constructor(
    public code: number,
    public message: string,
    public details?: Record<string, string>,
  ) {
    super(message);
    this.name = 'ApiError';
  }
}
