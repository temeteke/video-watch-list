'use client';

import { useState } from 'react';

interface AddViewingRecordFormProps {
  onSubmit: (data: {
    watchedAt: string;
    rating: number;
    comment: string | null;
  }) => Promise<void>;
  isLoading?: boolean;
}

export default function AddViewingRecordForm({
  onSubmit,
  isLoading = false,
}: AddViewingRecordFormProps) {
  const [watchedAt, setWatchedAt] = useState('');
  const [rating, setRating] = useState('');
  const [comment, setComment] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validation
    if (!watchedAt) {
      setError('視聴日時を入力してください');
      return;
    }

    if (!rating) {
      setError('評価を選択してください');
      return;
    }

    try {
      await onSubmit({
        watchedAt,
        rating: parseInt(rating, 10),
        comment: comment.trim() ? comment : null,
      });

      // Reset form after successful submission
      setWatchedAt('');
      setRating('');
      setComment('');
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'エラーが発生しました';
      setError(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="watched-at">視聴日時</label>
        <input
          id="watched-at"
          type="datetime-local"
          value={watchedAt}
          onChange={(e) => setWatchedAt(e.target.value)}
          disabled={isLoading}
          required
        />
      </div>

      <div>
        <label htmlFor="rating">評価</label>
        <select
          id="rating"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          disabled={isLoading}
          required
        >
          <option value="">選択してください</option>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </div>

      <div>
        <label htmlFor="comment">感想</label>
        <textarea
          id="comment"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="オプション: 感想を入力"
          disabled={isLoading}
          rows={4}
        />
      </div>

      {error && <div style={{ color: 'red' }}>{error}</div>}

      <button type="submit" disabled={isLoading}>
        記録を追加
      </button>
    </form>
  );
}
