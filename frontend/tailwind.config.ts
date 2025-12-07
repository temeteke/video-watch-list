import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: '#007bff',
        'primary-dark': '#0056b3',
        'primary-light': '#0d6efd',
        success: '#28a745',
        'success-dark': '#1e7e34',
        'success-light': '#198754',
        danger: '#dc3545',
        'danger-dark': '#bb2d3b',
        'danger-light': '#f8d7da',
        warning: '#ffc107',
        'warning-dark': '#e0a800',
        'warning-light': '#fff3cd',
        info: '#17a2b8',
        'info-dark': '#117a8b',
        'info-light': '#d1ecf1',
        'bg-light': '#f5f5f5',
        'text-dark': '#333333',
        'text-light': '#666666',
        'border-color': '#cccccc',
      },
      fontSize: {
        xs: '0.75rem',    // 12px
        sm: '0.875rem',   // 14px
        base: '1rem',     // 16px
        lg: '1.125rem',   // 18px
        xl: '1.25rem',    // 20px
        '2xl': '1.5rem',  // 24px
        '3xl': '1.875rem', // 30px
      },
      spacing: {
        xs: '4px',
        sm: '8px',
        md: '12px',
        lg: '16px',
        xl: '20px',
        '2xl': '24px',
        '3xl': '32px',
      },
      borderRadius: {
        xs: '2px',
        sm: '4px',
        md: '6px',
        lg: '8px',
        full: '9999px',
      },
      boxShadow: {
        xs: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
        sm: '0 1px 3px 0 rgba(0, 0, 0, 0.1)',
        md: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
      },
      minHeight: {
        'touch-target': '44px', // Accessibility: minimum touch target size
      },
    },
  },
  plugins: [],
}
export default config
