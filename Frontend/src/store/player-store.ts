import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";
import { useToast } from "vue-toastification";

import {
  updatePlayerDisplayName,
  updatePlayerPassword,
  deleteRegisteredPlayer,
} from "@/services/player-service";
import { useAuthStore } from "@/store/auth-store";

const toast = useToast();

export const usePlayerStore = defineStore("player", {
  actions: {
    async updateDisplayName(displayName: string) {
      const authStore = useAuthStore();
      try {
        assertNonNullish(authStore.playerId, "PlayerId not defined");
        const { token } = await updatePlayerDisplayName(authStore.playerId, displayName);
        authStore.setToken(token);
      } catch (e) {
        toast.error("Es gab ein Problem mit der Aktualisierung des Spielers");
      }
    },
    async updatePassword(oldPassword: string, newPassword: string): Promise<boolean> {
      const authStore = useAuthStore();
      try {
        assertNonNullish(authStore.playerId, "PlayerId not defined");
        const { token } = await updatePlayerPassword(authStore.playerId, oldPassword, newPassword);
        authStore.setToken(token);
        return true;
      } catch (e) {
        return false;
      }
    },
    async deleteCurrentPlayerAccount() {
      const authStore = useAuthStore();
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
