module.exports = {
  ci: {
    collect: {
      settings: { chromeFlags: "--no-sandbox" },
      staticDistDir: "./dist",
    },
    assert: {
      preset: "lighthouse:no-pwa",
    },
    upload: {
      target: "filesystem",
      outputDir: "./lhci",
    },
  },
};
