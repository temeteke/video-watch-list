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
    <div style={{ marginBottom: '20px' }}>
      <div style={{ display: 'flex', gap: '10px' }}>
        <input
          type="text"
          placeholder="タイトルを検索..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={isLoading}
          style={{
            flex: 1,
            padding: '8px 12px',
            fontSize: '16px',
            border: '1px solid #ccc',
            borderRadius: '4px',
          }}
          data-testid="search-input"
        />

        <select
          value={watchStatus || ''}
          onChange={(e) => setWatchStatus((e.target.value as WatchStatus) || undefined)}
          disabled={isLoading}
          style={{
            padding: '8px 12px',
            fontSize: '16px',
            border: '1px solid #ccc',
            borderRadius: '4px',
            minWidth: '150px',
          }}
          data-testid="watch-status-filter"
        >
          <option value="">すべての状態</option>
          <option value="WATCHED">視聴済み</option>
          <option value="UNWATCHED">未視聴</option>
        </select>

        <button
          onClick={handleSearch}
          disabled={isLoading}
          style={{
            padding: '8px 16px',
            fontSize: '16px',
            backgroundColor: '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: isLoading ? 'not-allowed' : 'pointer',
            opacity: isLoading ? 0.6 : 1,
          }}
          data-testid="search-button"
        >
          検索
        </button>

        <button
          onClick={handleClear}
          disabled={isLoading}
          style={{
            padding: '8px 16px',
            fontSize: '16px',
            backgroundColor: '#6c757d',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: isLoading ? 'not-allowed' : 'pointer',
            opacity: isLoading ? 0.6 : 1,
          }}
          data-testid="clear-button"
        >
          クリア
        </button>
      </div>
    </div>
  );
}
