<script setup lang="ts">
import Scoreboard from "../components/ScoreboardComponent.vue";
import { onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";

const router = useRouter();
const route = useRoute();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { currentTable } = storeToRefs(tableStore);
const { currentGame } = storeToRefs(gameStore);

watch(
  () => route.params.id,
  async (newId) => {
    await setCurrentTableId(newId);
  }
);

onMounted(async () => {
  await setCurrentTableId(route.params.id);
});

async function setCurrentTableId(newId: string | string[] | undefined) {
  if (typeof newId !== "string") return;
  tableStore.setCurrentTable(newId);
  await tableStore.loadTable(newId);
}

function newGame() {
  console.log("New Game will be created", currentTable.value);
}

function backToOverview() {
  router.push("/overview");
}
</script>
<template>
  <div class="container mx-auto">
    <button @click="backToOverview" class="btn btn-blue mt-2 ml-2">
      Zurück
    </button>
    <button @click="newGame" class="btn btn-blue ml-2 mt-2">
      Neues Spiel erstellen
    </button>
    <Scoreboard v-if="currentGame" :game="currentGame"></Scoreboard>
  </div>
</template>
