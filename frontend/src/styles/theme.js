// ==========================================================
// styles/theme.js
// JS mirror of CSS custom properties in main.css — use this
// wherever a JS object is needed (charts, inline styles,
// third-party components that don't read CSS vars).
// ==========================================================

const theme = {
  color: {
    bg: "#0b1120",
    surface: "#131c31",
    surfaceRaised: "#1b2540",
    border: "#263257",
    text: "#e7ebf5",
    textMuted: "#93a0bf",
    accent: "#f5b942",
    accentDim: "#c9942d",
    danger: "#f2726b",
    success: "#7cd9a5",
  },

  font: {
    display: `"Source Serif 4", "Georgia", serif`,
    body: `"Inter", -apple-system, BlinkMacSystemFont, sans-serif`,
  },

  radius: {
    default: "10px",
    sm: "6px",
  },

  shadow: "0 8px 24px rgba(0, 0, 0, 0.35)",

  maxWidth: "960px",
};

export default theme;