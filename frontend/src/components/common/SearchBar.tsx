'use client';

import { useState } from 'react';
import { WatchStatus } from '@/types/episode';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Search, RotateCcw } from 'lucide-react';

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

  const handleStatusChange = (value: string) => {
    const status = value === 'all' ? undefined : (value as WatchStatus);
    setWatchStatus(status);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="mb-6">
      <div className="grid grid-cols-1 md:grid-cols-[1fr_150px_auto_auto] gap-3 items-end">
        <Input
          type="text"
          placeholder="タイトルを検索..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={isLoading}
          data-testid="search-input"
        />

        <Select value={watchStatus || 'all'} onValueChange={handleStatusChange}>
          <SelectTrigger disabled={isLoading} data-testid="watch-status-filter">
            <SelectValue placeholder="すべての状態" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">すべての状態</SelectItem>
            <SelectItem value="WATCHED">視聴済み</SelectItem>
            <SelectItem value="UNWATCHED">未視聴</SelectItem>
          </SelectContent>
        </Select>

        <Button
          onClick={handleSearch}
          disabled={isLoading}
          size="sm"
          data-testid="search-button"
        >
          <Search className="mr-2 h-4 w-4" />
          検索
        </Button>

        <Button
          onClick={handleClear}
          disabled={isLoading}
          variant="outline"
          size="sm"
          data-testid="clear-button"
        >
          <RotateCcw className="mr-2 h-4 w-4" />
          クリア
        </Button>
      </div>
    </div>
  );
}
