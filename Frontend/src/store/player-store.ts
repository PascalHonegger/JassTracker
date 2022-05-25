import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";

import {
  updatePlayerDisplayName,
  deleteRegisteredPlayer,
} from "@/services/player-service";
import { useAuthStore } from "@/store/auth-store";

const authStore = useAuthStore();

export const usePlayerStore = defineStore("player", {
  actions: {
    async updateDisplayName(displayName: string) {
      try {
        assertNonNullish(authStore.playerId, "PlayerId not defined");
        const { token } = await updatePlayerDisplayName(
          authStore.playerId,
          displayName
        );
        authStore.setToken(token);
      } catch (e) {
        alert("There was an error with updating player");
      }
    },
    async deleteCurrentPlayerAccount() {
      try {
        assertNonNullish(authStore.playerId, "PlayerId not defined");
        await deleteRegisteredPlayer(authStore.playerId);
        await authStore.logout();
      } catch (e) {
        alert("There was an error with deleting the current player");
      }
    },
  },
});
