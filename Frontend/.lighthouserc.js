module.exports = {
  ci: {
    collect: {
      settings: { chromeFlags: "--no-sandbox" },
      staticDistDir: "./dist",
    },
    assert: {
      preset: "lighthouse:no-pwa",
      assertions: {
        "color-contrast": "off",
        "csp-xss": "off",
        "render-blocking-resources": "off",
      },
    },
    upload: {
      target: "filesystem",
      outputDir: "./lhci",
    },
  },
};
