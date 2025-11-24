import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AddViewingRecordForm from '@/components/episode/AddViewingRecordForm';

describe('AddViewingRecordForm', () => {
  const mockOnSubmit = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('rendering', () => {
    it('should render form with datetime input', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      expect(datetimeInput).toBeInTheDocument();
      expect(datetimeInput).toHaveAttribute('type', 'datetime-local');
    });

    it('should render rating select with options 1-5', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const ratingSelect = screen.getByLabelText(/評価/i);
      expect(ratingSelect).toBeInTheDocument();

      const options = screen.getAllByRole('option');
      expect(options).toHaveLength(6); // Default + 1-5
      expect(options[0]).toHaveTextContent('選択してください');
      expect(options[1]).toHaveTextContent('1');
      expect(options[2]).toHaveTextContent('2');
      expect(options[3]).toHaveTextContent('3');
      expect(options[4]).toHaveTextContent('4');
      expect(options[5]).toHaveTextContent('5');
    });

    it('should render comment textarea', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const commentTextarea = screen.getByLabelText(/感想/i);
      expect(commentTextarea).toBeInTheDocument();
      expect(commentTextarea).toHaveAttribute('rows', '4');
    });

    it('should render submit button', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const submitButton = screen.getByRole('button', { name: /記録を追加/i });
      expect(submitButton).toBeInTheDocument();
      expect(submitButton).toHaveAttribute('type', 'submit');
    });
  });

  describe('form submission', () => {
    it('should call onSubmit with correct data when form is submitted', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const commentTextarea = screen.getByLabelText(/感想/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '5');
      await user.type(commentTextarea, 'Wonderful rewatch!');
      await user.click(submitButton);

      await waitFor(() => {
        expect(mockOnSubmit).toHaveBeenCalledWith({
          watchedAt: '2025-01-20T19:30',
          rating: 5,
          comment: 'Wonderful rewatch!',
        });
      });
    });

    it('should handle null comment when not provided', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '4');
      await user.click(submitButton);

      await waitFor(() => {
        expect(mockOnSubmit).toHaveBeenCalledWith({
          watchedAt: '2025-01-20T19:30',
          rating: 4,
          comment: null,
        });
      });
    });

    it('should not submit if datetime is not provided', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.selectOptions(ratingSelect, '3');
      await user.click(submitButton);

      expect(mockOnSubmit).not.toHaveBeenCalled();
    });

    it('should not submit if rating is not provided', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.click(submitButton);

      expect(mockOnSubmit).not.toHaveBeenCalled();
    });
  });

  describe('loading state', () => {
    it('should disable inputs and button when loading', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} isLoading={true} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const commentTextarea = screen.getByLabelText(/感想/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      expect(datetimeInput).toBeDisabled();
      expect(ratingSelect).toBeDisabled();
      expect(commentTextarea).toBeDisabled();
      expect(submitButton).toBeDisabled();
    });

    it('should enable inputs when not loading', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} isLoading={false} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const commentTextarea = screen.getByLabelText(/感想/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      expect(datetimeInput).not.toBeDisabled();
      expect(ratingSelect).not.toBeDisabled();
      expect(commentTextarea).not.toBeDisabled();
      expect(submitButton).not.toBeDisabled();
    });
  });

  describe('error handling', () => {
    it('should display error message when submission fails', async () => {
      const user = userEvent.setup();
      const error = new Error('Failed to add viewing record');
      mockOnSubmit.mockRejectedValue(error);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '3');
      await user.click(submitButton);

      await waitFor(() => {
        expect(screen.getByText('Failed to add viewing record')).toBeInTheDocument();
      });
    });

    it('should display generic error message for unknown errors', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockRejectedValue('Unknown error');

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '2');
      await user.click(submitButton);

      await waitFor(() => {
        expect(screen.getByText(/エラーが発生しました/i)).toBeInTheDocument();
      });
    });
  });

  describe('form reset', () => {
    it('should clear form after successful submission', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i) as HTMLInputElement;
      const ratingSelect = screen.getByLabelText(/評価/i) as HTMLSelectElement;
      const commentTextarea = screen.getByLabelText(/感想/i) as HTMLTextAreaElement;
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '5');
      await user.type(commentTextarea, 'Great!');
      await user.click(submitButton);

      await waitFor(() => {
        expect(datetimeInput.value).toBe('');
        expect(ratingSelect.value).toBe('');
        expect(commentTextarea.value).toBe('');
      });
    });
  });

  describe('rating value conversion', () => {
    it('should convert rating string to number on submit', async () => {
      const user = userEvent.setup();
      mockOnSubmit.mockResolvedValue(undefined);

      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const datetimeInput = screen.getByLabelText(/視聴日時/i);
      const ratingSelect = screen.getByLabelText(/評価/i);
      const submitButton = screen.getByRole('button', { name: /記録を追加/i });

      await user.type(datetimeInput, '2025-01-20T19:30');
      await user.selectOptions(ratingSelect, '2');
      await user.click(submitButton);

      await waitFor(() => {
        expect(mockOnSubmit).toHaveBeenCalledWith(
          expect.objectContaining({
            rating: 2,
          })
        );
      });
    });
  });

  describe('accessibility', () => {
    it('should have proper labels for all inputs', () => {
      render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      expect(screen.getByLabelText(/視聴日時/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/評価/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/感想/i)).toBeInTheDocument();
    });

    it('should have proper form structure', () => {
      const { container } = render(<AddViewingRecordForm onSubmit={mockOnSubmit} />);

      const form = container.querySelector('form');
      expect(form).toBeInTheDocument();
    });
  });
});
