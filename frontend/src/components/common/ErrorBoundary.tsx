'use client';

import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
  fallback?: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

/**
 * ErrorBoundary - React Error Boundary
 * 子コンポーネントで発生したエラーをキャッチして表示
 */
export default class ErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error('Error caught by boundary:', error);
    console.error('Error info:', errorInfo);
  }

  resetError = () => {
    this.setState({ hasError: false, error: undefined });
  };

  render() {
    if (this.state.hasError) {
      return (
        this.props.fallback || (
          <div
            style={{
              padding: '24px',
              backgroundColor: '#f8d7da',
              border: '1px solid #f5c6cb',
              borderRadius: '4px',
              color: '#721c24',
              marginBottom: '20px',
            }}
            data-testid="error-boundary-fallback"
          >
            <h2 style={{ margin: '0 0 12px 0', fontSize: '16px' }}>
              エラーが発生しました
            </h2>
            <p style={{ margin: '0 0 16px 0', fontSize: '14px' }}>
              {this.state.error?.message ||
                'アプリケーションで予期しないエラーが発生しました。'}
            </p>
            <button
              onClick={this.resetError}
              style={{
                padding: '8px 16px',
                fontSize: '14px',
                backgroundColor: '#721c24',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
              }}
              data-testid="error-boundary-reset-button"
            >
              再試行
            </button>
          </div>
        )
      );
    }

    return this.props.children;
  }
}
