<template>
  <div
    class="scoreboard max-w-sm w-full lg:max-w-full lg:flex flex-col m-4 text-center"
  ></div>
</template>

<script lang="ts">
export default {
  name: "ScoreboardComponent",
  props: {
    scoreboard: Object,
  },
  data() {
    return {
      mutableScoreboard: { ...this.scoreboard },
    };
  },
  methods: {
    updateScoreboard() {
      // update board name
      // update team member names
    },
  },
};
</script>
