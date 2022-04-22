<script setup lang="ts">
import Scoreboard from "@/components/ScoreboardComponent.vue";
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";
import CreateGameComponent, {
  PartialCreateGame,
} from "@/components/CreateGameComponent.vue";
import Modal from "@/components/Modal.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { WebCreateGame } from "@/services/web-model";
import type { Game } from "@/types/types";
import GamePreviewComponent from "@/components/GamePreviewComponent.vue";
import { toDateTimeString } from "@/util/dates";

const router = useRouter();
const route = useRoute();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { currentTable } = storeToRefs(tableStore);
const { currentGame } = storeToRefs(gameStore);

const creatingGame = ref(false);
const isModalVisible = ref(false);

const newGame = ref<PartialCreateGame>({
  team1Player1: { playerId: null, displayName: "" },
  team1Player2: { playerId: null, displayName: "" },
  team2Player1: { playerId: null, displayName: "" },
  team2Player2: { playerId: null, displayName: "" },
});

const pastGames = computed<Game[]>(() =>
  Object.values(currentTable.value?.loadedGames ?? {}).filter(
    (g) => g !== gameStore.currentGame
  )
);

const selectedGame = ref<Game>();

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

  // Open the latest game if no game id is specified
  gameStore.setCurrentGame(newId, currentTable.value?.latestGameId ?? "");
  await gameStore.loadGamesForTable(tableStore.currentTableId);
}

async function createNewGame() {
  const tableId = currentTable.value?.id;
  if (tableId == null) {
    console.error("Can't create a game without a table!");
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
    <button @click="backToOverview" class="btn btn-blue mt-2 ml-2">
      Zurück
    </button>
    <button @click="isModalVisible = true" class="btn btn-blue ml-2 mt-2">
      Neues Spiel erstellen
    </button>
    <Scoreboard v-if="currentGame" :game="currentGame"></Scoreboard>
    <p v-else>Momentan läuft kein Spiel</p>
  </div>

  <WaitSpinner v-else />

  <div v-if="pastGames.length > 0" class="container mx-auto">
    <h2 class="font-bold text-lg">Vergangene Spiele</h2>
    <ul class="flex flex-row gap-2 flex-wrap">
      <li v-for="game in pastGames" :key="game.id">
        <RouterLink
          class="border p-2 rounded flex flex-col text-center"
          :class="
            game === selectedGame
              ? ['border-blue-700', 'border-2', 'font-bold']
              : ['border-black', 'border-1']
          "
          :to="{
            name: 'game',
            params: { tableId: currentTable.id, gameId: game.id },
          }"
        >
          <GamePreviewComponent :game="game" />
          <span>{{ toDateTimeString(game.endTime) }}</span>
        </RouterLink>
      </li>
    </ul>
    <Scoreboard v-if="selectedGame" :game="selectedGame" />
  </div>

  <Modal v-show="isModalVisible" @close="isModalVisible = false">
    <template v-slot:header>
      <p class="font-bold">Neues Spiel erstellen</p>
    </template>
    <template v-slot:body>
      <form @submit.prevent="createNewGame" class="flex justify-around">
        <CreateGameComponent
          :disabled="creatingGame"
          v-model:new-game="newGame"
        ></CreateGameComponent>
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
  </Modal>
</template>
