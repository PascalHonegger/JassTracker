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
      <th v-for="(team, index) in teams" :key="index" class="px-2">
        <div>Team {{ index + 1 }}</div>
        <div>{{ getTeamScore(team) }} Punkte</div>
      </th>
    </tr>
    <tr>
      <td v-for="(team, index) in teams" :key="index" class="border-t-2 border-slate-300">
        {{ team.player1.displayName }}
      </td>
    </tr>
    <tr>
      <td v-for="(team, index) in teams" :key="index" class="border-t-2 border-slate-300">
        {{ team.player2.displayName }}
      </td>
    </tr>
  </table>
</template>
