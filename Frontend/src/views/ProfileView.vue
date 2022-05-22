<script lang="ts" setup>
import { useAuthStore } from "@/store/auth-store";
import { deletePlayer } from "@/services/player-service";
import { storeToRefs } from "pinia";
import router from "@/router";

const authStore = useAuthStore();
const { playerId, isGuest } = storeToRefs(authStore);

async function deleteAccount() {
  if (playerId.value == null) {
    throw new Error("Unexpected: no active login on ProfileView");
  }
  await deletePlayer(playerId.value);
  authStore.logout();
  await router.push("/");
}
</script>

<template>
  <div class="container mx-auto text-center flex flex-row flex-wrap">
    <div v-if="!isGuest" class="p-4">
      <button @click="deleteAccount" class="btn btn-blue">Konto löschen</button>
    </div>
  </div>
</template>
