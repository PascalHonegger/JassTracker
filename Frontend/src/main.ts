import { createApp } from "vue";
import App from "./App.vue";
import { createStore } from "vuex";
import router from "./router";

import "./styles/tailwind.scss";
import "./styles/custom.scss";
import "./styles/modal.scss";

const store = createStore({
  state() {
    return {
      loggedIn: false,
    };
  },
  mutations: {
    setLoggedIn(state: { loggedIn: boolean }, loggedIn: boolean) {
      state.loggedIn = loggedIn;
    },
    logout(state: { loggedIn: boolean }) {
      state.loggedIn = false;
    },
  },
});

createApp(App).use(router).use(store).mount("#app");
