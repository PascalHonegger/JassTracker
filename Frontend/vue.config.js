const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 9090,
  },
  outputDir: "../Backend/bootstrap/src/main/resources/static",
  chainWebpack: (config) =>
    config.plugin("feature-flags").use(require("webpack").DefinePlugin, [
      {
        __VUE_OPTIONS_API__: "true", // Used by vue-draggable-next
        __VUE_PROD_DEVTOOLS__: "false",
      },
    ]),
});
