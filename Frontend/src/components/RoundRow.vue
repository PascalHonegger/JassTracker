<script setup lang="ts">
import type { Round, Row } from "@/types/types";
import { WebCreateRound, WebRound } from "@/services/web-model";
import { useRoundStore } from "@/store/round-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";
import ContractIcon from "./ContractIcon.vue";

const roundStore = useRoundStore();
const gameStore = useGameStore();
const { currentGame } = storeToRefs(gameStore);

async function handleInput(event: Event, round: Round) {
  if (currentGame.value === undefined) {
    alert("undefined table or game, this should not happen");
    return;
  }
  const target = event.target as HTMLInputElement;
  const inputScore = parseInt(target.value, 10);
  if (Math.abs(inputScore) > 157) {
    target.classList.add("!bg-red-500");
    event.preventDefault();
    return;
  }
  if (target.classList.contains("!bg-red-500")) {
    target.classList.remove("!bg-red-500");
  }
  const actualScore = inputScore < 0 ? 157 - Math.abs(inputScore) : inputScore;
  if (round.id) {
    if (actualScore == null || isNaN(actualScore)) {
      await roundStore.removeRound(
        round.id,
        round.playerId,
        round.contractId,
        round.number
      );
      return;
    }
    const updatedRound: WebRound = {
      id: round.id,
      number: round.number,
      score: actualScore,
      gameId: currentGame.value.id,
      playerId: round.playerId,
      contractId: round.contractId,
    };
    await roundStore.updateRound(updatedRound);
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

function validateNumber(event: KeyboardEvent) {
  if (!/^[-\d]$/.test(event.key)) {
    event.preventDefault();
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
    <th scope="row" class="border-r-2 border-slate-300 h-full">
      <div class="h-full items-center flex flex-row gap-1 px-1">
        <div class="shrink-0">
          <ContractIcon :contract="row.contract" />
        </div>
        <div>{{ row.contract.name }}</div>
        <div class="ml-auto">×{{ row.contract.multiplier }}</div>
      </div>
    </th>
    <template v-for="r in row.rounds" :key="r">
      <td>
        <input
          type="text"
          inputmode="numeric"
          class="text-right w-24 px-1"
          @change="handleInput($event, r)"
          @keypress="validateNumber"
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
