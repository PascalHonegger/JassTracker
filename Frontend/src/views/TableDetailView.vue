<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";
import CreateGame, { PartialCreateGame } from "@/components/CreateGame.vue";
import ModalDialog from "@/components/ModalDialog.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { WebCreateGame } from "@/services/web-model";
import type { Game } from "@/types/types";
import GameItem from "@/components/GameItem.vue";
import GameList from "@/components/GameList.vue";

const router = useRouter();
const route = useRoute();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { currentTable, currentTableId } = storeToRefs(tableStore);
const { currentGame } = storeToRefs(gameStore);

const creatingGame = ref(false);
const isModalVisible = ref(false);

const newGame = ref<PartialCreateGame>({
  team1Player1: { playerId: null, displayName: "" },
  team1Player2: { playerId: null, displayName: "" },
  team2Player1: { playerId: null, displayName: "" },
  team2Player2: { playerId: null, displayName: "" },
});

const otherGames = computed<Game[]>(() =>
  Object.values(currentTable.value?.loadedGames ?? {}).filter(
    (g) => g !== gameStore.currentGame
  )
);

const openGames = computed<Game[]>(() =>
  otherGames.value.filter((e) => e.endTime === undefined)
);

const completedGames = computed<Game[]>(() =>
  otherGames.value.filter((e) => e.endTime !== undefined)
);

watch(
  () => route.params.tableId,
  async (newId) => {
    await setCurrentTableId(newId);
  }
);

onMounted(async () => {
  await setCurrentTableId(route.params.tableId);
});

async function setCurrentTableId(newId: string | string[] | undefined) {
  if (typeof newId !== "string") return;
  tableStore.setCurrentTable(newId);
  await tableStore.loadTable(newId);

  await gameStore.loadGamesForTable(tableStore.currentTableId);
  // Open the latest game if no game id is specified
  gameStore.setCurrentGame(newId, currentTable.value?.latestGameId ?? "");
}

async function createNewGame() {
  const tableId = currentTable.value?.id;
  if (tableId == null) {
    alert("Can't create a game without a table!");
    return;
  }
  const createGame: WebCreateGame = {
    ...newGame.value,
    tableId,
  };
  const createdId = await gameStore.createGame(createGame);
  gameStore.setCurrentGame(tableId, createdId);
  isModalVisible.value = false;
}

function backToOverview() {
  router.push("/overview");
}
</script>
<template>
  <div class="container mx-auto" v-if="currentTable">
    <button @click="backToOverview" class="btn btn-blue mt-2">Zurück</button>
    <button @click="isModalVisible = true" class="btn btn-blue ml-2 mt-2">
      Neues Spiel erstellen
    </button>
    <GameItem v-if="currentGame" :game="currentGame" />
    <p v-else>Momentan läuft kein Spiel</p>
  </div>

  <WaitSpinner v-else />

  <div v-if="openGames.length > 0" class="container mx-auto my-2">
    <h2 class="font-bold text-lg">Offene Spiele</h2>
    <GameList :table-id="currentTableId" :games="openGames" />
  </div>

  <div v-if="completedGames.length > 0" class="container mx-auto my-2">
    <h2 class="font-bold text-lg">Abgeschlossene Spiele</h2>
    <GameList :table-id="currentTableId" :games="completedGames" />
  </div>

  <ModalDialog v-show="isModalVisible" @close="isModalVisible = false">
    <template v-slot:header>
      <p class="font-bold">Neues Spiel erstellen</p>
    </template>
    <template v-slot:body>
      <form @submit.prevent="createNewGame" class="flex justify-around">
        <CreateGame
          :disabled="creatingGame"
          v-model:new-game="newGame"
        ></CreateGame>
      </form>
    </template>
    <template v-slot:footer>
      <button
        type="button"
        class="btn btn-blue"
        :disabled="creatingGame"
        @click="createNewGame"
      >
        Neues Spiel starten

        <WaitSpinner v-if="creatingGame"></WaitSpinner>
      </button>
    </template>
  </ModalDialog>
</template>
