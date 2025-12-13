'use client';

import React from 'react';
import { Button } from '@/components/ui/button';
import {
  AlertDialog,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';

interface ConfirmDialogProps {
  isOpen: boolean;
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
  isOpen,
  title,
  message,
  onConfirm,
  onCancel,
  isLoading = false,
  confirmButtonLabel = '確認',
  cancelButtonLabel = 'キャンセル',
  variant = 'danger',
}: ConfirmDialogProps) {
  const getConfirmVariant = () => {
    switch (variant) {
      case 'danger':
        return 'destructive';
      case 'warning':
        return 'default';
      case 'info':
        return 'default';
      default:
        return 'default';
    }
  };

  return (
    <AlertDialog open={isOpen} onOpenChange={onCancel}>
      <AlertDialogContent data-testid="confirm-dialog-overlay">
        <AlertDialogHeader>
          <AlertDialogTitle data-testid="confirm-dialog-title">{title}</AlertDialogTitle>
          <AlertDialogDescription data-testid="confirm-dialog-message">
            {message}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <div className="flex gap-3 justify-end">
          <AlertDialogCancel data-testid="confirm-dialog-cancel-button">
            {cancelButtonLabel}
          </AlertDialogCancel>
          <Button
            onClick={onConfirm}
            disabled={isLoading}
            variant={getConfirmVariant()}
            size="sm"
            data-testid="confirm-dialog-confirm-button"
          >
            {isLoading ? '処理中...' : confirmButtonLabel}
          </Button>
        </div>
      </AlertDialogContent>
    </AlertDialog>
  );
}
