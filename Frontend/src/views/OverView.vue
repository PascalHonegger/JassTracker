<script setup lang="ts">
import TableItem from "../components/TableItem.vue";
import ModalDialog from "../components/ModalDialog.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { computed, onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import {
  WebCreateGame,
  WebCreateGameParticipation,
  WebGameParticipation,
} from "@/services/web-model";
import CreateGame, { CreateNewGameForm } from "@/components/CreateGame.vue";
import { useAuthStore } from "@/store/auth-store";
import { useMetaStore } from "@/store/meta-store";
import { useToast } from "vue-toastification";
const toast = useToast();

const router = useRouter();
const tableStore = useTableStore();
const gameStore = useGameStore();
const authStore = useAuthStore();
const metaStore = useMetaStore();

const { tablesAsArray } = storeToRefs(tableStore);

const newPlayer: WebCreateGameParticipation = {
  playerId: null,
  displayName: "",
};

const creatingTable = ref(false);
const isModalVisible = ref(false);
const newTableName = ref("");
const newGame = reactive<CreateNewGameForm>({
  team1Player1: newPlayer,
  team1Player2: newPlayer,
  team2Player1: newPlayer,
  team2Player2: newPlayer,
});
const availablePlayers = computed<WebCreateGameParticipation[]>(() => [
  { playerId: authStore.playerId, displayName: authStore.displayName ?? "" }, // Logged in player
  newPlayer,
]);

onMounted(async () => {
  metaStore.startLoading();
  try {
    await tableStore.loadTables();
  } finally {
    metaStore.stopLoading();
  }
});

async function createNewTable() {
  creatingTable.value = true;
  if (newTableName.value.length < 2 || newTableName.value.length > 30) {
    toast.error("Tisch Name muss zwischen 2 und 30 Zeichen sein");
    creatingTable.value = false;
    return;
  }
  const validateTeamPlayersSuccess = await validatePlayers(newGame);
  if (!validateTeamPlayersSuccess) {
    toast.error("Spieler benötigt einen Anzeige Namen");
    creatingTable.value = false;
    return;
  }
  const newTableId = await tableStore.createTable(newTableName.value);
  const createGame: WebCreateGame = {
    team1Player1: newGame.team1Player1,
    team1Player2: newGame.team1Player2,
    team2Player1: newGame.team2Player1,
    team2Player2: newGame.team2Player2,
    tableId: newTableId,
  };
  await gameStore.createGame(createGame);
  await router.push({ name: "table", params: { tableId: newTableId } });
}

async function validatePlayers(game: CreateNewGameForm): Promise<boolean> {
  let success = true;
  Object.values(game).forEach((item) => {
    if (!item.displayName) {
      success = false;
    }
  });
  return success;
}

function updatePlayer(
  player: keyof CreateNewGameForm,
  participation: WebGameParticipation | null
) {
  newGame[player] = participation ?? newPlayer;
}
</script>
<template>
  <div class="table-container container mx-auto">
    <div class="flex flex-col items-stretch md:flex-wrap md:flex-row gap-4 p-4">
      <TableItem v-for="t in tablesAsArray" :key="t.id" :table="t"></TableItem>
      <button
        @click="isModalVisible = true"
        class="jass-table cursor-pointer max-w-sm w-full lg:max-w-full lg:flex"
      >
        <span class="self-center text-6xl">+</span>
      </button>
    </div>

    <ModalDialog v-if="isModalVisible" @close="isModalVisible = false">
      <template v-slot:header>
        <p class="font-bold">Tisch erstellen</p>
      </template>
      <template v-slot:body>
        <div class="flex flex-col">
          <div
            class="flex flex-row gap-2 table-name mb-4 pb-4 justify-center border-b-2 border-black border-dashed"
          >
            <label for="tableName" class="text-center block self-center"
              >Tisch Name:</label
            >
            <input
              id="tableName"
              class="box-input self-center w-60"
              placeholder="Beispiel: Samschtig-Jass Familie"
              v-model="newTableName"
            />
          </div>
          <CreateGame
            :disabled="creatingTable"
            :existing-players="availablePlayers"
            :new-game-form="newGame"
            @updatePlayer="updatePlayer"
          ></CreateGame>
        </div>
      </template>
      <template v-slot:footer>
        <button
          type="button"
          class="btn btn-blue"
          :disabled="creatingTable"
          @click="createNewTable"
        >
          Neues Spiel starten

          <WaitSpinner v-if="creatingTable" size="medium"></WaitSpinner>
        </button>
      </template>
    </ModalDialog>
  </div>
</template>
