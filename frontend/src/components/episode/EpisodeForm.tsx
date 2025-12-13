'use client';

import { useState } from 'react';
import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Plus, Trash2 } from 'lucide-react';

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

  const addUrlField = () => {
    setUrls([...urls, '']);
  };

  const removeUrlField = (index: number) => {
    setUrls(urls.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (!episodeInfo.trim()) {
        toast.error('エピソード情報を入力してください');
        return;
      }
      const filledUrls = urls.filter(url => url.trim());
      await onSubmit(episodeInfo, filledUrls);
      setEpisodeInfo('');
      setUrls(['']);
      toast.success('エピソードを追加しました');
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'エラーが発生しました';
      console.error('EpisodeForm error:', errorMessage);
      toast.error(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div>
        <Label htmlFor="episode-info" className="mb-2 block">
          エピソード情報 <span className="text-danger-500">*</span>
        </Label>
        <Input
          id="episode-info"
          type="text"
          value={episodeInfo}
          onChange={(e) => setEpisodeInfo(e.target.value)}
          placeholder="例: 第1話"
          disabled={isLoading}
        />
      </div>

      <div>
        <Label className="mb-3 block">視聴ページURL</Label>
        <div className="space-y-3">
          {urls.map((url, index) => (
            <div key={index} className="flex gap-3">
              <Input
                type="url"
                value={url}
                onChange={(e) => handleUrlChange(index, e.target.value)}
                placeholder="https://..."
                disabled={isLoading}
                className="flex-1"
              />
              {urls.length > 1 && (
                <Button
                  type="button"
                  variant="destructive"
                  size="sm"
                  onClick={() => removeUrlField(index)}
                  disabled={isLoading}
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              )}
            </div>
          ))}
        </div>
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={addUrlField}
          disabled={isLoading}
          className="mt-3"
        >
          <Plus className="mr-2 h-4 w-4" />
          URLを追加
        </Button>
      </div>

      <Button type="submit" disabled={isLoading}>
        {isLoading ? '追加中...' : 'エピソードを追加'}
      </Button>
    </form>
  );
}
