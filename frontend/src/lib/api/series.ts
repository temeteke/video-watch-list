import { apiClient } from './client';
import { SeriesDetail, CreateSeriesRequest, UpdateSeriesRequest } from '@/types/series';

export const seriesApi = {
  async createSeries(titleId: number, request: CreateSeriesRequest): Promise<void> {
    return apiClient.post<void>(`/titles/${titleId}/series`, request);
  },

  async updateSeries(titleId: number, seriesId: number, request: UpdateSeriesRequest): Promise<void> {
    return apiClient.put<void>(`/titles/${titleId}/series/${seriesId}`, request);
  },

  async deleteSeries(titleId: number, seriesId: number): Promise<void> {
    return apiClient.delete<void>(`/titles/${titleId}/series/${seriesId}`);
  },
};
