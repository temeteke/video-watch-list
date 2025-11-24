import { titlesApi } from '@/lib/api/titles';
import { apiClient } from '@/lib/api/client';
import { WatchStatus } from '@/types/episode';

jest.mock('@/lib/api/client');

describe('titlesApi', () => {
  const mockedApiClient = apiClient as jest.Mocked<typeof apiClient>;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('searchTitles', () => {
    const mockTitles = [
      { id: 1, name: 'Test Title', seriesCount: 1 },
      { id: 2, name: 'Another Title', seriesCount: 2 },
    ];

    it('should call GET /titles/search with query parameter', async () => {
      const query = 'test';
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles(query);

      expect(mockedApiClient.get).toHaveBeenCalledWith('/titles/search?query=test');
    });

    it('should call GET /titles/search with watchStatus parameter', async () => {
      const watchStatus: WatchStatus = 'WATCHED';
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles(undefined, watchStatus);

      expect(mockedApiClient.get).toHaveBeenCalledWith('/titles/search?watchStatus=WATCHED');
    });

    it('should call GET /titles/search with both query and watchStatus parameters', async () => {
      const query = 'anime';
      const watchStatus: WatchStatus = 'UNWATCHED';
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles(query, watchStatus);

      expect(mockedApiClient.get).toHaveBeenCalledWith(
        '/titles/search?query=anime&watchStatus=UNWATCHED'
      );
    });

    it('should call GET /titles/search with no parameters when both are undefined', async () => {
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles(undefined, undefined);

      expect(mockedApiClient.get).toHaveBeenCalledWith('/titles/search');
    });

    it('should return search results as array', async () => {
      mockedApiClient.get.mockResolvedValue(mockTitles);

      const result = await titlesApi.searchTitles('test');

      expect(result).toEqual(mockTitles);
      expect(Array.isArray(result)).toBe(true);
    });

    it('should return empty array when no results found', async () => {
      mockedApiClient.get.mockResolvedValue([]);

      const result = await titlesApi.searchTitles('nonexistent');

      expect(result).toEqual([]);
    });

    it('should handle API errors during search', async () => {
      const error = new Error('Search failed');
      mockedApiClient.get.mockRejectedValue(error);

      await expect(titlesApi.searchTitles('test')).rejects.toThrow('Search failed');
    });

    it('should handle special characters in query string', async () => {
      const query = 'test & special';
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles(query);

      expect(mockedApiClient.get).toHaveBeenCalledWith(
        '/titles/search?query=test%20%26%20special'
      );
    });

    it('should handle empty string query parameter', async () => {
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles('');

      expect(mockedApiClient.get).toHaveBeenCalledWith('/titles/search');
    });

    it('should handle multiple search requests sequentially', async () => {
      mockedApiClient.get.mockResolvedValue(mockTitles);

      await titlesApi.searchTitles('first');
      await titlesApi.searchTitles('second', 'WATCHED');
      await titlesApi.searchTitles(undefined, 'UNWATCHED');

      expect(mockedApiClient.get).toHaveBeenCalledTimes(3);
      expect(mockedApiClient.get).toHaveBeenNthCalledWith(1, '/titles/search?query=first');
      expect(mockedApiClient.get).toHaveBeenNthCalledWith(
        2,
        '/titles/search?query=second&watchStatus=WATCHED'
      );
      expect(mockedApiClient.get).toHaveBeenNthCalledWith(3, '/titles/search?watchStatus=UNWATCHED');
    });
  });
});
