/**
 * ViewingRecord（視聴履歴）関連の型定義
 */

export interface ViewingRecordDetail {
  id: number;
  episodeId: number;
  watchedAt: string;
  rating: number;
  comment?: string;
  recordedAt: string;
}

export interface CreateViewingRecordRequest {
  watchedAt: string;
  rating: number;
  comment?: string;
}
