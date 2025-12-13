'use client';

import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Plus, Home } from 'lucide-react';

export default function Header() {
  return (
    <header className="border-b bg-white shadow-sm sticky top-0 z-30">
      <nav className="max-w-screen-xl mx-auto flex justify-between items-center px-4 py-4">
        <Link href="/" className="text-2xl font-bold hover:opacity-80 transition-opacity duration-200 flex items-center gap-2">
          <span>📺</span>
          <span>Video Watch List</span>
        </Link>
        <div className="flex items-center gap-3">
          <Button variant="ghost" size="sm" asChild>
            <Link href="/">
              <Home className="mr-2 h-4 w-4" />
              ホーム
            </Link>
          </Button>
          <Button variant="default" size="sm" asChild>
            <Link href="/titles/new">
              <Plus className="mr-2 h-4 w-4" />
              新規タイトル追加
            </Link>
          </Button>
        </div>
      </nav>
    </header>
  );
}
