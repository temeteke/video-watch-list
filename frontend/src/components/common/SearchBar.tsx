'use client';

import { useState } from 'react';
import { WatchStatus } from '@/types/episode';

interface SearchBarProps {
  onSearch: (query: string | undefined, watchStatus: WatchStatus | undefined) => void;
  isLoading?: boolean;
}

export default function SearchBar({ onSearch, isLoading = false }: SearchBarProps) {
  const [query, setQuery] = useState('');
  const [watchStatus, setWatchStatus] = useState<WatchStatus | undefined>();

  const handleSearch = () => {
    onSearch(query || undefined, watchStatus);
  };

  const handleClear = () => {
    setQuery('');
    setWatchStatus(undefined);
    onSearch(undefined, undefined);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="mb-xl">
      <div style={{ display: 'grid', gridTemplateColumns: '1fr auto auto auto', gap: '12px', alignItems: 'center' }}>
        <input
          type="text"
          placeholder="タイトルを検索..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={isLoading}
          className={`px-md py-sm border border-border-color rounded-md text-base focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-60 disabled:cursor-not-allowed`}
          data-testid="search-input"
        />

        <select
          value={watchStatus || ''}
          onChange={(e) => setWatchStatus((e.target.value as WatchStatus) || undefined)}
          disabled={isLoading}
          className={`px-md py-sm border border-border-color rounded-md text-base focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-60 disabled:cursor-not-allowed`}
          data-testid="watch-status-filter"
        >
          <option value="">すべての状態</option>
          <option value="WATCHED">視聴済み</option>
          <option value="UNWATCHED">未視聴</option>
        </select>

        <button
          onClick={handleSearch}
          disabled={isLoading}
          className="btn-primary disabled:opacity-60 disabled:cursor-not-allowed"
          data-testid="search-button"
        >
          検索
        </button>

        <button
          onClick={handleClear}
          disabled={isLoading}
          className="px-lg py-sm bg-gray-500 text-white rounded-md font-medium transition-colors duration-200 min-h-touch-target hover:bg-gray-600 active:bg-gray-600 disabled:opacity-60 disabled:cursor-not-allowed"
          data-testid="clear-button"
        >
          クリア
        </button>
      </div>
    </div>
  );
}
