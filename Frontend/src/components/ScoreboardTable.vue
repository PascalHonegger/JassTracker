<script setup lang="ts">
import RoundRow from "./RoundRow.vue";
import { Game, GameParticipation } from "@/types/types";

const props = defineProps<{ game: Game }>();

// Will be in store one day
const total = {
  player1: 0,
  player2: 0,
  player3: 0,
  player4: 0,
};

function isActive(participant: GameParticipation): boolean {
  return (
    props.game.endTime === undefined &&
    props.game.currentPlayer.playerId === participant.playerId
  );
}
</script>
<style lang="scss">
.active {
  @apply bg-green-300;
}
</style>
<template>
  <div class="max-w-2xl w-full my-2 text-center">
    <div class="border rounded border-black border-solid my-4">
      <div class="flex justify-between">
        <table class="table-fixed w-full">
          <thead>
            <tr class="h-10">
              <th></th>
              <th
                colspan="2"
                scope="colgroup"
                class="border-x-2 border-slate-300 text-xl"
              >
                Team 1
              </th>
              <th
                colspan="2"
                scope="colgroup"
                class="border-l-2 border-slate-300 text-xl"
              >
                Team 2
              </th>
            </tr>
            <tr class="border-b-2 border-slate-300">
              <th></th>
              <th
                scope="col"
                :class="{ active: isActive(game.team1.player1) }"
                class="border-l-2 border-slate-300"
              >
                {{ game.team1.player1.displayName }}
              </th>
              <th
                scope="col"
                :class="{ active: isActive(game.team1.player2) }"
                class="border-l-2 border-slate-300"
              >
                {{ game.team1.player2.displayName }}
              </th>
              <th
                scope="col"
                :class="{ active: isActive(game.team2.player1) }"
                class="border-l-2 border-slate-300"
              >
                {{ game.team2.player1.displayName }}
              </th>
              <th scope="col" :class="{ active: isActive(game.team2.player2) }">
                {{ game.team2.player2.displayName }}
              </th>
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
              <th scope="row">Total</th>
              <td>{{ total.player1 }}</td>
              <td>{{ total.player2 }}</td>
              <td>{{ total.player3 }}</td>
              <td>{{ total.player4 }}</td>
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
  </div>
</template>
