<script setup lang="ts">
import type { Round, Row } from "@/types/types";
import { WebCreateRound } from "@/services/web-model";
import { useRoundStore } from "@/store/round-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";

const roundStore = useRoundStore();
const gameStore = useGameStore();
const { currentGame } = storeToRefs(gameStore);

async function handleInput(event: Event, round: Round) {
  if (currentGame.value === undefined) {
    alert("undefined table or game, this should not happen");
    return;
  }
  const inputScore = parseInt((event.target as HTMLInputElement).value, 10);
  const actualScore = inputScore < 0 ? 157 - Math.abs(inputScore) : inputScore;
  if (round.id) {
    // update, TBD
  } else {
    const newRound: WebCreateRound = {
      number: currentGame.value.rounds.length + 1,
      score: actualScore,
      gameId: currentGame.value.id,
      playerId: round.playerId,
      contractId: round.contractId,
    };
    await roundStore.createRound(newRound);
  }
}

const props = defineProps<{ row: Row; readonly: boolean }>();

function getClass(round: Round): string {
  return props.readonly ? "locked" : round.type;
}
</script>
<style lang="scss">
.played,
.locked {
  background-color: lightgray;
}

.open {
  background-color: lightblue;
}
</style>
<template>
  <tr>
    <th scope="row" class="border-r-2 border-slate-300">
      {{ row.contract.name }}
    </th>
    <template v-for="r in row.rounds" :key="r">
      <td>
        <input
          type="text"
          inputmode="numeric"
          class="text-center w-24"
          @change="handleInput($event, r)"
          :disabled="r.type === 'locked' || readonly"
          :value="r.score"
          :class="getClass(r)"
          min="-157"
          max="157"
        />
      </td>
    </template>
  </tr>
</template>
