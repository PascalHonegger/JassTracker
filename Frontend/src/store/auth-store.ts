import { defineStore } from "pinia";
import { useContractStore } from "@/store/contract-store";
import { useGameStore } from "@/store/game-store";
import { useTableStore } from "@/store/table-store";
import {createLoginRequest} from "@/services/auth-service";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    playerId: "27fa77f3-eb56-46a0-8ada-b0a6f2e26cc0",
    name: "<Gast>",
    loggedIn: false,
  }),
  actions: {
    async createLoginRequest(username: String, password: String): Promise<string>{
      const createdLoginRequest = await createLoginRequest(username, password)
      return createdLoginRequest.id
    },
    async setLoggedIn() {
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
    },
  },
});
