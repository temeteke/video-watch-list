'use client';

import { ViewingRecordDetail } from '@/types/viewing-record';

interface ViewingRecordListProps {
  viewingRecords: ViewingRecordDetail[];
  onDelete: (recordId: number) => Promise<void>;
  isLoading?: boolean;
}

export default function ViewingRecordList({
  viewingRecords,
  onDelete,
  isLoading = false,
}: ViewingRecordListProps) {
  // Sort viewing records by recordedAt in descending order (newest first)
  const sortedRecords = [...viewingRecords].sort((a, b) => {
    return new Date(b.recordedAt).getTime() - new Date(a.recordedAt).getTime();
  });

  const handleDelete = async (recordId: number) => {
    try {
      await onDelete(recordId);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '削除に失敗しました';
      alert(errorMessage);
    }
  };

  if (sortedRecords.length === 0) {
    return <div className="py-lg text-center text-text-light">視聴履歴がありません</div>;
  }

  return (
    <div className="space-y-md">
      {sortedRecords.map((record, index) => (
        <div
          key={record.id}
          data-testid={`viewing-record-item-${index}`}
          className="card flex items-start justify-between gap-lg"
        >
          <div className="flex-1 space-y-sm">
            <div className="flex items-center gap-md">
              <span className="text-sm font-medium text-text-light">視聴日時:</span>
              <span className="text-sm text-text-dark">
                {new Date(record.watchedAt).toLocaleString('ja-JP')}
              </span>
            </div>
            <div className="flex items-center gap-md">
              <span className="text-sm font-medium text-text-light">評価:</span>
              <span className="text-sm font-bold text-primary">{record.rating}/5</span>
            </div>
            {record.comment && (
              <div className="pt-md">
                <span className="text-sm font-medium text-text-light">感想:</span>
                <p className="mt-sm text-sm text-text-dark leading-relaxed">
                  {record.comment}
                </p>
              </div>
            )}
          </div>
          <button
            type="button"
            onClick={() => handleDelete(record.id)}
            disabled={isLoading}
            className="btn-danger flex-shrink-0 disabled:opacity-60 disabled:cursor-not-allowed"
          >
            削除
          </button>
        </div>
      ))}
    </div>
  );
}
