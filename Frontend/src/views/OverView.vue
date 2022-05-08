<script setup lang="ts">
import TableItem from "../components/TableItem.vue";
import ModalDialog from "../components/ModalDialog.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import { WebCreateGame } from "@/services/web-model";
import CreateGame, { PartialCreateGame } from "@/components/CreateGame.vue";

const router = useRouter();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { tablesAsArray } = storeToRefs(tableStore);

const loadingTables = ref(false);
const creatingGame = ref(false);
const isModalVisible = ref(false);
const newTableName = ref("");
const newGame = ref<PartialCreateGame>({
  team1Player1: { playerId: null, displayName: "" },
  team1Player2: { playerId: null, displayName: "" },
  team2Player1: { playerId: null, displayName: "" },
  team2Player2: { playerId: null, displayName: "" },
});
onMounted(async () => {
  loadingTables.value = true;
  await tableStore.loadTables();
  loadingTables.value = false;
});

async function createNewTable() {
  creatingGame.value = true;
  const newTableId = await tableStore.createTable(newTableName.value);
  const createGame: WebCreateGame = {
    ...newGame.value,
    tableId: newTableId,
  };
  await gameStore.createGame(createGame);
  await router.push({ name: "table", params: { tableId: newTableId } });
}
</script>
<template>
  <div class="table-container container mx-auto">
    <WaitSpinner v-if="loadingTables"></WaitSpinner>
    <div
      v-else
      class="flex flex-col items-stretch md:flex-wrap md:flex-row gap-4 p-4"
    >
      <TableItem v-for="t in tablesAsArray" :key="t.id" :table="t"></TableItem>
      <button
        @click="isModalVisible = true"
        class="jass-table cursor-pointer max-w-sm w-full lg:max-w-full lg:flex"
      >
        <span class="self-center text-6xl">+</span>
      </button>
    </div>

    <ModalDialog v-show="isModalVisible" @close="isModalVisible = false">
      <template v-slot:header>
        <p class="font-bold">Neuen Tisch erstellen</p>
      </template>
      <template v-slot:body>
        <div class="table-name mb-2 text-center">
          <label for="tableName">Tisch Name</label>
          <input id="tableName" v-model="newTableName" />
        </div>
        <form @submit.prevent="createNewTable" class="flex justify-around">
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
          @click="createNewTable"
        >
          Neues Spiel starten

          <WaitSpinner v-if="creatingGame"></WaitSpinner>
        </button>
      </template>
    </ModalDialog>
  </div>
</template>
