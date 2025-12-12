'use client';

import { useEffect, useState } from 'react';
import { titlesApi } from '@/lib/api/titles';
import { TitleSummary } from '@/types/title';
import { WatchStatus } from '@/types/episode';
import TitleList from '@/components/title/TitleList';
import SearchBar from '@/components/common/SearchBar';
import Spinner from '@/components/common/Spinner';

export default function HomePage() {
  const [titles, setTitles] = useState<TitleSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTitles();
  }, []);

  const loadTitles = async () => {
    try {
      setLoading(true);
      const data = await titlesApi.getAllTitles();
      setTitles(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'エラーが発生しました');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (query: string | undefined, watchStatus: WatchStatus | undefined) => {
    try {
      setLoading(true);
      setError(null);
      const data = await titlesApi.searchTitles(query, watchStatus);
      setTitles(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'エラーが発生しました');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await titlesApi.deleteTitle(id);
      setTitles(titles.filter(t => t.id !== id));
    } catch (err) {
      throw err;
    }
  };

  return (
    <div className="space-y-lg">
      <SearchBar onSearch={handleSearch} isLoading={loading} />

      {error && <p style={{ color: 'red' }}>{error}</p>}
      {loading ? (
        <Spinner fullScreen />
      ) : (
        <TitleList titles={titles} onDelete={handleDelete} />
      )}
    </div>
  );
}
