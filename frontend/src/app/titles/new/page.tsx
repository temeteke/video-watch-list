'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { titlesApi } from '@/lib/api/titles';
import TitleForm from '@/components/title/TitleForm';
import { CreateTitleRequest } from '@/types/title';

export default function CreateTitlePage() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (data: CreateTitleRequest) => {
    try {
      setLoading(true);
      await titlesApi.createTitle(data);
      router.push('/');
    } catch (error) {
      throw error;
    } finally {
      setLoading(false);
    }
  };

  return (
    <main>
      <h1>タイトルを作成</h1>
      <TitleForm onSubmit={handleSubmit} isLoading={loading} />
    </main>
  );
}
