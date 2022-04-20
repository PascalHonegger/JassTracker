<script setup lang="ts">
import { Table } from "@/types/types";
import GamePreviewComponent from "./GamePreviewComponent.vue";
import { computed } from "vue";

const props = defineProps<{ table: Table }>();

const latestGame = computed(
  () => props.table.loadedGames[props.table.latestGameId]
);
</script>
<template>
  <router-link
    :to="{ name: 'table', params: { id: props.table.id } }"
    class="table max-w-sm w-full lg:max-w-full lg:flex flex-col text-center"
  >
    <p class="font-bold">{{ props.table.name }}</p>
    <game-preview-component
      v-if="latestGame != null"
      :game="latestGame"
    ></game-preview-component>
  </router-link>
</template>
