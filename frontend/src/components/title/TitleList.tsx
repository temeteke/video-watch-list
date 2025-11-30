'use client';

import Link from 'next/link';
import { TitleSummary } from '@/types/title';

interface TitleListProps {
  titles: TitleSummary[];
  onDelete?: (id: number) => Promise<void>;
}

export default function TitleList({ titles, onDelete }: TitleListProps) {
  const handleDelete = async (id: number) => {
    if (window.confirm('このタイトルを削除しますか？')) {
      try {
        await onDelete?.(id);
      } catch (error) {
        alert(error instanceof Error ? error.message : '削除に失敗しました');
      }
    }
  };

  if (titles.length === 0) {
    return <p>タイトルがありません</p>;
  }

  return (
    <ul>
      {titles.map((title) => (
        <li key={title.id}>
          <Link href={`/titles/${title.id}`}>{title.name}</Link>
          <button onClick={() => handleDelete(title.id)}>削除</button>
        </li>
      ))}
    </ul>
  );
}
