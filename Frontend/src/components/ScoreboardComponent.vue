<script setup lang="ts">
import Round from "./RoundComponent.vue";
import { Game } from "@/types/types";

const props = defineProps<{ game: Game }>();

// Will be in store one day
const total = {
  player1: 0,
  player2: 0,
  player3: 0,
  player4: 0,
};
</script>
<style lang="scss">
.game {
  .played,
  .locked {
    background-color: lightgray;
  }

  .open {
    background-color: lightblue;
  }
}
</style>
<template>
  <div
    class="scoreboard max-w-sm w-full lg:max-w-full lg:flex flex-col m-2 text-center"
  >
    <div
      class="game border rounded border-black border-solid maw-w-sm max-h-sm my-4"
    >
      <div class="flex justify-between">
        <table class="table-fixed w-full">
          <thead>
            <tr>
              <th></th>
              <th scope="col">{{ props.game.team1.player1.displayName }}</th>
              <th scope="col">{{ props.game.team1.player2.displayName }}</th>
              <th scope="col">{{ props.game.team2.player1.displayName }}</th>
              <th scope="col">{{ props.game.team2.player2.displayName }}</th>
            </tr>
          </thead>
          <tbody>
            <Round
              v-for="r in props.game.rows"
              :row="r"
              :key="r.contract.id"
            ></Round>
          </tbody>
          <tfoot>
            <tr>
              <td>Total</td>
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
