<script setup lang="ts">
import { Game } from "@/types/types";
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
  <button
    type="button"
    @click="finish"
    v-if="game.endTime === undefined"
    class="btn btn-blue"
  >
    Spiel abschliessen
  </button>
</template>
