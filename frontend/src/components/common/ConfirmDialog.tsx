'use client';

import React from 'react';

interface ConfirmDialogProps {
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
  isLoading?: boolean;
  confirmButtonLabel?: string;
  cancelButtonLabel?: string;
  variant?: 'danger' | 'warning' | 'info';
}

export default function ConfirmDialog({
  title,
  message,
  onConfirm,
  onCancel,
  isLoading = false,
  confirmButtonLabel = '確認',
  cancelButtonLabel = 'キャンセル',
  variant = 'danger',
}: ConfirmDialogProps) {
  const getConfirmButtonClass = () => {
    switch (variant) {
      case 'danger':
        return 'btn-danger';
      case 'warning':
        return 'btn-warning';
      case 'info':
        return 'btn-info';
      default:
        return 'btn-primary';
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      onClick={onCancel}
      data-testid="confirm-dialog-overlay"
    >
      <div
        className="bg-white rounded-lg p-2xl max-w-sm w-11/12 shadow-lg"
        onClick={(e) => e.stopPropagation()}
      >
        <h2
          className="mb-md text-lg font-bold text-text-dark"
          data-testid="confirm-dialog-title"
        >
          {title}
        </h2>

        <p
          className="mb-2xl text-sm text-text-light leading-relaxed"
          data-testid="confirm-dialog-message"
        >
          {message}
        </p>

        <div className="flex gap-md justify-end">
          <button
            onClick={onCancel}
            disabled={isLoading}
            className="px-lg py-sm border border-gray-300 rounded-md bg-gray-100 text-text-dark hover:bg-gray-200 active:bg-gray-200 disabled:opacity-60 disabled:cursor-not-allowed transition-colors duration-200"
            data-testid="confirm-dialog-cancel-button"
          >
            {cancelButtonLabel}
          </button>

          <button
            onClick={onConfirm}
            disabled={isLoading}
            className={`${getConfirmButtonClass()} disabled:opacity-60 disabled:cursor-not-allowed`}
            data-testid="confirm-dialog-confirm-button"
          >
            {isLoading ? '処理中...' : confirmButtonLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
