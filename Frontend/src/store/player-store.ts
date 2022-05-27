import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";
import { useToast } from "vue-toastification";

import {
  updatePlayerDisplayName,
  deleteRegisteredPlayer,
} from "@/services/player-service";
import { useAuthStore } from "@/store/auth-store";

const authStore = useAuthStore();
const toast = useToast();

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
        toast.error("Es gab ein Problem mit der Aktuallisierung des Spielers");
      }
    },
    async deleteCurrentPlayerAccount() {
      try {
        assertNonNullish(authStore.playerId, "PlayerId not defined");
        await deleteRegisteredPlayer(authStore.playerId);
        await authStore.logout();
      } catch (e) {
        toast.error("Es gab ein Problem mit der Löschung des Spielers");
      }
    },
  },
});
