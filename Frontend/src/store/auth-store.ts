import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    name: "",
    loggedIn: false,
  }),
  actions: {
    setLoggedIn() {
      this.loggedIn = true;
    },
    logout() {
      this.loggedIn = false;
    },
  },
});
