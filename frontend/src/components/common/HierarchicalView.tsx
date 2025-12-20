'use client';

import Link from 'next/link';
import { TitleDetail } from '@/types/title';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';

interface HierarchicalViewProps {
  title: TitleDetail;
  onAddSeries?: () => void;
  onAddEpisode?: (seriesId: number) => void;
}

export default function HierarchicalView({
  title,
  onAddSeries,
  onAddEpisode,
}: HierarchicalViewProps) {
  const hasMultipleSeries = title.series.length > 1;
  const totalEpisodes = title.series.reduce((sum, s) => sum + s.episodes.length, 0);

  const episodeRow = (episode: TitleDetail['series'][0]['episodes'][0], seriesId: number) => (
    <Link
      href={`/series/${seriesId}/episodes/${episode.id}`}
      className="flex items-center justify-between p-3 bg-neutral-50 rounded-md border border-neutral-200 hover:bg-neutral-100 transition-colors duration-200"
    >
      <span className="text-sm text-neutral-700">
        {episode.episodeInfo}
      </span>
      <Badge variant={episode.watchStatus === 'watched' ? 'default' : 'secondary'}>
        {episode.watchStatus === 'watched' ? '視聴済み' : '未視聴'}
      </Badge>
    </Link>
  );

  return (
    <div className="space-y-6">
      <h2 className="text-3xl font-bold text-neutral-900">{title.name}</h2>

      {hasMultipleSeries ? (
        <div className="space-y-6">
          <div className="space-y-6">
            {title.series.map((series) => (
              <Card key={series.id} className="p-6">
                <h3 className="text-xl font-bold text-neutral-900 mb-4">
                  {series.name || 'デフォルト'}
                </h3>
                <div className="space-y-2 mb-6">
                  {series.episodes.map((episode) => (
                    <div key={episode.id}>
                      {episodeRow(episode, series.id)}
                    </div>
                  ))}
                </div>
                <Button
                  onClick={() => {
                    onAddEpisode?.(series.id);
                  }}
                  className="w-full"
                  variant="outline"
                >
                  エピソード追加
                </Button>
              </Card>
            ))}
          </div>
          <Button onClick={onAddSeries} className="w-full">
            シリーズ追加
          </Button>
        </div>
      ) : (
        <div className="space-y-6">
          <div className="space-y-2">
            {title.series[0]?.episodes.map((episode) => (
              <div key={episode.id}>
                {episodeRow(episode, title.series[0].id)}
              </div>
            ))}
          </div>
          <Button
            onClick={() => onAddEpisode?.(title.series[0]?.id)}
            className="w-full"
            variant="outline"
          >
            エピソード追加
          </Button>
          {totalEpisodes === 1 && (
            <Button onClick={onAddSeries} className="w-full">
              シリーズ追加
            </Button>
          )}
        </div>
      )}
    </div>
  );
}
