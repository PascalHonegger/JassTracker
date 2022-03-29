<template>
  <div class="table-container container mx-auto">
    <div class="flex items-stretch flex-wrap">
      <Table
        @click="detail($event)"
        v-for="t in allTables"
        :table="t"
        :key="t.id"
      ></Table>
      <div class="table max-w-sm w-full lg:max-w-full lg:flex m-4">
        <p class="add-icon">+</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Table from "../components/TableComponent.vue";
// import { getTables } from '../services/tableService';

export default {
  name: "TableOverview",
  data() {
    return {
      allTables: [],
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
  },
  methods: {
    detail($event: any) {
      this.$router.push(`/table/${$event.currentTarget.dataset.id}`);
    },
  },
};
</script>
