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
    <scoreboard :scoreboard="scoreboard"></scoreboard>
  </div>
</template>

<script lang="ts">
import Scoreboard from "../components/ScoreboardComponent.vue";
// import { getScoreboard } from '../services/scoreboardService';

export default {
  name: "ScoreboardDetailView",
  data() {
    return {
      scoreboard: {},
      gameInProgress: false,
    };
  },
  created() {
    this.scoreboard = [
      {
        id: 1,
        name: "Demo Scoreboard",
      },
    ];
    /* getScoreboard().then((data) => {
      this.scoreboard = data.dataObject || [];
    }); */
  },
  components: {
    Scoreboard,
  },
  methods: {
    newGame() {
      console.log("New Game will be created");
    },
    backToOverview() {
      this.$router.push(`/overview`);
    },
  },
};
</script>
