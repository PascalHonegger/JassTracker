const defaultTheme = require("tailwindcss/defaultTheme");
module.exports = {
  future: {
    // removeDeprecatedGapUtilities: true,
    // purgeLayersByDefault: true,
  },
  content: ["./public/**/*.html", "./src/**/*.vue"],
  theme: {
    fontFamily: {
      sans: ["Inter", ...defaultTheme.fontFamily["sans"]],
    },
    extend: {
      screens: {
        xl: { max: "1024px" },
        "2xl": { max: "1024px" },
      },
    },
  },
  variants: {
    cursor: ["hover"],
  },
  plugins: [],
};
