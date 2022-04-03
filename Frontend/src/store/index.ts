import { createStore, useStore as baseUseStore, Store } from "vuex";
import { InjectionKey } from "vue";

export interface State {
  loggedIn: boolean;
}

export const key: InjectionKey<Store<State>> = Symbol();

export const store = createStore<State>({
  state: {
    loggedIn: false,
  },
  mutations: {
    setLoggedIn(state, loggedIn: boolean) {
      state.loggedIn = loggedIn;
    },
    logout(state) {
      state.loggedIn = false;
    },
  },
});

// define your own `useStore` composition function to return typed store
export function useStore() {
  return baseUseStore(key);
}
