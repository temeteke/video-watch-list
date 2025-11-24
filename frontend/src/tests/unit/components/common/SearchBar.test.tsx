import { render, screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import SearchBar from '@/components/common/SearchBar';
import { WatchStatus } from '@/types/episode';

describe('SearchBar', () => {
  const mockOnSearch = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('rendering', () => {
    it('should render search input field', () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      expect(searchInput).toBeInTheDocument();
      expect(searchInput).toHaveAttribute('type', 'text');
      expect(searchInput).toHaveAttribute('placeholder', 'タイトルを検索...');
    });

    it('should render watch status filter select', () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const filterSelect = screen.getByTestId('watch-status-filter');
      expect(filterSelect).toBeInTheDocument();

      const options = screen.getAllByRole('option');
      expect(options.length).toBeGreaterThan(0);
      expect(options[0]).toHaveTextContent('すべての状態');
      expect(options[1]).toHaveTextContent('視聴済み');
      expect(options[2]).toHaveTextContent('未視聴');
    });

    it('should render search button', () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchButton = screen.getByTestId('search-button');
      expect(searchButton).toBeInTheDocument();
      expect(searchButton).toHaveTextContent('検索');
    });

    it('should render clear button', () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const clearButton = screen.getByTestId('clear-button');
      expect(clearButton).toBeInTheDocument();
      expect(clearButton).toHaveTextContent('クリア');
    });
  });

  describe('search functionality', () => {
    it('should call onSearch when search button is clicked', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'test query');
      await user.click(searchButton);

      expect(mockOnSearch).toHaveBeenCalledWith('test query', undefined);
    });

    it('should call onSearch with query and watch status', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'anime');
      await user.selectOptions(filterSelect, 'WATCHED');
      await user.click(searchButton);

      expect(mockOnSearch).toHaveBeenCalledWith('anime', 'WATCHED');
    });

    it('should call onSearch with watch status only', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');

      await user.selectOptions(filterSelect, 'UNWATCHED');
      await user.click(searchButton);

      expect(mockOnSearch).toHaveBeenCalledWith(undefined, 'UNWATCHED');
    });

    it('should call onSearch with undefined when no filters are set', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      expect(mockOnSearch).toHaveBeenCalledWith(undefined, undefined);
    });
  });

  describe('clear functionality', () => {
    it('should clear search input when clear button is clicked', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input') as HTMLInputElement;
      const clearButton = screen.getByTestId('clear-button');

      await user.type(searchInput, 'test query');
      expect(searchInput.value).toBe('test query');

      await user.click(clearButton);
      expect(searchInput.value).toBe('');
    });

    it('should clear watch status filter when clear button is clicked', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const filterSelect = screen.getByTestId('watch-status-filter') as HTMLSelectElement;
      const clearButton = screen.getByTestId('clear-button');

      await user.selectOptions(filterSelect, 'WATCHED');
      expect(filterSelect.value).toBe('WATCHED');

      await user.click(clearButton);
      expect(filterSelect.value).toBe('');
    });

    it('should call onSearch with undefined values when clear button is clicked', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const clearButton = screen.getByTestId('clear-button');

      await user.type(searchInput, 'test');
      await user.selectOptions(filterSelect, 'WATCHED');
      await user.click(clearButton);

      expect(mockOnSearch).toHaveBeenCalledWith(undefined, undefined);
    });
  });

  describe('keyboard support', () => {
    it('should trigger search when Enter key is pressed in search input', async () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      fireEvent.change(searchInput, { target: { value: 'test' } });
      fireEvent.keyDown(searchInput, { key: 'Enter' });

      expect(mockOnSearch).toHaveBeenCalledWith('test', undefined);
    });

    it('should not trigger search when other keys are pressed', async () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      fireEvent.change(searchInput, { target: { value: 'test' } });
      fireEvent.keyDown(searchInput, { key: 'a' });

      expect(mockOnSearch).not.toHaveBeenCalled();
    });
  });

  describe('loading state', () => {
    it('should disable all inputs and buttons when loading', () => {
      render(<SearchBar onSearch={mockOnSearch} isLoading={true} />);

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');
      const clearButton = screen.getByTestId('clear-button');

      expect(searchInput).toBeDisabled();
      expect(filterSelect).toBeDisabled();
      expect(searchButton).toBeDisabled();
      expect(clearButton).toBeDisabled();
    });

    it('should enable all inputs and buttons when not loading', () => {
      render(<SearchBar onSearch={mockOnSearch} isLoading={false} />);

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');
      const clearButton = screen.getByTestId('clear-button');

      expect(searchInput).not.toBeDisabled();
      expect(filterSelect).not.toBeDisabled();
      expect(searchButton).not.toBeDisabled();
      expect(clearButton).not.toBeDisabled();
    });

    it('should default to not loading when isLoading prop is not provided', () => {
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      expect(searchInput).not.toBeDisabled();
    });
  });

  describe('form input changes', () => {
    it('should update search input value when user types', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input') as HTMLInputElement;
      await user.type(searchInput, 'my search query');

      expect(searchInput.value).toBe('my search query');
    });

    it('should update watch status filter when user selects option', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const filterSelect = screen.getByTestId('watch-status-filter') as HTMLSelectElement;
      await user.selectOptions(filterSelect, 'UNWATCHED');

      expect(filterSelect.value).toBe('UNWATCHED');
    });

    it('should handle empty search input', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      expect(mockOnSearch).toHaveBeenCalledWith(undefined, undefined);
    });
  });

  describe('integration', () => {
    it('should handle multiple search operations sequentially', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input');
      const filterSelect = screen.getByTestId('watch-status-filter');
      const searchButton = screen.getByTestId('search-button');

      // First search
      await user.type(searchInput, 'first');
      await user.click(searchButton);
      expect(mockOnSearch).toHaveBeenLastCalledWith('first', undefined);

      // Clear and second search
      const clearButton = screen.getByTestId('clear-button');
      await user.click(clearButton);
      expect(mockOnSearch).toHaveBeenLastCalledWith(undefined, undefined);

      // Third search with different filter
      await user.type(searchInput, 'second');
      await user.selectOptions(filterSelect, 'WATCHED');
      await user.click(searchButton);
      expect(mockOnSearch).toHaveBeenLastCalledWith('second', 'WATCHED');

      expect(mockOnSearch).toHaveBeenCalledTimes(3);
    });

    it('should preserve search input after unsuccessful search', async () => {
      const user = userEvent.setup();
      render(<SearchBar onSearch={mockOnSearch} />);

      const searchInput = screen.getByTestId('search-input') as HTMLInputElement;
      const searchButton = screen.getByTestId('search-button');

      await user.type(searchInput, 'test');
      await user.click(searchButton);

      expect(searchInput.value).toBe('test');
      expect(mockOnSearch).toHaveBeenCalled();
    });
  });
});
