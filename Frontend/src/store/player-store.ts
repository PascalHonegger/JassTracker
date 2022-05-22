import { defineStore } from "pinia";

import { updatePlayerDisplayName } from "@/services/player-service";
import { useAuthStore } from "@/store/auth-store";

const authStore = useAuthStore();

export const usePlayerStore = defineStore("player", {
  actions: {
    async updateDisplayName(displayName: string) {
      try {
        if (authStore.playerId === null) {
          alert("Player should have an ID");
          return;
        }
        await updatePlayerDisplayName(authStore.playerId, displayName);
      } catch (e) {
        alert("There was an error with updating player");
      }
    },
  },
});
