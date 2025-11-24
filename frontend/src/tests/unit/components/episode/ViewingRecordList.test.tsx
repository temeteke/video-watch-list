import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ViewingRecordList from '@/components/episode/ViewingRecordList';
import { ViewingRecordDetail } from '@/types/viewing-record';

describe('ViewingRecordList', () => {
  const mockOnDelete = jest.fn();

  const mockViewingRecords: ViewingRecordDetail[] = [
    {
      id: 1,
      episodeId: 100,
      watchedAt: '2025-01-15T20:00:00',
      rating: 4,
      comment: 'Great episode!',
      recordedAt: '2025-01-15T20:30:00',
    },
    {
      id: 2,
      episodeId: 100,
      watchedAt: '2025-01-10T19:00:00',
      rating: 5,
      comment: 'Excellent!',
      recordedAt: '2025-01-10T19:30:00',
    },
    {
      id: 3,
      episodeId: 100,
      watchedAt: '2025-01-05T18:00:00',
      rating: 3,
      comment: null,
      recordedAt: '2025-01-05T18:30:00',
    },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('rendering', () => {
    it('should render viewing records list', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      // Verify that all records are displayed
      expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      expect(screen.getByText(/Excellent!/)).toBeInTheDocument();
    });

    it('should display empty message when no viewing records', () => {
      render(
        <ViewingRecordList viewingRecords={[]} onDelete={mockOnDelete} />
      );

      expect(screen.getByText(/視聴履歴がありません/i)).toBeInTheDocument();
    });

    it('should render delete button for each viewing record', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const deleteButtons = screen.getAllByRole('button', { name: /削除/i });
      expect(deleteButtons).toHaveLength(mockViewingRecords.length);
    });

    it('should display watching date for each record', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      expect(screen.getByText(/2025-01-15/)).toBeInTheDocument();
      expect(screen.getByText(/2025-01-10/)).toBeInTheDocument();
      expect(screen.getByText(/2025-01-05/)).toBeInTheDocument();
    });

    it('should display rating for each record', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      expect(screen.getByText(/評価.*4/)).toBeInTheDocument();
      expect(screen.getByText(/評価.*5/)).toBeInTheDocument();
      expect(screen.getByText(/評価.*3/)).toBeInTheDocument();
    });
  });

  describe('sorting', () => {
    it('should display viewing records in newest first (descending) order', () => {
      const { container } = render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const records = container.querySelectorAll('[data-testid^="viewing-record-item-"]');
      expect(records).toHaveLength(mockViewingRecords.length);

      // Verify newest is first
      expect(records[0]).toHaveTextContent(/Great episode!/);
      expect(records[1]).toHaveTextContent(/Excellent!/);
      expect(records[2]).not.toHaveTextContent(/Great episode!/);
    });

    it('should maintain descending order even with unsorted input', () => {
      const unsortedRecords = [
        mockViewingRecords[2], // oldest
        mockViewingRecords[0], // newest
        mockViewingRecords[1], // middle
      ];

      const { container } = render(
        <ViewingRecordList
          viewingRecords={unsortedRecords}
          onDelete={mockOnDelete}
        />
      );

      const records = container.querySelectorAll('[data-testid^="viewing-record-item-"]');

      // Should be sorted newest to oldest by recordedAt
      expect(records[0]).toHaveTextContent(/Great episode!/); // recordedAt: 2025-01-15
      expect(records[1]).toHaveTextContent(/Excellent!/); // recordedAt: 2025-01-10
    });
  });

  describe('delete functionality', () => {
    it('should call onDelete with record id when delete button is clicked', async () => {
      const user = userEvent.setup();
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const deleteButtons = screen.getAllByRole('button', { name: /削除/i });
      await user.click(deleteButtons[0]);

      await waitFor(() => {
        expect(mockOnDelete).toHaveBeenCalledWith(mockViewingRecords[0].id);
      });
    });

    it('should call onDelete with correct record id for each delete action', async () => {
      const user = userEvent.setup();
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const deleteButtons = screen.getAllByRole('button', { name: /削除/i });

      // Delete first record
      await user.click(deleteButtons[0]);

      // Delete second record
      await user.click(deleteButtons[1]);

      await waitFor(() => {
        expect(mockOnDelete).toHaveBeenCalledTimes(2);
        expect(mockOnDelete).toHaveBeenNthCalledWith(1, mockViewingRecords[0].id);
        expect(mockOnDelete).toHaveBeenNthCalledWith(2, mockViewingRecords[1].id);
      });
    });
  });

  describe('comment handling', () => {
    it('should display comment when present', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      expect(screen.getByText(/Excellent!/)).toBeInTheDocument();
    });

    it('should handle null comment gracefully', () => {
      const recordsWithNullComment = [
        {
          ...mockViewingRecords[0],
          comment: null,
        },
      ];

      render(
        <ViewingRecordList
          viewingRecords={recordsWithNullComment}
          onDelete={mockOnDelete}
        />
      );

      // Should render without error
      expect(screen.getByTestId('viewing-record-item-0')).toBeInTheDocument();
    });

    it('should handle empty string comment', () => {
      const recordsWithEmptyComment = [
        {
          ...mockViewingRecords[0],
          comment: '',
        },
      ];

      render(
        <ViewingRecordList
          viewingRecords={recordsWithEmptyComment}
          onDelete={mockOnDelete}
        />
      );

      expect(screen.getByTestId('viewing-record-item-0')).toBeInTheDocument();
    });
  });

  describe('multiple records', () => {
    it('should display all records', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const recordItems = screen.getAllByTestId(/viewing-record-item-/);
      expect(recordItems).toHaveLength(mockViewingRecords.length);
    });

    it('should handle single record', () => {
      render(
        <ViewingRecordList
          viewingRecords={[mockViewingRecords[0]]}
          onDelete={mockOnDelete}
        />
      );

      expect(screen.getByText(/Great episode!/)).toBeInTheDocument();
      const deleteButtons = screen.getAllByRole('button', { name: /削除/i });
      expect(deleteButtons).toHaveLength(1);
    });

    it('should handle large number of records', () => {
      const manyRecords = Array.from({ length: 50 }, (_, i) => ({
        id: i,
        episodeId: 100,
        watchedAt: new Date(2025, 0, 50 - i).toISOString(),
        rating: (i % 5) + 1,
        comment: `Comment ${i}`,
        recordedAt: new Date(2025, 0, 50 - i).toISOString(),
      }));

      render(
        <ViewingRecordList
          viewingRecords={manyRecords}
          onDelete={mockOnDelete}
        />
      );

      const recordItems = screen.getAllByTestId(/viewing-record-item-/);
      expect(recordItems.length).toBeGreaterThan(0);
    });
  });

  describe('accessibility', () => {
    it('should have proper structure', () => {
      const { container } = render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const list = container.querySelector('ul') || container.querySelector('div[role="list"]');
      expect(list).toBeInTheDocument();
    });

    it('should have accessible delete buttons', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      const deleteButtons = screen.getAllByRole('button', { name: /削除/i });
      deleteButtons.forEach((button) => {
        expect(button).toHaveAttribute('type', 'button');
      });
    });
  });

  describe('data attributes', () => {
    it('should have proper data-testid attributes', () => {
      render(
        <ViewingRecordList
          viewingRecords={mockViewingRecords}
          onDelete={mockOnDelete}
        />
      );

      mockViewingRecords.forEach((_, index) => {
        expect(
          screen.getByTestId(`viewing-record-item-${index}`)
        ).toBeInTheDocument();
      });
    });
  });
});
