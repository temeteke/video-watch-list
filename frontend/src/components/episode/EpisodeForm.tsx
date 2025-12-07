'use client';

import { useState } from 'react';

interface EpisodeFormProps {
  onSubmit: (episodeInfo: string, urls: string[]) => Promise<void>;
  isLoading?: boolean;
}

export default function EpisodeForm({ onSubmit, isLoading = false }: EpisodeFormProps) {
  const [episodeInfo, setEpisodeInfo] = useState('');
  const [urls, setUrls] = useState<string[]>(['']);

  const handleUrlChange = (index: number, value: string) => {
    const newUrls = [...urls];
    newUrls[index] = value;
    setUrls(newUrls);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (!episodeInfo.trim()) {
        alert('エピソード情報を入力してください');
        return;
      }
      const filledUrls = urls.filter(url => url.trim());
      await onSubmit(episodeInfo, filledUrls);
      setEpisodeInfo('');
      setUrls(['']);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'エラーが発生しました';
      console.error('EpisodeForm error:', errorMessage);
      alert(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-md">
      <input
        type="text"
        value={episodeInfo}
        onChange={(e) => setEpisodeInfo(e.target.value)}
        placeholder="例: 第1話"
        disabled={isLoading}
        className="w-full"
      />
      <div className="space-y-md">
        {urls.map((url, index) => (
          <input
            key={index}
            type="url"
            value={url}
            onChange={(e) => handleUrlChange(index, e.target.value)}
            placeholder="https://..."
            disabled={isLoading}
            className="w-full"
          />
        ))}
      </div>
      <button
        type="submit"
        disabled={isLoading}
        className="btn-primary disabled:opacity-60 disabled:cursor-not-allowed w-full sm:w-auto"
      >
        エピソードを追加
      </button>
    </form>
  );
}
