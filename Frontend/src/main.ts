import "vue-toastification/dist/index.css";
import "./styles/tailwind.css";
import "./styles/custom.css";
import "./styles/modal.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import Toast, { POSITION } from "vue-toastification";
import type { PluginOptions } from "vue-toastification";

import App from "./App.vue";
import router from "./router";

const toastOptions: PluginOptions = {
  position: POSITION.BOTTOM_CENTER,
  closeOnClick: true,
  pauseOnHover: false,
  timeout: 5000,
};

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(Toast, toastOptions);

app.mount("#app");
