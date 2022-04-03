import { createStore } from "vuex";

export const store = createStore({
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
