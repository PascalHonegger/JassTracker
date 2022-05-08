<script setup lang="ts">
import type { Game } from "@/types/types";
import { toDateTimeString } from "@/util/dates";
import GamePreview from "@/components/GamePreview.vue";

defineProps<{ tableId: string; games: Game[] }>();

function getFormattedTime(game: Game) {
  return toDateTimeString(game.endTime ?? game.startTime);
}
</script>

<template>
  <ul class="flex flex-row gap-2 flex-wrap">
    <li v-for="game in games" :key="game.id">
      <RouterLink
        class="border p-2 rounded flex flex-col text-center"
        :to="{
          name: 'game',
          params: { tableId, gameId: game.id },
        }"
      >
        <GamePreview :game="game" />
        <span>{{ getFormattedTime(game) }}</span>
      </RouterLink>
    </li>
  </ul>
</template>
