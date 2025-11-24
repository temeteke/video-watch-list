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
    return <div>視聴履歴がありません</div>;
  }

  return (
    <ul>
      {sortedRecords.map((record, index) => (
        <li key={record.id} data-testid={`viewing-record-item-${index}`}>
          <div>
            <div>視聴日時: {record.watchedAt}</div>
            <div>評価: {record.rating}</div>
            {record.comment && <div>感想: {record.comment}</div>}
          </div>
          <button
            type="button"
            onClick={() => handleDelete(record.id)}
            disabled={isLoading}
          >
            削除
          </button>
        </li>
      ))}
    </ul>
  );
}
