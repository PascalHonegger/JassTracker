import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import Toast, { PluginOptions, POSITION } from "vue-toastification";

import "vue-toastification/dist/index.css";
import "./styles/tailwind.scss";
import "./styles/custom.scss";
import "./styles/modal.scss";

const toastOptions: PluginOptions = {
  position: POSITION.BOTTOM_CENTER,
  closeOnClick: true,
  pauseOnHover: false,
  timeout: 5000,
};

createApp(App)
  .use(createPinia())
  .use(router)
  .use(Toast, toastOptions)
  .mount("#app");
