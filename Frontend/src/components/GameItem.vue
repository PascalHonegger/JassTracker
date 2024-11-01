<script setup lang="ts">
import type { Game } from "@/types/types";
import { toDateTimeString } from "@/util/dates";
import ScoreboardTable from "@/components/ScoreboardTable.vue";
import { useGameStore } from "@/store/game-store";

const gameStore = useGameStore();

const props = defineProps<{ game: Game }>();

function finish() {
  gameStore.finishGame(props.game.id);
}
</script>

<template>
  <p class="mt-2">Gestartet: {{ toDateTimeString(game.startTime) }}</p>
  <p v-if="game.endTime">Beendet: {{ toDateTimeString(game.endTime) }}</p>
  <ScoreboardTable :game="game" />
  <button v-if="game.endTime === undefined" type="button" class="btn btn-blue" @click="finish">
    Spiel abschliessen
  </button>
</template>
