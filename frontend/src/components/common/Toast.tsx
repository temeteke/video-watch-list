'use client';

import React, { useEffect, useState } from 'react';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

interface ToastProps {
  message: string;
  type?: ToastType;
  duration?: number;
  onClose?: () => void;
}

interface ToastMessage {
  id: string;
  message: string;
  type: ToastType;
}

// グローバルなトーストコンテキストを管理するためのシングルトン
let toastQueue: ToastMessage[] = [];
let toastListeners: Array<(messages: ToastMessage[]) => void> = [];

export const showToast = (message: string, type: ToastType = 'info', duration = 3000) => {
  const id = `${Date.now()}-${Math.random()}`;
  const newMessage: ToastMessage = { id, message, type };

  toastQueue.push(newMessage);
  notifyListeners();

  if (duration > 0) {
    setTimeout(() => {
      toastQueue = toastQueue.filter((t) => t.id !== id);
      notifyListeners();
    }, duration);
  }

  return id;
};

export const removeToast = (id: string) => {
  toastQueue = toastQueue.filter((t) => t.id !== id);
  notifyListeners();
};

const notifyListeners = () => {
  toastListeners.forEach((listener) => listener([...toastQueue]));
};

const subscribeToToasts = (listener: (messages: ToastMessage[]) => void) => {
  toastListeners.push(listener);
  return () => {
    toastListeners = toastListeners.filter((l) => l !== listener);
  };
};

// トースト表示コンポーネント
export default function Toast() {
  const [messages, setMessages] = useState<ToastMessage[]>([]);

  useEffect(() => {
    const unsubscribe = subscribeToToasts(setMessages);
    return unsubscribe;
  }, []);

  const getBackgroundColor = (type: ToastType) => {
    switch (type) {
      case 'success':
        return '#28a745';
      case 'error':
        return '#dc3545';
      case 'warning':
        return '#ffc107';
      case 'info':
        return '#17a2b8';
      default:
        return '#333';
    }
  };

  const getIcon = (type: ToastType) => {
    switch (type) {
      case 'success':
        return '✓';
      case 'error':
        return '✕';
      case 'warning':
        return '⚠';
      case 'info':
        return 'ℹ';
      default:
        return '•';
    }
  };

  return (
    <div
      style={{
        position: 'fixed',
        top: '20px',
        right: '20px',
        zIndex: 9999,
        maxWidth: '400px',
      }}
      data-testid="toast-container"
    >
      {messages.map((msg) => (
        <div
          key={msg.id}
          style={{
            backgroundColor: getBackgroundColor(msg.type),
            color: 'white',
            padding: '16px',
            marginBottom: '12px',
            borderRadius: '4px',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.2)',
            display: 'flex',
            alignItems: 'center',
            gap: '12px',
            animation: 'slideIn 0.3s ease-out',
          }}
          data-testid={`toast-${msg.type}`}
        >
          <span style={{ fontSize: '18px', minWidth: '24px' }}>{getIcon(msg.type)}</span>
          <span style={{ flex: 1, fontSize: '14px', lineHeight: '1.5' }}>
            {msg.message}
          </span>
          <button
            onClick={() => removeToast(msg.id)}
            style={{
              background: 'none',
              border: 'none',
              color: 'white',
              fontSize: '20px',
              cursor: 'pointer',
              padding: '0',
              minWidth: '24px',
            }}
            data-testid={`toast-close-${msg.id}`}
          >
            ✕
          </button>
        </div>
      ))}
      <style>{`
        @keyframes slideIn {
          from {
            transform: translateX(400px);
            opacity: 0;
          }
          to {
            transform: translateX(0);
            opacity: 1;
          }
        }
      `}</style>
    </div>
  );
}
