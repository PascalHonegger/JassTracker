<script setup lang="ts">
import TableItem from "@/components/TableItem.vue";
import ModalDialog from "@/components/ModalDialog.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { computed, onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import type { WebCreateGame, WebCreateGameParticipation } from "@/services/web-model";
import CreateGame from "@/components/CreateGame.vue";
import type { CreateNewGameForm } from "@/components/CreateGame.vue";
import { useAuthStore } from "@/store/auth-store";
import { useMetaStore } from "@/store/meta-store";
import { useToast } from "vue-toastification";
import {
  minTableNameLength,
  maxTableNameLength,
  minDisplayNameLength,
  maxDisplayNameLength,
} from "@/util/constants";
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
  if (
    newTableName.value.length < minTableNameLength ||
    newTableName.value.length > maxTableNameLength
  ) {
    toast.error(
      `Tischname muss zwischen ${minTableNameLength} und ${maxTableNameLength} Zeichen sein`,
    );
    creatingTable.value = false;
    return;
  }
  const validateTeamPlayersSuccess = await validatePlayers(newGame);
  if (!validateTeamPlayersSuccess) {
    toast.error(
      `Spieler Alias muss zwischen ${minDisplayNameLength} und ${maxDisplayNameLength} Zeichen sein`,
    );
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
  toast.success(`Tisch ${newTableName.value} wurde erfolgreich erstellt`);
  await router.push({ name: "table", params: { tableId: newTableId } });
}

async function validatePlayers(game: CreateNewGameForm): Promise<boolean> {
  let success = true;
  Object.values(game).forEach((item) => {
    if (
      !item.displayName ||
      item.displayName.length < minDisplayNameLength ||
      item.displayName.length > maxDisplayNameLength
    ) {
      success = false;
    }
  });
  return success;
}

function updatePlayer(
  player: keyof CreateNewGameForm,
  participation: WebCreateGameParticipation | null,
) {
  newGame[player] = participation ?? newPlayer;
}
</script>
<template>
  <div class="table-container container mx-auto">
    <div class="flex flex-col items-stretch md:flex-wrap md:flex-row gap-4 p-4">
      <TableItem v-for="t in tablesAsArray" :key="t.id" :table="t"></TableItem>
      <button
        class="jass-table cursor-pointer max-w-sm w-full lg:max-w-full lg:flex"
        @click="isModalVisible = true"
      >
        <span class="self-center text-6xl">+</span>
      </button>
    </div>

    <ModalDialog v-if="isModalVisible" class="overflow-auto" @close="isModalVisible = false">
      <template #header>
        <p class="font-bold">Tisch erstellen</p>
      </template>
      <template #body>
        <div class="flex flex-col">
          <div
            class="flex flex-row gap-2 table-name mb-4 pb-4 justify-center border-b-2 border-black border-dashed"
          >
            <label for="tableName" class="text-center block self-center">Tisch Name:</label>
            <input
              id="tableName"
              v-model="newTableName"
              class="box-input self-center w-60"
              placeholder="Beispiel: Samschtig-Jass Familie"
            />
          </div>
          <CreateGame
            :disabled="creatingTable"
            :existing-players="availablePlayers"
            :new-game-form="newGame"
            @update-player="updatePlayer"
          ></CreateGame>
        </div>
      </template>
      <template #footer>
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
