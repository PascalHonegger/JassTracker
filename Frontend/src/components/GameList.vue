<script setup lang="ts">
import type { Game } from "@/types/types";
import { toDateTimeString } from "@/util/dates";
import GamePreview from "@/components/GamePreview.vue";

export interface NamedGame {
  name: string;
  game: Game;
}

defineProps<{ tableId: string; games: NamedGame[] }>();

function getFormattedTime(game: Game) {
  return toDateTimeString(game.endTime ?? game.startTime);
}
</script>

<template>
  <ul class="flex flex-row gap-2 flex-wrap">
    <li v-for="{ name, game } in games" :key="game.id">
      <RouterLink
        class="border p-2 rounded flex flex-col text-center"
        :to="{
          name: 'game',
          params: { tableId, gameId: game.id },
        }"
      >
        <div class="font-bold border-slate-300 border-b-2 flex flex-col">
          <div>{{ name }}</div>
          <div>{{ getFormattedTime(game) }}</div>
        </div>
        <GamePreview :game="game" />
      </RouterLink>
    </li>
  </ul>
</template>
