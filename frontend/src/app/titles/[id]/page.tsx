'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { titlesApi } from '@/lib/api/titles';
import { seriesApi } from '@/lib/api/series';
import { episodesApi } from '@/lib/api/episodes';
import { showToast } from '@/components/common/Toast';
import { TitleDetail } from '@/types/title';
import HierarchicalView from '@/components/common/HierarchicalView';
import SeriesForm from '@/components/series/SeriesForm';
import EpisodeForm from '@/components/episode/EpisodeForm';
import Spinner from '@/components/common/Spinner';
import Breadcrumb from '@/components/common/Breadcrumb';

export default function TitleDetailPage() {
  const params = useParams();
  const router = useRouter();
  const titleId = Number(params.id);

  const [title, setTitle] = useState<TitleDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showSeriesForm, setShowSeriesForm] = useState(false);
  const [showEpisodeForm, setShowEpisodeForm] = useState<number | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    loadTitle();
  }, [titleId]);

  const loadTitle = async () => {
    try {
      setLoading(true);
      const data = await titlesApi.getTitleDetail(titleId);
      setTitle(data as TitleDetail);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'エラーが発生しました');
    } finally {
      setLoading(false);
    }
  };

  const handleAddSeries = async (name: string) => {
    try {
      await seriesApi.createSeries(titleId, { name });
      await loadTitle();
      setShowSeriesForm(false);
    } catch (err) {
      throw err;
    }
  };

  const handleAddEpisode = async (seriesId: number, episodeInfo: string, urls: string[]) => {
    try {
      setIsSubmitting(true);
      console.log('Adding episode:', { seriesId, episodeInfo, urls });
      await episodesApi.createEpisode(seriesId, { episodeInfo, watchPageUrls: urls });
      console.log('Episode added successfully');
      await loadTitle();
      setShowEpisodeForm(null);
    } catch (err) {
      console.error('Failed to add episode:', err);
      const errorMessage = err instanceof Error ? err.message : String(err);
      showToast(`エピソード追加に失敗しました: ${errorMessage}`, 'error');
      throw err;
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) return <Spinner fullScreen />;
  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!title) return <p>タイトルが見つかりません</p>;

  return (
    <main>
      <Breadcrumb
        items={[
          { label: 'ホーム', href: '/' },
          { label: title.name },
        ]}
      />

      <button onClick={() => router.back()}>戻る</button>
      <HierarchicalView
        title={title}
        onAddSeries={() => setShowSeriesForm(!showSeriesForm)}
        onAddEpisode={(seriesId) => setShowEpisodeForm(showEpisodeForm === seriesId ? null : seriesId)}
      />

      {showSeriesForm && <SeriesForm onSubmit={handleAddSeries} />}
      {showEpisodeForm && title.series.find(s => s.id === showEpisodeForm) && (
        <EpisodeForm
          onSubmit={(info, urls) => handleAddEpisode(showEpisodeForm, info, urls)}
          isLoading={isSubmitting}
        />
      )}
    </main>
  );
}
