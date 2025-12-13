'use client';

import { useState } from 'react';
import Link from 'next/link';
import { TitleSummary } from '@/types/title';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Trash2 } from 'lucide-react';
import { toast } from 'sonner';

interface TitleListProps {
  titles: TitleSummary[];
  onDelete?: (id: number) => Promise<void>;
}

export default function TitleList({ titles, onDelete }: TitleListProps) {
  const [deleteConfirm, setDeleteConfirm] = useState<{ isOpen: boolean; id?: number }>({
    isOpen: false,
  });
  const [isDeleting, setIsDeleting] = useState(false);

  const handleDeleteClick = (id: number) => {
    setDeleteConfirm({ isOpen: true, id });
  };

  const handleConfirmDelete = async () => {
    if (!deleteConfirm.id) return;

    try {
      setIsDeleting(true);
      await onDelete?.(deleteConfirm.id);
      setDeleteConfirm({ isOpen: false });
      toast.success('タイトルを削除しました');
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

  const deletingTitle = deleteConfirm.id ? titles.find((t) => t.id === deleteConfirm.id) : null;

  if (titles.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-neutral-500">タイトルがありません</p>
      </div>
    );
  }

  return (
    <>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {titles.map((title) => (
          <Card key={title.id} className="hover:shadow-lg transition-shadow duration-200 p-4">
            <div className="flex items-start justify-between gap-3">
              <Link
                href={`/titles/${title.id}`}
                className="text-primary-600 font-medium hover:text-primary-700 text-lg block truncate flex-1"
              >
                {title.name}
              </Link>
              <Button
                onClick={() => handleDeleteClick(title.id)}
                variant="ghost"
                size="sm"
                aria-label="削除"
                title="削除"
              >
                <Trash2 className="h-4 w-4 text-neutral-500 hover:text-danger-500" />
              </Button>
            </div>
          </Card>
        ))}
      </div>

      <ConfirmDialog
        isOpen={deleteConfirm.isOpen}
        title="タイトル削除"
        message={
          deletingTitle
            ? `「${deletingTitle.name}」を削除してもよろしいですか？`
            : 'このタイトルを削除してもよろしいですか？'
        }
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
