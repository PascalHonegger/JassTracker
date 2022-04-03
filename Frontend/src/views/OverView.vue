<template>
  <div class="table-container container mx-auto">
    <div class="flex items-stretch flex-wrap">
      <Table
        @click="detail($event)"
        v-for="t in allTables"
        :table="t"
        :key="t.id"
      ></Table>
      <div
        @click="showModal"
        class="table create-table max-w-sm w-full lg:max-w-full lg:flex m-4"
      >
        <p class="add-icon">+</p>
      </div>
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

<script lang="ts">
import Table from "../components/TableComponent.vue";
import Modal from "../components/Modal.vue";
// import { getTables } from '../services/tableService';

export default {
  name: "TableOverview",
  data() {
    return {
      allTables: [],
      isModalVisible: false,
      newTable: {
        tableName: "",
        player1: "",
        player2: "",
        player3: "",
        player4: "",
      },
    };
  },
  created() {
    this.allTables = [
      {
        id: 1,
        name: "Demo Table",
        members: ["Player 1", "Player 2", "Player 3", "Player 4"],
      },
      {
        id: 2,
        name: "Demo Table",
        members: ["Player 1", "Player 2", "Player 3", "Player 4"],
      },
    ];
    /* getTables().then((data) => {
      this.allTables = data.dataObject || [];
    }); */
  },
  components: {
    Table,
    Modal,
  },
  methods: {
    detail($event: any) {
      this.$router.push(`/table/${$event.currentTarget.dataset.id}`);
    },
    showModal() {
      this.isModalVisible = true;
    },
    closeModal() {
      this.isModalVisible = false;
    },
    saveTable() {
      // TODO add validation
      // TBD create new table via table service all information in this.newTable

      const id = 1;
      // close popup
      this.closeModal();
      // route to new created table -> gotta await id
      this.$router.push(`/table/${id}`);
    },
  },
};
</script>
