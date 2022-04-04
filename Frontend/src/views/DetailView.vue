<script setup lang="ts">
import Scoreboard from "../components/ScoreboardComponent.vue";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
// import { getScoreboard } from '../services/scoreboardService';

const router = useRouter();

const scoreboard = ref({});
const gameInProgress = ref(false);
onMounted(() => {
  scoreboard.value = [
    {
      id: 1,
      name: "Demo Scoreboard",
    },
  ];
  /* getScoreboard().then((data) => {
    this.scoreboard = data.dataObject || [];
  }); */
});
function newGame() {
  console.log("New Game will be created");
}
function backToOverview() {
  router.push("/overview");
}
</script>
<template>
  <div class="container mx-auto">
    <button @click="backToOverview" class="btn btn-blue mt-2 ml-2">Back</button>
    <button
      v-if="!gameInProgress"
      @click="newGame"
      class="btn btn-blue ml-2 mt-2"
    >
      New Game
    </button>
    <Scoreboard :scoreboard="scoreboard"></Scoreboard>
  </div>
</template>
