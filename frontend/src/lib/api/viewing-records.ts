import { apiClient } from './client';

interface ViewingRecordRequest {
  watchedAt: string;
  rating: number;
  comment?: string | null;
}

export const viewingRecordsApi = {
  async completeEpisode(
    episodeId: number,
    request: ViewingRecordRequest
  ): Promise<void> {
    return apiClient.post<void>(`/episodes/${episodeId}/complete`, request);
  },

  async addViewingRecord(
    episodeId: number,
    request: ViewingRecordRequest
  ): Promise<void> {
    return apiClient.post<void>(
      `/episodes/${episodeId}/viewing-records`,
      request
    );
  },

  async deleteViewingRecord(recordId: number): Promise<void> {
    return apiClient.delete<void>(`/viewing-records/${recordId}`);
  },
};
