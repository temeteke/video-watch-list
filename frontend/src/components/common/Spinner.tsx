'use client';

type SpinnerSize = 'sm' | 'md' | 'lg';

interface SpinnerProps {
  size?: SpinnerSize;
  fullScreen?: boolean;
}

export default function Spinner({ size = 'md', fullScreen = false }: SpinnerProps) {
  const sizeClasses = {
    sm: 'w-6 h-6',
    md: 'w-12 h-12',
    lg: 'w-16 h-16',
  };

  const spinner = (
    <div
      className={`${sizeClasses[size]} border-4 border-primary border-t-transparent rounded-full animate-spin`}
      role="status"
      aria-label="読み込み中"
    >
      <span className="sr-only">読み込み中...</span>
    </div>
  );

  if (fullScreen) {
    return (
      <div className="fixed inset-0 bg-white bg-opacity-80 flex items-center justify-center z-40">
        {spinner}
      </div>
    );
  }

  return <div className="flex items-center justify-center">{spinner}</div>;
}
