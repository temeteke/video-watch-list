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
    <div>
      <h2>{title.name}</h2>

      {hasMultipleSeries ? (
        <div>
          {title.series.map((series) => (
            <div key={series.id}>
              <h3>{series.name || 'デフォルト'}</h3>
              <ul>
                {series.episodes.map((episode) => (
                  <li key={episode.id}>
                    {episode.episodeInfo} - {episode.watchStatus}
                  </li>
                ))}
              </ul>
              <button onClick={() => onAddEpisode?.(series.id)}>
                エピソード追加
              </button>
            </div>
          ))}
          <button onClick={onAddSeries}>シリーズ追加</button>
        </div>
      ) : (
        <div>
          <ul>
            {title.series[0]?.episodes.map((episode) => (
              <li key={episode.id}>
                {episode.episodeInfo} - {episode.watchStatus}
              </li>
            ))}
          </ul>
          <button onClick={() => onAddEpisode?.(title.series[0]?.id)}>
            エピソード追加
          </button>
          {totalEpisodes === 1 && (
            <button onClick={onAddSeries}>シリーズ追加</button>
          )}
        </div>
      )}
    </div>
  );
}
