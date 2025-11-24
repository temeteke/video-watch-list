import { apiClient } from './client';
import {
  TitleSummary,
  TitleDetail,
  CreateTitleRequest,
  UpdateTitleRequest,
} from '@/types/title';
import { WatchStatus } from '@/types/episode';

const BASE_URL = '/titles';

export const titlesApi = {
  async createTitle(request: CreateTitleRequest): Promise<TitleSummary> {
    return apiClient.post<TitleSummary>(BASE_URL, request);
  },

  async getTitleDetail(id: number): Promise<TitleDetail> {
    return apiClient.get<TitleDetail>(`${BASE_URL}/${id}`);
  },

  async updateTitle(id: number, request: UpdateTitleRequest): Promise<void> {
    return apiClient.put<void>(`${BASE_URL}/${id}`, request);
  },

  async deleteTitle(id: number): Promise<void> {
    return apiClient.delete<void>(`${BASE_URL}/${id}`);
  },

  async getAllTitles(): Promise<TitleSummary[]> {
    return apiClient.get<TitleSummary[]>(BASE_URL);
  },

  async searchTitles(
    query?: string,
    watchStatus?: WatchStatus
  ): Promise<TitleSummary[]> {
    const params = new URLSearchParams();
    if (query) {
      params.append('query', query);
    }
    if (watchStatus) {
      params.append('watchStatus', watchStatus);
    }
    const queryString = params.toString();
    const url = queryString ? `${BASE_URL}/search?${queryString}` : `${BASE_URL}/search`;
    return apiClient.get<TitleSummary[]>(url);
  },
};
