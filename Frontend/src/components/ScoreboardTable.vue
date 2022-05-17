<script setup lang="ts">
import RoundRow from "./RoundRow.vue";
import { Game, GameParticipation, Team } from "@/types/types";
import { computed } from "vue";
import { useContractStore } from "@/store/contract-store";

const contractStore = useContractStore();

const props = defineProps<{ game: Game }>();

function isActive(participation: GameParticipation): boolean {
  return (
    props.game.endTime === undefined &&
    props.game.currentPlayer.playerId === participation.playerId
  );
}

const total = computed(() => {
  const { team1, team2, rounds } = props.game;
  const tempTotal: Record<string, number> = {
    [team1.player1.playerId]: 0,
    [team1.player2.playerId]: 0,
    [team2.player1.playerId]: 0,
    [team2.player2.playerId]: 0,
  };
  rounds.forEach((r) => {
    tempTotal[r.playerId] += contractStore.getCalculatedScore(r);
  });
  return tempTotal;
});

function getTeamTotal(team: Team): number {
  return (
    total.value[team.player1.playerId] + total.value[team.player2.playerId]
  );
}

const teams = computed(() => [props.game.team1, props.game.team2]);
</script>
<style scoped lang="scss">
.active {
  @apply bg-green-300;
}
</style>
<template>
  <div
    class="max-w-3xl my-4 w-full text-center border rounded border-black border-solid overflow-auto flex"
  >
    <table class="table-fixed min-w-fit grow">
      <thead>
        <tr class="h-10">
          <th></th>
          <th
            v-for="teamNumber in [1, 2]"
            :key="teamNumber"
            colspan="2"
            scope="colgroup"
            class="border-x-2 border-slate-300 text-xl"
          >
            Team {{ teamNumber }}
          </th>
        </tr>
        <tr class="border-b-2 border-slate-300">
          <th></th>
          <template v-for="(team, index) in teams" :key="index">
            <th
              scope="col"
              :class="{ active: isActive(game.team1.player1) }"
              class="border-l-2 border-slate-300"
            >
              {{ team.player1.displayName }}
            </th>
            <th
              scope="col"
              :class="{ active: isActive(game.team1.player2) }"
              class="border-l-2 border-slate-300"
            >
              {{ team.player2.displayName }}
            </th>
          </template>
        </tr>
      </thead>
      <tbody>
        <RoundRow
          class="odd:bg-white even:bg-slate-100 h-10"
          v-for="r in game.rows"
          :row="r"
          :key="r.contract.id"
          :readonly="game.endTime !== undefined"
        />
      </tbody>
      <tfoot>
        <tr class="border-t-2 border-slate-300 h-10 text-xl font-bold">
          <th rowspan="2" scope="row" class="border-r-2 border-slate-300">
            Total
          </th>
          <template v-for="(t, playerId) in total" :key="playerId">
            <td>{{ t }}</td>
          </template>
        </tr>
        <tr class="border-t-2 border-slate-300">
          <td
            class="h-10 text-xl font-bold"
            colspan="2"
            v-for="(team, index) in teams"
            :key="index"
          >
            {{ getTeamTotal(team) }}
          </td>
        </tr>
      </tfoot>
    </table>
  </div>
</template>
