'use client';

import { useState } from 'react';
import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { AlertCircle } from 'lucide-react';

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
      toast.success('視聴記録を追加しました');
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'エラーが発生しました';
      setError(errorMessage);
      toast.error(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {error && (
        <Alert variant="destructive">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      <div>
        <Label htmlFor="watched-at" className="mb-2 block">
          視聴日時 <span className="text-danger-500">*</span>
        </Label>
        <Input
          id="watched-at"
          type="datetime-local"
          value={watchedAt}
          onChange={(e) => setWatchedAt(e.target.value)}
          disabled={isLoading}
          required
        />
      </div>

      <div>
        <Label htmlFor="rating" className="mb-2 block">
          評価 <span className="text-danger-500">*</span>
        </Label>
        <select
          id="rating"
          value={rating}
          onChange={(e) => setRating(e.target.value)}
          disabled={isLoading}
          required
          className="flex h-10 w-full rounded-md border border-neutral-200 bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-neutral-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0 disabled:cursor-not-allowed disabled:opacity-50"
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
        <Label htmlFor="comment" className="mb-2 block">
          感想
        </Label>
        <textarea
          id="comment"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="オプション: 感想を入力"
          disabled={isLoading}
          rows={4}
          className="flex w-full rounded-md border border-neutral-200 bg-white px-3 py-2 text-sm ring-offset-white placeholder:text-neutral-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0 disabled:cursor-not-allowed disabled:opacity-50"
        />
      </div>

      <Button type="submit" disabled={isLoading}>
        {isLoading ? '追加中...' : '記録を追加'}
      </Button>
    </form>
  );
}
