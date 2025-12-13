'use client';

import Link from 'next/link';
import { Fragment } from 'react';

interface BreadcrumbItem {
  label: string;
  href?: string;
}

interface BreadcrumbProps {
  items: BreadcrumbItem[];
}

export default function Breadcrumb({ items }: BreadcrumbProps) {
  return (
    <nav
      className="flex items-center gap-2 text-sm mb-6"
      aria-label="パンくずリスト"
    >
      {items.map((item, idx) => (
        <Fragment key={idx}>
          {idx > 0 && (
            <span className="text-neutral-400" aria-hidden="true">
              /
            </span>
          )}
          {item.href ? (
            <Link
              href={item.href}
              className="text-primary-600 hover:underline hover:text-primary-700 transition-colors duration-200"
            >
              {item.label}
            </Link>
          ) : (
            <span className="text-neutral-700">{item.label}</span>
          )}
        </Fragment>
      ))}
    </nav>
  );
}
