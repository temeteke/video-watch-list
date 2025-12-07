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
    return <p className="text-center text-text-light py-lg">タイトルがありません</p>;
  }

  return (
    <div className="grid gap-md sm:grid-cols-2 lg:grid-cols-3">
      {titles.map((title) => (
        <div
          key={title.id}
          className="card hover:shadow-lg transition-shadow duration-200"
        >
          <div className="flex items-start justify-between gap-2">
            <Link
              href={`/titles/${title.id}`}
              className="text-primary font-medium hover:text-primary-dark text-lg block truncate flex-1"
            >
              {title.name}
            </Link>
            <button
              onClick={() => handleDelete(title.id)}
              className="text-text-light hover:text-danger transition-colors duration-200 p-1 flex-shrink-0"
              aria-label="削除"
              title="削除"
            >
              🗑️
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}
