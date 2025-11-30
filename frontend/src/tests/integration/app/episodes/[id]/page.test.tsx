import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import EpisodeDetailPage from '@/app/episodes/[id]/page';
import * as episodesApi from '@/lib/api/episodes';
import * as viewingRecordsApi from '@/lib/api/viewing-records';
import { EpisodeDetail } from '@/types/episode';

// Mock the API modules
jest.mock('@/lib/api/episodes');
jest.mock('@/lib/api/viewing-records');

// Mock Next.js router and params
jest.mock('next/navigation', () => ({
  useParams: () => ({ id: '1' }),
  useRouter: () => ({
    back: jest.fn(),
  }),
}));

const mockEpisode: EpisodeDetail = {
  id: 1,
  seriesId: 100,
  episodeInfo: '第1話',
  watchStatus: 'unwatched',
  watchPageUrls: ['https://example.com/episode1'],
  viewingRecords: [],
  createdAt: '2025-01-01T00:00:00',
  updatedAt: '2025-01-01T00:00:00',
};

const mockEpisodeWithViewingRecords: EpisodeDetail = {
  ...mockEpisode,
  watchStatus: 'watched',
  viewingRecords: [
    {
      id: 1,
      episodeId: 1,
      watchedAt: '2025-01-15T20:00:00',
      rating: 5,
      comment: 'Great episode!',
      recordedAt: '2025-01-15T20:30:00',
    },
  ],
};

describe('Episode Detail Page', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('rendering', () => {
    it('should render episode detail page', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisode
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/第1話/)).toBeInTheDocument();
      });
    });

    it('should display episode information', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisode
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/第1話/)).toBeInTheDocument();
        expect(screen.getByText(/unwatched|未視聴/i)).toBeInTheDocument();
      });
    });

    it('should show CompleteEpisodeForm for unwatched episodes', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisode
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByRole('button', { name: /視聴を完了する/i })).toBeInTheDocument();
      });
    });

    it('should show ViewingRecordList when episode has viewing records', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisodeWithViewingRecords
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      });
    });

    it('should show AddViewingRecordForm for watched episodes', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisodeWithViewingRecords
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByRole('button', { name: /記録を追加/i })).toBeInTheDocument();
      });
    });

    it('should display loading state initially', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockImplementation(
        () => new Promise(resolve => setTimeout(() => resolve(mockEpisode), 100))
      );

      render(<EpisodeDetailPage />);

      expect(screen.getByText(/読み込み中/i)).toBeInTheDocument();
    });

    it('should display error message when API fails', async () => {
      const error = new Error('Failed to load episode');
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockRejectedValue(error);

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/Failed to load episode/)).toBeInTheDocument();
      });
    });
  });

  describe('complete episode functionality', () => {
    it('should call completeEpisode when form is submitted', async () => {
      const user = userEvent.setup();
      const mockCompleteEpisode = jest.fn().mockResolvedValue(undefined);

      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisode
      );
      (viewingRecordsApi.viewingRecordsApi.completeEpisode as jest.Mock).mockImplementation(
        mockCompleteEpisode
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/第1話/)).toBeInTheDocument();
      });

      const datetimeInput = screen.getByLabelText(/視聴完了日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /視聴を完了する/i });

      await user.type(datetimeInput, '2025-01-15T20:00');
      await user.selectOptions(ratingSelect, '5');
      await user.click(submitButton);

      await waitFor(() => {
        expect(mockCompleteEpisode).toHaveBeenCalledWith(
          1,
          expect.objectContaining({
            watchedAt: '2025-01-15T20:00',
            rating: 5,
          })
        );
      });
    });

    it('should reload episode data after completing', async () => {
      const user = userEvent.setup();
      const getEpisodeDetailMock = jest.fn()
        .mockResolvedValueOnce(mockEpisode)
        .mockResolvedValueOnce(mockEpisodeWithViewingRecords);

      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockImplementation(
        getEpisodeDetailMock
      );
      (viewingRecordsApi.viewingRecordsApi.completeEpisode as jest.Mock).mockResolvedValue(
        undefined
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/第1話/)).toBeInTheDocument();
      });

      const datetimeInput = screen.getByLabelText(/視聴完了日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /視聴を完了する/i });

      await user.type(datetimeInput, '2025-01-15T20:00');
      await user.selectOptions(ratingSelect, '4');
      await user.click(submitButton);

      await waitFor(() => {
        expect(getEpisodeDetailMock).toHaveBeenCalledTimes(2);
      });
    });
  });

  describe('viewing record functionality', () => {
    it('should call addViewingRecord when adding record', async () => {
      const user = userEvent.setup();
      const mockAddViewingRecord = jest.fn().mockResolvedValue(undefined);

      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisodeWithViewingRecords
      );
      (viewingRecordsApi.viewingRecordsApi.addViewingRecord as jest.Mock).mockImplementation(
        mockAddViewingRecord
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      });

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:00');
      await user.selectOptions(ratingSelect, '4');
      await user.click(submitButton);

      await waitFor(() => {
        expect(mockAddViewingRecord).toHaveBeenCalledWith(
          1,
          expect.objectContaining({
            watchedAt: '2025-01-20T19:00',
            rating: 4,
          })
        );
      });
    });

    it('should call deleteViewingRecord when deleting record', async () => {
      const user = userEvent.setup();
      const mockDeleteViewingRecord = jest.fn().mockResolvedValue(undefined);

      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisodeWithViewingRecords
      );
      (viewingRecordsApi.viewingRecordsApi.deleteViewingRecord as jest.Mock).mockImplementation(
        mockDeleteViewingRecord
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      });

      const deleteButton = screen.getByRole('button', { name: /削除/i });
      await user.click(deleteButton);

      await waitFor(() => {
        expect(mockDeleteViewingRecord).toHaveBeenCalledWith(1);
      });
    });
  });

  describe('back navigation', () => {
    it('should have back button', async () => {
      (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
        mockEpisode
      );

      render(<EpisodeDetailPage />);

      await waitFor(() => {
        expect(screen.getByText(/第1話/)).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /戻る/i })).toBeInTheDocument();
    });
  });
});
