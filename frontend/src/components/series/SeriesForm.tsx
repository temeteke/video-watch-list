'use client';

import { useState } from 'react';
import { showToast } from '@/components/common/Toast';

interface SeriesFormProps {
  onSubmit: (name: string) => Promise<void>;
  isLoading?: boolean;
}

export default function SeriesForm({ onSubmit, isLoading = false }: SeriesFormProps) {
  const [name, setName] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await onSubmit(name);
      setName('');
      showToast('シリーズを追加しました', 'success');
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'エラーが発生しました';
      showToast(errorMessage, 'error');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-md">
      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="例: Season 2"
        disabled={isLoading}
        className="flex-1"
      />
      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary disabled:opacity-60 disabled:cursor-not-allowed whitespace-nowrap"
      >
        シリーズを追加
      </button>
    </form>
  );
}
