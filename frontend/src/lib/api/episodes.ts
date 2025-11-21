import { apiClient } from './client';
import { CreateEpisodeRequest, UpdateEpisodeRequest } from '@/types/episode';

export const episodesApi = {
  async createEpisode(seriesId: number, request: CreateEpisodeRequest): Promise<void> {
    return apiClient.post<void>(`/series/${seriesId}/episodes`, request);
  },

  async updateEpisode(seriesId: number, episodeId: number, request: UpdateEpisodeRequest): Promise<void> {
    return apiClient.put<void>(`/series/${seriesId}/episodes/${episodeId}`, request);
  },

  async deleteEpisode(seriesId: number, episodeId: number): Promise<void> {
    return apiClient.delete<void>(`/series/${seriesId}/episodes/${episodeId}`);
  },
};
