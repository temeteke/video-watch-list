'use client';

import { useState } from 'react';
import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

interface SeriesFormProps {
  onSubmit: (name: string) => Promise<void>;
  isLoading?: boolean;
}

export default function SeriesForm({ onSubmit, isLoading = false }: SeriesFormProps) {
  const [name, setName] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      toast.error('シリーズ名を入力してください');
      return;
    }
    try {
      await onSubmit(name);
      setName('');
      toast.success('シリーズを追加しました');
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'エラーが発生しました';
      toast.error(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-3">
      <Input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="例: Season 2"
        disabled={isLoading}
        className="flex-1"
      />
      <Button
        type="submit"
        disabled={isLoading}
      >
        シリーズを追加
      </Button>
    </form>
  );
}
