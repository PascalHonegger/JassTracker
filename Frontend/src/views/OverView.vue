<script setup lang="ts">
import Table from "../components/TableComponent.vue";
import Modal from "../components/Modal.vue";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import { storeToRefs } from "pinia";

const router = useRouter();
const tableStore = useTableStore();

const { tablesAsArray, loading } = storeToRefs(tableStore);

const isModalVisible = ref(false);
const newTable = {
  tableName: "",
  player1: "",
  player2: "",
  player3: "",
  player4: "",
};
onMounted(async () => {
  await tableStore.loadTables();
});

function showModal() {
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}

function saveTable() {
  // TODO add validation
  // TBD create new table via table service all information in this.newTable

  const id = 1;
  // close popup
  closeModal();
  // route to new created table -> gotta await id
  router.push({ name: "table", params: { id } });
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
    <div v-if="loading">LOADING...</div>
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
        <p class="font-bold">Neuer Tisch erstellen</p>
      </template>
      <template v-slot:body>
        <div class="table-name mb-2 text-center">
          <label for="tableName">Tisch Name</label>
          <input id="tableName" v-model="newTable.tableName" />
        </div>
        <div class="table-players">
          <div class="mb-4">
            <label for="player1">Spieler 1</label>
            <input
              autocomplete="username"
              id="player1"
              v-model="newTable.player1"
            />

            <label for="player2">Spieler 2</label>
            <input
              autocomplete="username"
              id="player2"
              v-model="newTable.player2"
            />
          </div>
          <div class="mb-4">
            <label for="player3">Spieler 3</label>
            <input
              autocomplete="username"
              id="player3"
              v-model="newTable.player3"
            />

            <label for="player4">Spieler 4</label>
            <input
              autocomplete="username"
              v-model="newTable.player4"
              id="player4"
            />
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <button type="button" class="btn btn-blue" @click="saveTable">
          Start
        </button>
      </template>
    </Modal>
  </div>
</template>
