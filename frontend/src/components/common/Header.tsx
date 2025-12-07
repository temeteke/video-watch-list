'use client';

import Link from 'next/link';

export default function Header() {
  return (
    <header className="bg-primary text-white py-lg px-xl shadow-md sticky top-0 z-30">
      <nav className="max-w-screen-xl mx-auto flex justify-between items-center">
        <Link href="/" className="text-2xl font-bold hover:opacity-90 transition-opacity duration-200">
          📺 Video Watch List
        </Link>
        <div className="flex items-center gap-md">
          <Link
            href="/"
            className="px-lg py-sm bg-white/10 text-white rounded-md font-medium hover:bg-white/20 transition-colors duration-200"
          >
            ホーム
          </Link>
          <Link
            href="/titles/new"
            className="px-lg py-sm bg-white text-primary rounded-md font-medium hover:bg-gray-100 transition-colors duration-200 whitespace-nowrap"
          >
            新規タイトル追加
          </Link>
        </div>
      </nav>
    </header>
  );
}
