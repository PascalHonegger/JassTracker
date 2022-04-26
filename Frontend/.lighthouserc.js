module.exports = {
  ci: {
    collect: {
      settings: { chromeFlags: "--no-sandbox" },
    },
    assert: {
      preset: "lighthouse:recommended",
    },
    upload: {
      target: "filesystem",
      outputDir: "./lhci",
    },
  },
};
