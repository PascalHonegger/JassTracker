<template>
  <router-link
    :to="{ name: 'table', params: { id: table.id } }"
    class="table max-w-sm w-full lg:max-w-full lg:flex flex-col m-4 text-center"
  >
    <p class="font-bold">{{ mutableTable.name }}</p>
    <ul class="member-list">
      <li v-for="member in mutableTable.members" :key="member">
        {{ member }}
      </li>
    </ul>
  </router-link>
</template>

<script lang="ts">
export default {
  name: "TableComponent",
  props: {
    table: Object,
  },
  data() {
    return {
      mutableTable: { ...this.table },
    };
  },
  methods: {
    updateTable() {
      // update table name
      // update team member names
    },
  },
};
</script>
