<script setup lang="ts">
import { ref } from "vue";

const props = defineProps({
  table: Object,
});
const mutableTable = ref({ ...props.table });

function updateTable() {
  // update table name
  // update team member names
}
</script>
<style lang="scss">
.member-list {
  columns: 2;
}
</style>
<template>
  <router-link
    :to="{ name: 'table', params: { id: mutableTable.id } }"
    class="table max-w-sm w-full lg:max-w-full lg:flex flex-col m-4 text-center"
  >
    <p class="font-bold">{{ mutableTable.name }}</p>
    <ul class="member-list">
      <li v-for="p in mutableTable.players" :key="p.id">
        {{ p.displayName }}
      </li>
    </ul>
  </router-link>
</template>
