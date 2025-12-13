'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { episodesApi } from '@/lib/api/episodes';
import { viewingRecordsApi } from '@/lib/api/viewing-records';
import { EpisodeDetail } from '@/types/episode';
import CompleteEpisodeForm from '@/components/episode/CompleteEpisodeForm';
import AddViewingRecordForm from '@/components/episode/AddViewingRecordForm';
import ViewingRecordList from '@/components/episode/ViewingRecordList';
import Spinner from '@/components/common/Spinner';
import Breadcrumb from '@/components/common/Breadcrumb';

export default function EpisodeDetailPage() {
  const params = useParams();
  const router = useRouter();
  const episodeId = Number(params.id);

  const [episode, setEpisode] = useState<EpisodeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    loadEpisode();
  }, [episodeId]);

  const loadEpisode = async () => {
    try {
      setLoading(true);
      const data = await episodesApi.getEpisodeDetail(episodeId);
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
  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!episode) return <p>エピソードが見つかりません</p>;

  return (
    <main>
      <Breadcrumb
        items={[
          { label: 'ホーム', href: '/' },
          { label: `エピソード ${episode.episodeInfo}` },
        ]}
      />

      <button onClick={() => router.back()}>戻る</button>

      <section>
        <h1>{episode.episodeInfo}</h1>
        <div>
          <p>状態: {episode.watchStatus === 'watched' ? '視聴済み' : '未視聴'}</p>
          {episode.watchPageUrls.length > 0 && (
            <div>
              <h3>視聴ページ</h3>
              <ul>
                {episode.watchPageUrls.map((url, index) => (
                  <li key={index}>
                    <a href={url} target="_blank" rel="noopener noreferrer">
                      {url}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </section>

      <section>
        <h2>視聴完了</h2>
        {episode.watchStatus === 'unwatched' ? (
          <CompleteEpisodeForm
            onSubmit={handleCompleteEpisode}
            isLoading={isSubmitting}
          />
        ) : (
          <p>このエピソードは視聴済みです。</p>
        )}
      </section>

      <section>
        <h2>視聴履歴</h2>
        <ViewingRecordList
          viewingRecords={episode.viewingRecords}
          onDelete={handleDeleteViewingRecord}
        />
      </section>

      {episode.watchStatus === 'watched' && (
        <section>
          <h2>視聴記録を追加</h2>
          <AddViewingRecordForm
            onSubmit={handleAddViewingRecord}
            isLoading={isSubmitting}
          />
        </section>
      )}
    </main>
  );
}
