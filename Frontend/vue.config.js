const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 9090,
  },
  chainWebpack: (config) =>
    config.plugin("feature-flags").use(require("webpack").DefinePlugin, [
      {
        __VUE_OPTIONS_API__: "false",
        __VUE_PROD_DEVTOOLS__: "false",
      },
    ]),
});
