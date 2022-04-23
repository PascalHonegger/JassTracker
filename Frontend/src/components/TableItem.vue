<script setup lang="ts">
import { Table } from "@/types/types";
import GamePreview from "./GamePreview.vue";
import { computed } from "vue";

const props = defineProps<{ table: Table }>();

const latestGame = computed(
  () => props.table.loadedGames[props.table.latestGameId]
);
</script>
<template>
  <RouterLink
    :to="{ name: 'table', params: { tableId: table.id } }"
    class="table max-w-sm w-full lg:max-w-full lg:flex flex-col text-center"
  >
    <p class="font-bold">{{ table.name }}</p>
    <GamePreview v-if="latestGame != null" :game="latestGame"></GamePreview>
  </RouterLink>
</template>
