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
  const getConfirmButtonColor = () => {
    switch (variant) {
      case 'danger':
        return '#dc3545';
      case 'warning':
        return '#ffc107';
      case 'info':
        return '#17a2b8';
      default:
        return '#007bff';
    }
  };

  return (
    <div
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
      }}
      onClick={onCancel}
      data-testid="confirm-dialog-overlay"
    >
      <div
        style={{
          backgroundColor: 'white',
          borderRadius: '8px',
          padding: '24px',
          maxWidth: '400px',
          width: '90%',
          boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <h2
          style={{
            margin: '0 0 16px 0',
            fontSize: '18px',
            fontWeight: 'bold',
            color: '#333',
          }}
          data-testid="confirm-dialog-title"
        >
          {title}
        </h2>

        <p
          style={{
            margin: '0 0 24px 0',
            fontSize: '14px',
            color: '#666',
            lineHeight: '1.5',
          }}
          data-testid="confirm-dialog-message"
        >
          {message}
        </p>

        <div
          style={{
            display: 'flex',
            gap: '12px',
            justifyContent: 'flex-end',
          }}
        >
          <button
            onClick={onCancel}
            disabled={isLoading}
            style={{
              padding: '8px 16px',
              fontSize: '14px',
              border: '1px solid #ddd',
              borderRadius: '4px',
              backgroundColor: '#f8f9fa',
              color: '#333',
              cursor: isLoading ? 'not-allowed' : 'pointer',
              opacity: isLoading ? 0.6 : 1,
            }}
            data-testid="confirm-dialog-cancel-button"
          >
            {cancelButtonLabel}
          </button>

          <button
            onClick={onConfirm}
            disabled={isLoading}
            style={{
              padding: '8px 16px',
              fontSize: '14px',
              backgroundColor: getConfirmButtonColor(),
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: isLoading ? 'not-allowed' : 'pointer',
              opacity: isLoading ? 0.6 : 1,
            }}
            data-testid="confirm-dialog-confirm-button"
          >
            {isLoading ? '処理中...' : confirmButtonLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
