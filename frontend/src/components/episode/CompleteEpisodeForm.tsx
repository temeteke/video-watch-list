'use client';

import { useState } from 'react';
import { showToast } from '@/components/common/Toast';

interface CompleteEpisodeFormProps {
  onSubmit: (data: {
    watchedAt: string;
    rating: number;
    comment: string | null;
  }) => Promise<void>;
  isLoading?: boolean;
}

export default function CompleteEpisodeForm({
  onSubmit,
  isLoading = false,
}: CompleteEpisodeFormProps) {
  const [watchedAt, setWatchedAt] = useState('');
  const [rating, setRating] = useState('');
  const [comment, setComment] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validation
    if (!watchedAt) {
      setError('視聴完了日時を入力してください');
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
      showToast('視聴を完了しました', 'success');
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'エラーが発生しました';
      setError(errorMessage);
      showToast(errorMessage, 'error');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-lg">
      <div className="form-group">
        <label htmlFor="watched-at" className="form-label">
          視聴完了日時
        </label>
        <input
          id="watched-at"
          type="datetime-local"
          value={watchedAt}
          onChange={(e) => setWatchedAt(e.target.value)}
          disabled={isLoading}
          required
          className="w-full"
        />
      </div>

      <div className="form-group">
        <label htmlFor="rating" className="form-label">
          評価
        </label>
        <select
          id="rating"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          disabled={isLoading}
          required
          className="w-full"
        >
          <option value="">選択してください</option>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="comment" className="form-label">
          感想
        </label>
        <textarea
          id="comment"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="オプション: 感想を入力"
          disabled={isLoading}
          rows={4}
          className="w-full px-md py-sm border border-border-color rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-60"
        />
      </div>

      {error && <div className="px-md py-md bg-danger-light text-danger rounded-md">{error}</div>}

      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary disabled:opacity-60 disabled:cursor-not-allowed px-xl py-md"
      >
        視聴を完了する
      </button>
    </form>
  );
}
