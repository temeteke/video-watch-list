'use client';

import { useState } from 'react';
import { toast } from 'sonner';
import { ViewingRecordDetail } from '@/types/viewing-record';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Trash2 } from 'lucide-react';

interface ViewingRecordListProps {
  viewingRecords: ViewingRecordDetail[];
  onDelete: (recordId: number) => Promise<void>;
}

export default function ViewingRecordList({
  viewingRecords,
  onDelete,
}: ViewingRecordListProps) {
  const [deleteConfirm, setDeleteConfirm] = useState<{ isOpen: boolean; id?: number }>({
    isOpen: false,
  });
  const [isDeleting, setIsDeleting] = useState(false);

  // Sort viewing records by recordedAt in descending order (newest first)
  const sortedRecords = [...viewingRecords].sort((a, b) => {
    return new Date(b.recordedAt).getTime() - new Date(a.recordedAt).getTime();
  });

  const handleDeleteClick = (recordId: number) => {
    setDeleteConfirm({ isOpen: true, id: recordId });
  };

  const handleConfirmDelete = async () => {
    if (!deleteConfirm.id) return;

    try {
      setIsDeleting(true);
      await onDelete(deleteConfirm.id);
      setDeleteConfirm({ isOpen: false });
      toast.success('視聴記録を削除しました');
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '削除に失敗しました';
      toast.error(errorMessage);
    } finally {
      setIsDeleting(false);
    }
  };

  const handleCancelDelete = () => {
    setDeleteConfirm({ isOpen: false });
  };

  if (sortedRecords.length === 0) {
    return <div className="py-6 text-center text-neutral-500">視聴履歴がありません</div>;
  }

  return (
    <>
      <div className="space-y-3">
        {sortedRecords.map((record, index) => (
          <Card
            key={record.id}
            data-testid={`viewing-record-item-${index}`}
            className="p-4 flex items-start justify-between gap-4"
          >
            <div className="flex-1 space-y-3">
              <div className="flex items-center gap-2">
                <span className="text-sm font-medium text-neutral-600">視聴日時:</span>
                <span className="text-sm text-neutral-900">
                  {new Date(record.watchedAt).toLocaleString('ja-JP')}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="text-sm font-medium text-neutral-600">評価:</span>
                <span className="text-sm font-bold text-primary-500">{record.rating}/5</span>
              </div>
              {record.comment && (
                <div className="pt-2">
                  <span className="text-sm font-medium text-neutral-600">感想:</span>
                  <p className="mt-2 text-sm text-neutral-900 leading-relaxed">
                    {record.comment}
                  </p>
                </div>
              )}
            </div>
            <Button
              type="button"
              variant="destructive"
              size="icon"
              onClick={() => handleDeleteClick(record.id)}
              disabled={isDeleting}
              className="flex-shrink-0"
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          </Card>
        ))}
      </div>

      <ConfirmDialog
        isOpen={deleteConfirm.isOpen}
        title="視聴記録削除"
        message="この視聴記録を削除してもよろしいですか？"
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
        confirmButtonLabel="削除"
        cancelButtonLabel="キャンセル"
        variant="danger"
        isLoading={isDeleting}
      />
    </>
  );
}
