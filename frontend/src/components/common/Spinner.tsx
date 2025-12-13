'use client';

import { Loader2 } from 'lucide-react';

type SpinnerSize = 'sm' | 'md' | 'lg';

interface SpinnerProps {
  size?: SpinnerSize;
  fullScreen?: boolean;
}

export default function Spinner({ size = 'md', fullScreen = false }: SpinnerProps) {
  const sizeClasses = {
    sm: 'h-4 w-4',
    md: 'h-8 w-8',
    lg: 'h-12 w-12',
  };

  const spinner = (
    <div
      role="status"
      aria-label="読み込み中"
    >
      <Loader2 className={`${sizeClasses[size]} animate-spin text-primary-500`} />
      <span className="sr-only">読み込み中...</span>
    </div>
  );

  if (fullScreen) {
    return (
      <div className="fixed inset-0 bg-white/80 flex items-center justify-center z-40">
        {spinner}
      </div>
    );
  }

  return <div className="flex items-center justify-center">{spinner}</div>;
}
