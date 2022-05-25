import { defineStore } from "pinia";

import {
  updatePlayerDisplayName,
  deleteRegisteredPlayer,
} from "@/services/player-service";
import { useAuthStore } from "@/store/auth-store";

const authStore = useAuthStore();

function checkInvalid(
  valueToBeAsserted: string | null
): asserts valueToBeAsserted is string {
  if (valueToBeAsserted === null || valueToBeAsserted === undefined) {
    throw new Error("Value is invalid");
  }
}
export const usePlayerStore = defineStore("player", {
  actions: {
    async updateDisplayName(displayName: string) {
      try {
        checkInvalid(authStore.playerId);
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
        checkInvalid(authStore.playerId);
        await deleteRegisteredPlayer(authStore.playerId);
        await authStore.logout();
      } catch (e) {
        alert("There was an error with deleting the current player");
      }
    },
  },
});
