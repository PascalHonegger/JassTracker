<template>
  <div class="container mx-auto">
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
};
</script>
