/**
 * Episode（エピソード）関連の型定義
 */

export type WatchStatus = 'WATCHED' | 'UNWATCHED';

export interface EpisodeDetail {
  id: number;
  seriesId: number;
  episodeInfo: string;
  watchStatus: 'unwatched' | 'watched';
  watchPageUrls: string[];
  viewingRecords: ViewingRecord[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateEpisodeRequest {
  episodeInfo?: string;
  watchPageUrls?: string[];
}

export interface UpdateEpisodeRequest {
  episodeInfo?: string;
  watchPageUrls?: string[];
}

export interface CompleteEpisodeRequest {
  watchedAt: string;
  rating: number;
  comment?: string;
}

export interface ViewingRecord {
  id: number;
  episodeId: number;
  watchedAt: string;
  rating: number;
  comment?: string;
  recordedAt: string;
}
