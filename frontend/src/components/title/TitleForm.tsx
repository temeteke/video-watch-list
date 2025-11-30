'use client';

import { useState } from 'react';
import { CreateTitleRequest } from '@/types/title';

interface TitleFormProps {
  onSubmit: (data: CreateTitleRequest) => Promise<void>;
  isLoading?: boolean;
}

export default function TitleForm({ onSubmit, isLoading = false }: TitleFormProps) {
  const [name, setName] = useState('');
  const [urls, setUrls] = useState<string[]>(['']);
  const [error, setError] = useState<string | null>(null);

  const handleUrlChange = (index: number, value: string) => {
    const newUrls = [...urls];
    newUrls[index] = value;
    setUrls(newUrls);
  };

  const addUrlField = () => {
    setUrls([...urls, '']);
  };

  const removeUrlField = (index: number) => {
    setUrls(urls.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!name.trim()) {
      setError('タイトル名は必須です');
      return;
    }

    try {
      const filledUrls = urls.filter(url => url.trim());
      await onSubmit({
        name: name.trim(),
        titleInfoUrls: filledUrls.length > 0 ? filledUrls : undefined,
      });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'エラーが発生しました');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="name">タイトル名 *</label>
        <input
          id="name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="例: 進撃の巨人"
          disabled={isLoading}
        />
      </div>

      <div>
        <label>タイトル情報URL</label>
        {urls.map((url, index) => (
          <div key={index}>
            <input
              type="url"
              value={url}
              onChange={(e) => handleUrlChange(index, e.target.value)}
              placeholder="https://..."
              disabled={isLoading}
            />
            {urls.length > 1 && (
              <button
                type="button"
                onClick={() => removeUrlField(index)}
                disabled={isLoading}
              >
                削除
              </button>
            )}
          </div>
        ))}
        <button type="button" onClick={addUrlField} disabled={isLoading}>
          URL を追加
        </button>
      </div>

      {error && <div style={{ color: 'red' }}>{error}</div>}

      <button type="submit" disabled={isLoading}>
        {isLoading ? '作成中...' : '作成'}
      </button>
    </form>
  );
}
