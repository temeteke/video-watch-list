import { apiClient } from './client';
import {
  TitleSummary,
  TitleDetail,
  CreateTitleRequest,
  UpdateTitleRequest,
} from '@/types/title';

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
};
