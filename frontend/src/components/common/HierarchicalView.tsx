'use client';

import { TitleDetail } from '@/types/title';

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

  return (
    <div className="space-y-lg">
      <h2 className="text-3xl font-bold text-text-dark">{title.name}</h2>

      {hasMultipleSeries ? (
        <div className="space-y-lg">
          <div className="space-y-lg">
            {title.series.map((series) => (
              <div key={series.id} className="card">
                <h3 className="text-xl font-bold text-text-dark mb-md">
                  {series.name || 'デフォルト'}
                </h3>
                <div className="space-y-sm mb-lg">
                  {series.episodes.map((episode) => (
                    <div
                      key={episode.id}
                      className="flex items-center justify-between p-md bg-gray-50 rounded-md border border-border-color"
                    >
                      <span className="text-sm text-text-dark">
                        {episode.episodeInfo}
                      </span>
                      <span
                        className={`text-xs px-md py-sm rounded-full font-medium ${
                          episode.watchStatus === 'watched'
                            ? 'bg-success text-white'
                            : 'bg-warning text-text-dark'
                        }`}
                      >
                        {episode.watchStatus === 'watched' ? '視聴済み' : '未視聴'}
                      </span>
                    </div>
                  ))}
                </div>
                <button
                  onClick={() => {
                    console.log('Episode add button clicked for series:', series.id);
                    onAddEpisode?.(series.id);
                  }}
                  className="btn-info w-full disabled:opacity-60 disabled:cursor-not-allowed"
                >
                  エピソード追加
                </button>
              </div>
            ))}
          </div>
          <button onClick={onAddSeries} className="btn-primary w-full">
            シリーズ追加
          </button>
        </div>
      ) : (
        <div className="space-y-lg">
          <div className="space-y-sm">
            {title.series[0]?.episodes.map((episode) => (
              <div
                key={episode.id}
                className="flex items-center justify-between p-md bg-gray-50 rounded-md border border-border-color"
              >
                <span className="text-sm text-text-dark">
                  {episode.episodeInfo}
                </span>
                <span
                  className={`text-xs px-md py-sm rounded-full font-medium ${
                    episode.watchStatus === 'watched'
                      ? 'bg-success text-white'
                      : 'bg-warning text-text-dark'
                  }`}
                >
                  {episode.watchStatus === 'watched' ? '視聴済み' : '未視聴'}
                </span>
              </div>
            ))}
          </div>
          <button
            onClick={() => onAddEpisode?.(title.series[0]?.id)}
            className="btn-info w-full disabled:opacity-60 disabled:cursor-not-allowed"
          >
            エピソード追加
          </button>
          {totalEpisodes === 1 && (
            <button onClick={onAddSeries} className="btn-primary w-full">
              シリーズ追加
            </button>
          )}
        </div>
      )}
    </div>
  );
}
