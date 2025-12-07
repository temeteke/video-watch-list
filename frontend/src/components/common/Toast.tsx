'use client';

import React, { useEffect, useState } from 'react';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

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

  const getToastClass = (type: ToastType) => {
    switch (type) {
      case 'success':
        return 'bg-success text-white';
      case 'error':
        return 'bg-danger text-white';
      case 'warning':
        return 'bg-warning text-text-dark';
      case 'info':
        return 'bg-info text-white';
      default:
        return 'bg-text-dark text-white';
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
      className="fixed top-lg right-lg z-50 max-w-sm"
      data-testid="toast-container"
    >
      {messages.map((msg) => (
        <div
          key={msg.id}
          className={`${getToastClass(msg.type)} px-lg py-md mb-md rounded-md shadow-md flex items-center gap-md animate-slideIn`}
          data-testid={`toast-${msg.type}`}
        >
          <span className="text-lg min-w-fit">{getIcon(msg.type)}</span>
          <span className="flex-1 text-sm leading-relaxed">{msg.message}</span>
          <button
            onClick={() => removeToast(msg.id)}
            className="bg-none border-none text-white text-xl cursor-pointer p-0 min-w-fit hover:opacity-80"
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
        .animate-slideIn {
          animation: slideIn 0.3s ease-out;
        }
      `}</style>
    </div>
  );
}
