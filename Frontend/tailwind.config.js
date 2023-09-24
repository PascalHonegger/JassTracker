/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', "./src/**/*.{vue,js,ts,jsx,tsx}",],
  theme: {
    extend: {
      screens: {
        xl: { max: '1024px' },
        '2xl': { max: '1024px' }
      }
    }
  },
  variants: {
    cursor: ['hover']
  },
  plugins: []
}
