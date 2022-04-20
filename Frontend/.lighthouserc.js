module.exports = {
    ci: {
        collect: {
            settings: {chromeFlags: '--no-sandbox'},
        },
        assert: {
            preset: 'lighthouse:recommended',
        },
        upload: {
            target: 'temporary-public-storage',
        },
    },
};
