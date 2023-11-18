<script setup lang="ts">
import type { Game, Team } from "@/types/types";
import { useContractStore } from "@/store/contract-store";
import { computed } from "vue";

const props = defineProps<{ game: Game }>();

const contractStore = useContractStore();

const teams = computed(() => [props.game.team1, props.game.team2]);

function getTeamScore(team: Team): number {
  const playerIds = [team.player1.playerId, team.player2.playerId];
  return props.game.rounds
    .filter(({ playerId }) => playerIds.includes(playerId))
    .map((r) => contractStore.getCalculatedScore(r))
    .reduce((a, b) => a + b, 0);
}
</script>
<template>
  <table>
    <tr>
      <th class="px-2" v-for="(team, index) in teams" :key="index">
        <div>Team {{ index + 1 }}</div>
        <div>{{ getTeamScore(team) }} Punkte</div>
      </th>
    </tr>
    <tr>
      <td class="border-t-2 border-slate-300" v-for="(team, index) in teams" :key="index">
        {{ team.player1.displayName }}
      </td>
    </tr>
    <tr>
      <td class="border-t-2 border-slate-300" v-for="(team, index) in teams" :key="index">
        {{ team.player2.displayName }}
      </td>
    </tr>
  </table>
</template>
