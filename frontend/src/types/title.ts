/**
 * Title（タイトル）関連の型定義
 */

export interface TitleSummary {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface TitleDetail extends TitleSummary {
  series: Series[];
  titleInfoUrls: string[];
}

export interface CreateTitleRequest {
  name: string;
  titleInfoUrls?: string[];
}

export interface UpdateTitleRequest {
  name: string;
  titleInfoUrls?: string[];
}

export interface Series {
  id: number;
  titleId: number;
  name: string;
  episodes: Episode[];
  createdAt: string;
  updatedAt: string;
}

export interface Episode {
  id: number;
  seriesId: number;
  episodeInfo: string;
  watchStatus: WatchStatus;
  watchPageUrls: string[];
  viewingRecords: ViewingRecord[];
  createdAt: string;
  updatedAt: string;
}

export enum WatchStatus {
  UNWATCHED = 'unwatched',
  WATCHED = 'watched',
}

export interface ViewingRecord {
  id: number;
  episodeId: number;
  watchedAt: string;
  rating: number;
  comment: string;
  recordedAt: string;
}
