<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import { onMounted, onUnmounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import ScoreboardTable from "@/components/ScoreboardTable.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";

const router = useRouter();
const route = useRoute();

const gameStore = useGameStore();
const tableStore = useTableStore();

const { currentGame } = storeToRefs(gameStore);

watch(
  () => route.params.gameId,
  async (newId) => await setCurrentGameId(newId)
);

onMounted(async () => await setCurrentGameId(route.params.gameId));

onUnmounted(async () => await setCurrentGameId(""));

async function setCurrentGameId(newId: string | string[]) {
  if (Array.isArray(newId)) {
    return;
  }
  gameStore.setCurrentGame(tableStore.currentTableId, newId);
  if (newId) {
    await gameStore.loadGame(tableStore.currentTableId, newId);
  }
}

function backToTable() {
  router.push({
    name: "table",
    params: { tableId: tableStore.currentTableId },
  });
}
</script>

<template>
  <div class="container mx-auto mb-3">
    <button class="btn btn-blue mt-2" @click="backToTable">Zurück</button>
    <ScoreboardTable v-if="currentGame" :game="currentGame" />
    <WaitSpinner v-else />
  </div>
</template>