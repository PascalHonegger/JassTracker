import { defineStore } from "pinia";

export const useMetaStore = defineStore("meta", {
  state: () => ({
    count: 0,
  }),
  getters: {
    isLoading(state): boolean {
      return state.count > 0;
    },
  },
  actions: {
    startLoading() {
      this.count++;
    },
    stopLoading() {
      this.count--;
    },
  },
});
