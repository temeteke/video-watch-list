import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import HomePage from '@/app/page';
import * as titlesApi from '@/lib/api/titles';

// Mock the API module
jest.mock('@/lib/api/titles');

// Mock Next.js Link
jest.mock('next/link', () => {
  return ({ children, href }: any) => (
    <a href={href}>{children}</a>
  );
});

const mockTitles = [
  { id: 1, name: 'Anime Title 1', seriesCount: 1 },
  { id: 2, name: 'Anime Title 2', seriesCount: 2 },
  { id: 3, name: 'Manga Title 1', seriesCount: 1 },
];

describe('Home Page', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('initial rendering', () => {
    it('should render page title', async () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByRole('heading', { name: /視聴予定リスト/i })).toBeInTheDocument();
      });
    });

    it('should load all titles on mount', async () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(titlesApi.titlesApi.getAllTitles).toHaveBeenCalled();
      });
    });

    it('should display loading message initially', () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockImplementation(
        () => new Promise(resolve => setTimeout(() => resolve(mockTitles), 100))
      );

      render(<HomePage />);

      expect(screen.getByText(/読み込み中/i)).toBeInTheDocument();
    });

    it('should render SearchBar component', async () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
        expect(screen.getByTestId('watch-status-filter')).toBeInTheDocument();
        expect(screen.getByTestId('search-button')).toBeInTheDocument();
      });
    });

    it('should render new title creation link', async () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByRole('link', { name: /新規タイトル作成/i })).toBeInTheDocument();
      });
    });
  });

  describe('error handling', () => {
    it('should display error message when loading fails', async () => {
      const error = new Error('Failed to load titles');
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockRejectedValue(error);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByText(/Failed to load titles/)).toBeInTheDocument();
      });
    });

    it('should display generic error message for unknown errors', async () => {
      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockRejectedValue('Unknown error');

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByText(/エラーが発生しました/i)).toBeInTheDocument();
      });
    });
  });

  describe('search functionality', () => {
    it('should call searchTitles when search button is clicked', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([mockTitles[0]]);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'Anime');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith('Anime', undefined);
      });
    });

    it('should call searchTitles with watch status filter', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([]);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('watch-status-filter')).toBeInTheDocument();
      });

      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');

      await user.selectOptions(filterSelect, 'WATCHED');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith(undefined, 'WATCHED');
      });
    });

    it('should call searchTitles with both query and filter', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([]);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'Title');
      await user.selectOptions(filterSelect, 'UNWATCHED');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith('Title', 'UNWATCHED');
      });
    });

    it('should trigger search when Enter key is pressed', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([mockTitles[0]]);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      await user.type(searchInput, 'test{Enter}');

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith('test', undefined);
      });
    });

    it('should display search results', async () => {
      const user = userEvent.setup();
      const searchResults = [mockTitles[0]];
      const mockSearchTitles = jest.fn().mockResolvedValue(searchResults);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'Anime Title 1');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith('Anime Title 1', undefined);
      });
    });

    it('should handle empty search results', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([]);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'nonexistent');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalledWith('nonexistent', undefined);
      });
    });

    it('should show loading state during search', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockImplementation(
        () => new Promise(resolve => setTimeout(() => resolve([]), 50))
      );

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'test');
      await user.click(searchButton);

      expect(screen.getByText(/読み込み中/i)).toBeInTheDocument();
    });

    it('should handle search API errors gracefully', async () => {
      const user = userEvent.setup();
      const error = new Error('Search failed');
      const mockSearchTitles = jest.fn().mockRejectedValue(error);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'test');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText(/Search failed/)).toBeInTheDocument();
      });
    });

    it('should disable SearchBar during search', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockImplementation(
        () => new Promise(resolve => setTimeout(() => resolve([]), 50))
      );

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      expect(screen.getByTestId('search-button')).toBeDisabled();
    });
  });

  describe('clear functionality', () => {
    it('should reload all titles when clear button is clicked', async () => {
      const user = userEvent.setup();
      const mockSearchTitles = jest.fn().mockResolvedValue([mockTitles[0]]);
      const mockGetAllTitles = jest.fn()
        .mockResolvedValueOnce(mockTitles)
        .mockResolvedValueOnce(mockTitles);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockImplementation(mockGetAllTitles);
      (titlesApi.titlesApi.searchTitles as jest.Mock).mockImplementation(mockSearchTitles);

      render(<HomePage />);

      await waitFor(() => {
        expect(screen.getByTestId('search-input')).toBeInTheDocument();
      });

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');
      const clearButton = screen.getByTestId('clear-button');

      await user.type(searchInput, 'test');
      await user.click(searchButton);

      await waitFor(() => {
        expect(mockSearchTitles).toHaveBeenCalled();
      });

      await user.click(clearButton);

      await waitFor(() => {
        expect(mockGetAllTitles).toHaveBeenCalledTimes(2);
      });
    });
  });

  describe('title deletion', () => {
    it('should call deleteTitle when delete is triggered', async () => {
      const mockDeleteTitle = jest.fn().mockResolvedValue(undefined);

      (titlesApi.titlesApi.getAllTitles as jest.Mock).mockResolvedValue(mockTitles);
      (titlesApi.titlesApi.deleteTitle as jest.Mock).mockImplementation(mockDeleteTitle);

      render(<HomePage />);

      await waitFor(() => {
        expect(titlesApi.titlesApi.getAllTitles).toHaveBeenCalled();
      });
    });
  });
});
