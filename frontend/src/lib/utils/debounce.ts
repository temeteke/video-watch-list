/**
 * デバウンス処理 - 関数の呼び出しを遅延させて、指定時間内の連続呼び出しを1回だけ実行
 * 検索入力などの頻繁なイベントのパフォーマンス最適化に使用
 */

type Func = (...args: any[]) => void | Promise<void>;

interface DebouncedFunction {
  <T extends Func>(func: T, wait: number): (...args: Parameters<T>) => void;
}

/**
 * 関数をデバウンスする
 * @param func デバウンス対象の関数
 * @param wait 遅延時間（ミリ秒）
 * @returns デバウンスされた関数
 *
 * 例:
 * const debouncedSearch = debounce((query: string) => {
 *   fetchSearchResults(query);
 * }, 300);
 *
 * // onChange イベントハンドラで使用
 * <input onChange={(e) => debouncedSearch(e.target.value)} />
 */
export function debounce<T extends Func>(func: T, wait: number): (...args: Parameters<T>) => void {
  let timeout: NodeJS.Timeout | null = null;

  return function (...args: Parameters<T>) {
    if (timeout) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(() => {
      func(...args);
      timeout = null;
    }, wait);
  };
}

/**
 * キャンセル可能なデバウンス関数を返す
 * @param func デバウンス対象の関数
 * @param wait 遅延時間（ミリ秒）
 * @returns デバウンスされた関数とキャンセルメソッド
 *
 * 例:
 * const { debounced, cancel } = createDebouncedFunction(
 *   (query: string) => fetchSearchResults(query),
 *   300
 * );
 *
 * // キャンセルする場合
 * cancel();
 */
export function createDebouncedFunction<T extends Func>(
  func: T,
  wait: number
): {
  debounced: (...args: Parameters<T>) => void;
  cancel: () => void;
} {
  let timeout: NodeJS.Timeout | null = null;

  const debounced = function (...args: Parameters<T>) {
    if (timeout) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(() => {
      func(...args);
      timeout = null;
    }, wait);
  };

  const cancel = () => {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
  };

  return { debounced, cancel };
}

/**
 * スロットル処理 - 一定時間内の複数呼び出しを間隔を空けて複数回実行
 * スクロール時のリサイズイベントなどで使用
 * @param func スロットル対象の関数
 * @param wait 間隔時間（ミリ秒）
 * @returns スロットルされた関数
 */
export function throttle<T extends Func>(func: T, wait: number): (...args: Parameters<T>) => void {
  let lastCall = 0;
  let timeout: NodeJS.Timeout | null = null;

  return function (...args: Parameters<T>) {
    const now = Date.now();

    if (now - lastCall >= wait) {
      func(...args);
      lastCall = now;
    } else {
      if (timeout) {
        clearTimeout(timeout);
      }

      timeout = setTimeout(() => {
        func(...args);
        lastCall = Date.now();
      }, wait - (now - lastCall));
    }
  };
}

/**
 * リーディングエッジ付きのデバウンス - 最初の呼び出しを即座に実行
 * @param func デバウンス対象の関数
 * @param wait 遅延時間（ミリ秒）
 * @returns デバウンスされた関数
 */
export function debounceWithLeading<T extends Func>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: NodeJS.Timeout | null = null;
  let lastCall = 0;

  return function (...args: Parameters<T>) {
    const now = Date.now();

    if (now - lastCall >= wait) {
      func(...args);
      lastCall = now;
    }

    if (timeout) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(() => {
      timeout = null;
    }, wait);
  };
}
