import { render, screen, waitFor } from '@testing-library/react';
import EpisodeDetailPage from '@/app/series/[seriesId]/episodes/[episodeId]/page';
import * as episodesApi from '@/lib/api/episodes';
import * as viewingRecordsApi from '@/lib/api/viewing-records';
import { EpisodeDetail } from '@/types/episode';

// Mock the API modules
jest.mock('@/lib/api/episodes');
jest.mock('@/lib/api/viewing-records');

// Mock Next.js router and params
jest.mock('next/navigation', () => ({
  useParams: () => ({ seriesId: '100', episodeId: '1' }),
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

describe('Episode Detail Page', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should render episode detail page and call API with correct parameters', async () => {
    (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
      mockEpisode
    );

    render(<EpisodeDetailPage />);

    await waitFor(() => {
      expect(episodesApi.episodesApi.getEpisodeDetail).toHaveBeenCalledWith(100, 1);
    });
  });

  it('should display episode information when loaded', async () => {
    (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
      mockEpisode
    );

    render(<EpisodeDetailPage />);

    await waitFor(() => {
      expect(screen.getByText(/第1話/)).toBeInTheDocument();
      expect(screen.getByText(/未視聴/i)).toBeInTheDocument();
    });
  });

  it('should have back button', async () => {
    (episodesApi.episodesApi.getEpisodeDetail as jest.Mock).mockResolvedValue(
      mockEpisode
    );

    render(<EpisodeDetailPage />);

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /戻る/i })).toBeInTheDocument();
    });
  });
});
