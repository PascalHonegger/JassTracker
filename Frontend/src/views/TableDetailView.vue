<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";
import CreateGame, { CreateNewGameForm } from "@/components/CreateGame.vue";
import ModalDialog from "@/components/ModalDialog.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import {
  WebCreateGame,
  WebCreateGameParticipation,
  WebGameParticipation,
} from "@/services/web-model";
import type { Game, GameParticipation } from "@/types/types";
import GameItem from "@/components/GameItem.vue";
import GameList, { NamedGame } from "@/components/GameList.vue";
import { dateCompare } from "@/util/dates";

const router = useRouter();
const route = useRoute();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { currentTable, currentTableId } = storeToRefs(tableStore);
const { currentGame } = storeToRefs(gameStore);

const creatingGame = ref(false);
const isModalVisible = ref(false);

const newPlayer: WebCreateGameParticipation = {
  playerId: null,
  displayName: "",
};

const newGame = reactive<CreateNewGameForm>({
  team1Player1: newPlayer,
  team1Player2: newPlayer,
  team2Player1: newPlayer,
  team2Player2: newPlayer,
});

const currentGamePlayers = computed<GameParticipation[]>(() => {
  const game: Game | null = currentGame.value;
  if (game == null) {
    return [newPlayer];
  }
  return [
    game.team1.player1,
    game.team1.player2,
    game.team2.player1,
    game.team2.player2,
    newPlayer,
  ];
});

const namedGames = computed<NamedGame[]>(() => {
  return Object.values(currentTable.value?.loadedGames ?? {})
    .filter((g) => g !== gameStore.currentGame)
    .sort((g1, g2) => dateCompare(g1.startTime, g2.startTime))
    .map((game, index) => ({ name: `Spiel ${index + 1}`, game }))
    .reverse();
});

const openGames = computed(() =>
  namedGames.value.filter(({ game }) => game.endTime === undefined)
);

const completedGames = computed(() =>
  namedGames.value.filter(({ game }) => game.endTime !== undefined)
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
    ...newGame,
    tableId,
  };
  const createdId = await gameStore.createGame(createGame);
  gameStore.setCurrentGame(tableId, createdId);
  isModalVisible.value = false;
}

function updatePlayer(
  player: keyof CreateNewGameForm,
  participation: WebGameParticipation | null
) {
  newGame[player] = participation ?? newPlayer;
}

function openCreateGameDialog() {
  const game: Game | null = currentGame.value;
  newGame.team1Player1 = game?.team1?.player1 ?? newPlayer;
  newGame.team1Player2 = game?.team1?.player2 ?? newPlayer;
  newGame.team2Player1 = game?.team2?.player1 ?? newPlayer;
  newGame.team2Player2 = game?.team2?.player2 ?? newPlayer;
  isModalVisible.value = true;
}

function backToOverview() {
  router.push("/overview");
}
</script>
<template>
  <div class="container mx-auto p-4" v-if="currentTable">
    <button @click="backToOverview" class="btn btn-blue mt-2">Zurück</button>
    <button @click="openCreateGameDialog" class="btn btn-blue ml-2 mt-2">
      Neues Spiel erstellen
    </button>
    <GameItem v-if="currentGame" :game="currentGame" />
    <p v-else>Momentan läuft kein Spiel</p>

    <div v-if="openGames.length > 0" class="my-2">
      <h2 class="font-bold text-lg">Offene Spiele</h2>
      <GameList :table-id="currentTableId" :games="openGames" />
    </div>

    <div v-if="completedGames.length > 0" class="my-2">
      <h2 class="font-bold text-lg">Abgeschlossene Spiele</h2>
      <GameList :table-id="currentTableId" :games="completedGames" />
    </div>
  </div>

  <WaitSpinner v-else />

  <ModalDialog v-if="isModalVisible" @close="isModalVisible = false">
    <template v-slot:header>
      <p class="font-bold">Neues Spiel erstellen</p>
    </template>
    <template v-slot:body>
      <form
        @submit.prevent="createNewGame"
        class="flex flex-row justify-around gap-2"
      >
        <CreateGame
          :disabled="creatingGame"
          :existing-players="currentGamePlayers"
          :new-game-form="newGame"
          @updatePlayer="updatePlayer"
        ></CreateGame>
      </form>
    </template>
    <template v-slot:footer
      >echo 'source /usr/share/nvm/init-nvm.sh' >> ~/.bashrc
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
