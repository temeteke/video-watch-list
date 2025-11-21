'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { titlesApi } from '@/lib/api/titles';
import { TitleSummary } from '@/types/title';
import TitleList from '@/components/title/TitleList';

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

  const handleDelete = async (id: number) => {
    try {
      await titlesApi.deleteTitle(id);
      setTitles(titles.filter(t => t.id !== id));
    } catch (err) {
      throw err;
    }
  };

  return (
    <main>
      <h1>視聴予定リスト</h1>
      <Link href="/titles/new">新規タイトル作成</Link>

      {loading && <p>読み込み中...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && <TitleList titles={titles} onDelete={handleDelete} />}
    </main>
  );
}
