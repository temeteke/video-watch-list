'use client';

import { useState } from 'react';

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
    } catch (error) {
      alert(error instanceof Error ? error.message : 'エラーが発生しました');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="例: Season 2"
        disabled={isLoading}
      />
      <button type="submit" disabled={isLoading}>
        シリーズを追加
      </button>
    </form>
  );
}
