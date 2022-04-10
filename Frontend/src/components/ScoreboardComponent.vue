<script setup lang="ts">
import { ref } from "vue";
import Round from "./RoundComponent.vue";
import { Player, Row } from "@/types/types";
import { useRoute, useRouter } from "vue-router";
import { useGameStore } from "@/store/game-store";
import { storeToRefs } from "pinia";

const props = defineProps({
  game: Object,
});
const router = useRouter();
const route = useRoute();
const gameStore = useGameStore();

const { currentGame } = storeToRefs(gameStore);

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
              <td></td>
              <template v-for="n in currentGame.players" :key="n.id">
                <td>{{ n.displayName }}</td>
              </template>
            </tr>
          </thead>
          <tbody>
            <Round
              v-for="r in currentGame.rows"
              :round="r"
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
