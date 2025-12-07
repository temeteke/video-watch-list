import type { Metadata, Viewport } from 'next';
import React from 'react';
import './globals.css';
import Header from '@/components/common/Header';
import Toast from '@/components/common/Toast';

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
        <main className="max-w-screen-xl mx-auto px-xl py-lg">
          {children}
        </main>
        <Toast />
      </body>
    </html>
  );
}
