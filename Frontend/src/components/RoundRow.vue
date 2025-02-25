<script setup lang="ts">
import type { Round, Row } from "@/types/types";
import type { WebCreateRound, WebRound } from "@/services/web-model";
import { useRoundStore } from "@/store/round-store";
import { useContractStore } from "@/store/contract-store";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";
import ContractIcon from "./ContractIcon.vue";
import ScoreInput from "@/components/ScoreInput.vue";
import { assertNonNullish } from "@/util/assert";
import { maxScore } from "@/util/constants";

const roundStore = useRoundStore();
const gameStore = useGameStore();
const { currentGame } = storeToRefs(gameStore);

const contractStore = useContractStore();

async function handleInput(score: number | undefined, round: Round) {
  assertNonNullish(currentGame.value, "undefined table or game, this should not happen");
  round.score = score ?? null;
  if (round.id) {
    if (score == null || isNaN(score)) {
      await roundStore.removeRound(round.id, round.playerId, round.contractId, round.number);
      return;
    }
    const updatedRound: WebRound = {
      id: round.id,
      number: round.number,
      score: score,
      gameId: currentGame.value.id,
      playerId: round.playerId,
      contractId: round.contractId,
    };
    await roundStore.updateRound(updatedRound);
  } else if (score != null) {
    const newRound: WebCreateRound = {
      number: currentGame.value.rounds.length + 1,
      score: score,
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
        <div class="relative w-fit mx-auto">
          <ScoreInput
            v-model="r.score"
            class="w-24 px-1"
            :class="getClass(r)"
            :disabled="r.type === 'locked' || readonly"
            :max="maxScore"
            @update:model-value="(score) => handleInput(score, r)"
          />
          <span
            v-if="r.score != null"
            class="absolute pointer-events-none border-l border-dotted border-black w-1/2 right-0"
            >{{ contractStore.getCalculatedScore(r) }}</span
          >
        </div>
      </td>
    </template>
  </tr>
</template>

<style lang="postcss" scoped>
.played,
.locked {
  background-color: lightgray;
}

.open {
  background-color: lightblue;
}
</style>
