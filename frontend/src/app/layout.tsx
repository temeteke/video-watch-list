import type { Metadata, Viewport } from 'next';
import React from 'react';
import './globals.css';

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
        <header>
          <h1>視聴予定リスト</h1>
        </header>
        <main>
          {children}
        </main>
      </body>
    </html>
  );
}
