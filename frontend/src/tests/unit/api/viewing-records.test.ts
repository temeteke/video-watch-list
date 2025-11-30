import { viewingRecordsApi } from '@/lib/api/viewing-records';
import { apiClient } from '@/lib/api/client';

jest.mock('@/lib/api/client');

describe('viewingRecordsApi', () => {
  const mockedApiClient = apiClient as jest.Mocked<typeof apiClient>;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('completeEpisode', () => {
    it('should call POST /episodes/{id}/complete with request data', async () => {
      const episodeId = 1;
      const request = {
        watchedAt: '2025-01-01T20:00:00',
        rating: 4,
        comment: 'Great episode!',
      };

      mockedApiClient.post.mockResolvedValue(undefined);

      await viewingRecordsApi.completeEpisode(episodeId, request);

      expect(mockedApiClient.post).toHaveBeenCalledWith(
        `/episodes/${episodeId}/complete`,
        request
      );
    });

    it('should handle API errors when completing episode', async () => {
      const episodeId = 1;
      const request = {
        watchedAt: '2025-01-01T20:00:00',
        rating: 4,
        comment: 'Great episode!',
      };
      const error = new Error('API Error');

      mockedApiClient.post.mockRejectedValue(error);

      await expect(
        viewingRecordsApi.completeEpisode(episodeId, request)
      ).rejects.toThrow('API Error');
    });

    it('should handle null comment in request', async () => {
      const episodeId = 1;
      const request = {
        watchedAt: '2025-01-01T20:00:00',
        rating: 5,
        comment: null,
      };

      mockedApiClient.post.mockResolvedValue(undefined);

      await viewingRecordsApi.completeEpisode(episodeId, request);

      expect(mockedApiClient.post).toHaveBeenCalledWith(
        `/episodes/${episodeId}/complete`,
        request
      );
    });
  });

  describe('addViewingRecord', () => {
    it('should call POST /episodes/{id}/viewing-records with request data', async () => {
      const episodeId = 2;
      const request = {
        watchedAt: '2025-01-02T19:00:00',
        rating: 5,
        comment: 'Excellent rewatch!',
      };

      mockedApiClient.post.mockResolvedValue(undefined);

      await viewingRecordsApi.addViewingRecord(episodeId, request);

      expect(mockedApiClient.post).toHaveBeenCalledWith(
        `/episodes/${episodeId}/viewing-records`,
        request
      );
    });

    it('should handle API errors when adding viewing record', async () => {
      const episodeId = 2;
      const request = {
        watchedAt: '2025-01-02T19:00:00',
        rating: 5,
        comment: 'Excellent rewatch!',
      };
      const error = new Error('Episode not found');

      mockedApiClient.post.mockRejectedValue(error);

      await expect(
        viewingRecordsApi.addViewingRecord(episodeId, request)
      ).rejects.toThrow('Episode not found');
    });

    it('should allow optional comment field', async () => {
      const episodeId = 2;
      const request = {
        watchedAt: '2025-01-02T19:00:00',
        rating: 3,
        comment: undefined,
      };

      mockedApiClient.post.mockResolvedValue(undefined);

      await viewingRecordsApi.addViewingRecord(episodeId, request);

      expect(mockedApiClient.post).toHaveBeenCalledWith(
        `/episodes/${episodeId}/viewing-records`,
        request
      );
    });
  });

  describe('deleteViewingRecord', () => {
    it('should call DELETE /viewing-records/{id}', async () => {
      const recordId = 10;

      mockedApiClient.delete.mockResolvedValue(undefined);

      await viewingRecordsApi.deleteViewingRecord(recordId);

      expect(mockedApiClient.delete).toHaveBeenCalledWith(
        `/viewing-records/${recordId}`
      );
    });

    it('should handle API errors when deleting viewing record', async () => {
      const recordId = 10;
      const error = new Error('Record not found');

      mockedApiClient.delete.mockRejectedValue(error);

      await expect(
        viewingRecordsApi.deleteViewingRecord(recordId)
      ).rejects.toThrow('Record not found');
    });

    it('should handle deletion with different record IDs', async () => {
      const recordIds = [1, 5, 99];

      mockedApiClient.delete.mockResolvedValue(undefined);

      for (const recordId of recordIds) {
        await viewingRecordsApi.deleteViewingRecord(recordId);
        expect(mockedApiClient.delete).toHaveBeenCalledWith(
          `/viewing-records/${recordId}`
        );
      }

      expect(mockedApiClient.delete).toHaveBeenCalledTimes(recordIds.length);
    });
  });

  describe('integration with apiClient', () => {
    it('should use apiClient instance for all requests', async () => {
      const episodeId = 3;
      const request = {
        watchedAt: '2025-01-03T18:00:00',
        rating: 4,
        comment: 'Good',
      };

      mockedApiClient.post.mockResolvedValue(undefined);
      mockedApiClient.delete.mockResolvedValue(undefined);

      await viewingRecordsApi.completeEpisode(episodeId, request);
      await viewingRecordsApi.addViewingRecord(episodeId, request);
      await viewingRecordsApi.deleteViewingRecord(1);

      expect(mockedApiClient.post).toHaveBeenCalledTimes(2);
      expect(mockedApiClient.delete).toHaveBeenCalledTimes(1);
    });
  });
});
