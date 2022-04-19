<script setup lang="ts">
import Table from "../components/TableComponent.vue";
import Modal from "../components/Modal.vue";
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { storeToRefs } from "pinia";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { useGameStore } from "@/store/game-store";
import { WebCreateGame } from "@/services/web-model";

const router = useRouter();
const tableStore = useTableStore();
const gameStore = useGameStore();

const { tablesAsArray } = storeToRefs(tableStore);

const loadingTables = ref(false);
const creatingGame = ref(false);
const isModalVisible = ref(false);
const newTableName = ref("");
const newGame = reactive({
  team1Player1: "",
  team1Player2: "",
  team2Player1: "",
  team2Player2: "",
});
onMounted(async () => {
  loadingTables.value = true;
  await tableStore.loadTables();
  loadingTables.value = false;
});

function showModal() {
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}

async function createNewTable() {
  const newTableId = await tableStore.createTable(newTableName.value);
  const createGame: WebCreateGame = {
    tableId: newTableId,
    team1Player1: { displayName: newGame.team1Player1, playerId: null },
    team1Player2: { displayName: newGame.team1Player2, playerId: null },
    team2Player1: { displayName: newGame.team2Player1, playerId: null },
    team2Player2: { displayName: newGame.team2Player2, playerId: null },
  };
  await gameStore.createGame(createGame);
  closeModal();
  await router.push({ name: "table", params: { id: newTableId } });
}
</script>
<style lang="scss">
.create-table {
  cursor: pointer;
  text-align: center;
}
.add-icon {
  font-size: 50pt;
  align-self: center;
}
</style>
<template>
  <div class="table-container container mx-auto">
    <WaitSpinner v-if="loadingTables"></WaitSpinner>
    <div v-else class="flex items-stretch flex-wrap">
      <Table v-for="t in tablesAsArray" :key="t.id" :table="t"></Table>
      <button
        @click="showModal"
        class="table create-table max-w-sm w-full lg:max-w-full lg:flex m-4"
      >
        <span class="add-icon">+</span>
      </button>
    </div>

    <Modal
      class="create-table-modal"
      v-show="isModalVisible"
      @close="closeModal"
    >
      <template v-slot:header>
        <p class="font-bold">Neuen Tisch erstellen</p>
      </template>
      <template v-slot:body>
        <div class="table-name mb-2 text-center">
          <label for="tableName">Tisch Name</label>
          <input id="tableName" v-model="newTableName" />
        </div>
        <form @submit.prevent="createNewTable" class="table-players">
          <div class="mb-4">
            <label for="player1">Spieler 1</label>
            <input
              autocomplete="username"
              id="player1"
              v-model="newGame.team1Player1"
              :disabled="creatingGame"
            />

            <label for="player2">Spieler 2</label>
            <input
              autocomplete="username"
              id="player2"
              v-model="newGame.team1Player2"
              :disabled="creatingGame"
            />
          </div>
          <div class="mb-4">
            <label for="player3">Spieler 3</label>
            <input
              autocomplete="username"
              id="player3"
              v-model="newGame.team2Player1"
              :disabled="creatingGame"
            />

            <label for="player4">Spieler 4</label>
            <input
              autocomplete="username"
              v-model="newGame.team2Player2"
              :disabled="creatingGame"
              id="player4"
            />
          </div>
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
    </Modal>
  </div>
</template>
