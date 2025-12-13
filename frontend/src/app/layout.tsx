import type { Metadata, Viewport } from 'next';
import React from 'react';
import './globals.css';
import Header from '@/components/common/Header';
import { Toaster } from 'sonner';

export const metadata: Metadata = {
  title: '視聴予定リスト',
  description: 'ドラマ・アニメ・映画の視聴予定と履歴を管理するアプリケーション',
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ja" suppressHydrationWarning>
      <body>
        <Header />
        <main className="max-w-screen-xl mx-auto px-4 py-6">
          {children}
        </main>
        <Toaster />
      </body>
    </html>
  );
}
