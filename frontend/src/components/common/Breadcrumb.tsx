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
      className="flex items-center space-x-sm text-sm mb-lg"
      aria-label="パンくずリスト"
    >
      {items.map((item, idx) => (
        <Fragment key={idx}>
          {idx > 0 && (
            <span className="text-text-light" aria-hidden="true">
              /
            </span>
          )}
          {item.href ? (
            <Link
              href={item.href}
              className="text-primary hover:underline hover:text-primary-dark transition-colors duration-200"
            >
              {item.label}
            </Link>
          ) : (
            <span className="text-text-dark">{item.label}</span>
          )}
        </Fragment>
      ))}
    </nav>
  );
}
