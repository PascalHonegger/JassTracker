import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { key, store } from "./store";

import "./styles/tailwind.scss";
import "./styles/custom.scss";
import "./styles/modal.scss";

createApp(App).use(router).use(store, key).mount("#app");
