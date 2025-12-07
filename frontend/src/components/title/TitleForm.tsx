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
    <form onSubmit={handleSubmit} className="space-y-lg">
      <div className="form-group">
        <label htmlFor="name" className="form-label">
          タイトル名 *
        </label>
        <input
          id="name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="例: 進撃の巨人"
          disabled={isLoading}
          className="w-full"
        />
      </div>

      <div className="form-group">
        <label className="form-label">タイトル情報URL</label>
        <div className="space-y-md">
          {urls.map((url, index) => (
            <div key={index} className="flex gap-md items-center">
              <input
                type="url"
                value={url}
                onChange={(e) => handleUrlChange(index, e.target.value)}
                placeholder="https://..."
                disabled={isLoading}
                className="flex-1"
              />
              {urls.length > 1 && (
                <button
                  type="button"
                  onClick={() => removeUrlField(index)}
                  disabled={isLoading}
                  className="px-lg py-sm bg-danger text-white rounded-md font-medium transition-colors duration-200 min-h-touch-target hover:bg-danger-dark active:bg-danger-dark disabled:opacity-60 disabled:cursor-not-allowed whitespace-nowrap"
                >
                  削除
                </button>
              )}
            </div>
          ))}
        </div>
        <button
          type="button"
          onClick={addUrlField}
          disabled={isLoading}
          className="mt-md px-lg py-sm bg-info text-white rounded-md font-medium transition-colors duration-200 min-h-touch-target hover:bg-info-dark active:bg-info-dark disabled:opacity-60 disabled:cursor-not-allowed"
        >
          URL を追加
        </button>
      </div>

      {error && <div className="px-md py-md bg-danger-light text-danger rounded-md">{error}</div>}

      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary disabled:opacity-60 disabled:cursor-not-allowed px-xl py-md"
      >
        {isLoading ? '作成中...' : '作成'}
      </button>
    </form>
  );
}
