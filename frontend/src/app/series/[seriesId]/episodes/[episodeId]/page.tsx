'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { episodesApi } from '@/lib/api/episodes';
import { viewingRecordsApi } from '@/lib/api/viewing-records';
import { EpisodeDetail } from '@/types/episode';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { AlertCircle, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import CompleteEpisodeForm from '@/components/episode/CompleteEpisodeForm';
import AddViewingRecordForm from '@/components/episode/AddViewingRecordForm';
import ViewingRecordList from '@/components/episode/ViewingRecordList';
import Spinner from '@/components/common/Spinner';
import Breadcrumb from '@/components/common/Breadcrumb';

export default function EpisodeDetailPage() {
  const params = useParams();
  const router = useRouter();
  const episodeId = Number(params.episodeId);
  const seriesId = Number(params.seriesId);

  const [episode, setEpisode] = useState<EpisodeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    loadEpisode();
  }, [episodeId, seriesId]);

  const loadEpisode = async () => {
    try {
      setLoading(true);
      const data = await episodesApi.getEpisodeDetail(seriesId, episodeId);
      setEpisode(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'エラーが発生しました');
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteEpisode = async (data: {
    watchedAt: string;
    rating: number;
    comment: string | null;
  }) => {
    try {
      setIsSubmitting(true);
      await viewingRecordsApi.completeEpisode(episodeId, data);
      await loadEpisode();
    } catch (err) {
      throw err;
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleAddViewingRecord = async (data: {
    watchedAt: string;
    rating: number;
    comment: string | null;
  }) => {
    try {
      setIsSubmitting(true);
      await viewingRecordsApi.addViewingRecord(episodeId, data);
      await loadEpisode();
    } catch (err) {
      throw err;
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteViewingRecord = async (recordId: number) => {
    try {
      setIsSubmitting(true);
      await viewingRecordsApi.deleteViewingRecord(recordId);
      await loadEpisode();
    } catch (err) {
      throw err;
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) return <Spinner fullScreen />;
  if (error) {
    return (
      <main className="space-y-6">
        <Breadcrumb
          items={[
            { label: 'ホーム', href: '/' },
          ]}
        />
        <Alert variant="destructive">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
        <Button onClick={() => router.back()} variant="outline">
          <ArrowLeft className="mr-2 h-4 w-4" />
          戻る
        </Button>
      </main>
    );
  }
  if (!episode) {
    return (
      <main className="space-y-6">
        <Breadcrumb
          items={[
            { label: 'ホーム', href: '/' },
          ]}
        />
        <Alert variant="destructive">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>エピソードが見つかりません</AlertDescription>
        </Alert>
        <Button onClick={() => router.back()} variant="outline">
          <ArrowLeft className="mr-2 h-4 w-4" />
          戻る
        </Button>
      </main>
    );
  }

  return (
    <main className="space-y-6">
      <Breadcrumb
        items={[
          { label: 'ホーム', href: '/' },
          { label: `エピソード ${episode.episodeInfo}` },
        ]}
      />

      <Button onClick={() => router.back()} variant="outline">
        <ArrowLeft className="mr-2 h-4 w-4" />
        戻る
      </Button>

      <section className="space-y-4">
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">{episode.episodeInfo}</h1>
          <Badge variant={episode.watchStatus === 'watched' ? 'default' : 'secondary'}>
            {episode.watchStatus === 'watched' ? '視聴済み' : '未視聴'}
          </Badge>
        </div>
        {episode.watchPageUrls.length > 0 && (
          <div className="space-y-2">
            <h3 className="text-lg font-semibold text-neutral-900">視聴ページ</h3>
            <ul className="space-y-2">
              {episode.watchPageUrls.map((url, index) => (
                <li key={index}>
                  <a
                    href={url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-primary-500 hover:text-primary-600 underline text-sm break-all"
                  >
                    {url}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        )}
      </section>

      <section className="space-y-4">
        <h2 className="text-2xl font-bold text-neutral-900">視聴完了</h2>
        {episode.watchStatus === 'unwatched' ? (
          <CompleteEpisodeForm
            onSubmit={handleCompleteEpisode}
            isLoading={isSubmitting}
          />
        ) : (
          <p className="text-neutral-600">このエピソードは視聴済みです。</p>
        )}
      </section>

      <section className="space-y-4">
        <h2 className="text-2xl font-bold text-neutral-900">視聴履歴</h2>
        <ViewingRecordList
          viewingRecords={episode.viewingRecords}
          onDelete={handleDeleteViewingRecord}
        />
      </section>

      {episode.watchStatus === 'watched' && (
        <section className="space-y-4">
          <h2 className="text-2xl font-bold text-neutral-900">視聴記録を追加</h2>
          <AddViewingRecordForm
            onSubmit={handleAddViewingRecord}
            isLoading={isSubmitting}
          />
        </section>
      )}
    </main>
  );
}
