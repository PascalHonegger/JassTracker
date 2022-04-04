import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";

import "./styles/tailwind.scss";
import "./styles/custom.scss";
import "./styles/modal.scss";

createApp(App).use(createPinia()).use(router).mount("#app");
