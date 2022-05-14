import { defineStore } from "pinia";
import { useContractStore } from "@/store/contract-store";
import { useGameStore } from "@/store/game-store";
import { useTableStore } from "@/store/table-store";
import { loginGuestPlayer, loginPlayer } from "@/services/auth-service";
import { clearToken, setToken } from "@/services/requests";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    token: "",
    name: "",
    loggedIn: false,
  }),
  actions: {
    async loginPlayer(username: string, password: string): Promise<boolean> {
      try {
        const { token } = await loginPlayer(username, password);
        this.token = token;
        setToken(token);
        await this.setLoggedIn();
        return true;
      } catch (e) {
        return false;
      }
    },
    async guestAccess() {
      const { token } = await loginGuestPlayer();
      this.token = token;
      setToken(token);
      await this.setLoggedIn();
    },
    async setLoggedIn() {
      this.loggedIn = true;
      // Load all available contracts once after login
      // These shouldn't change and loading them
      // For each game seems excessive
      const contractStore = useContractStore();
      try {
        this.loading = true;
        await contractStore.loadContracts();
        this.loggedIn = true;
      } finally {
        this.loading = false;
      }
    },
    logout() {
      this.loggedIn = false;

      // Reset all stores to prevent any weird behavior on logout / login
      useContractStore().$reset();
      useGameStore().$reset();
      useTableStore().$reset();
      clearToken();
    },
  },
});
