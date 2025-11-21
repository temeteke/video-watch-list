/**
 * Series（シリーズ）関連の型定義
 */

import { Episode } from './title';

export interface SeriesDetail {
  id: number;
  titleId: number;
  name: string;
  episodes: Episode[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateSeriesRequest {
  name?: string;
}

export interface UpdateSeriesRequest {
  name?: string;
}
