'use client';

import { useState } from 'react';
import Link from 'next/link';
import { TitleSummary } from '@/types/title';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import { showToast } from '@/components/common/Toast';

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
      showToast('タイトルを削除しました', 'success');
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '削除に失敗しました';
      showToast(errorMessage, 'error');
    } finally {
      setIsDeleting(false);
    }
  };

  const handleCancelDelete = () => {
    setDeleteConfirm({ isOpen: false });
  };

  const deletingTitle = deleteConfirm.id ? titles.find((t) => t.id === deleteConfirm.id) : null;

  if (titles.length === 0) {
    return <p className="text-center text-text-light py-lg">タイトルがありません</p>;
  }

  return (
    <>
      <div className="grid gap-md sm:grid-cols-2 lg:grid-cols-3">
        {titles.map((title) => (
          <div
            key={title.id}
            className="card hover:shadow-lg transition-shadow duration-200"
          >
            <div className="flex items-start justify-between gap-2">
              <Link
                href={`/titles/${title.id}`}
                className="text-primary font-medium hover:text-primary-dark text-lg block truncate flex-1"
              >
                {title.name}
              </Link>
              <button
                onClick={() => handleDeleteClick(title.id)}
                className="text-text-light hover:text-danger transition-colors duration-200 p-1 flex-shrink-0"
                aria-label="削除"
                title="削除"
              >
                🗑️
              </button>
            </div>
          </div>
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
