<script setup lang="ts">
import { Game } from "@/types/types";

const props = defineProps<{ game: Game }>();
</script>
<style lang="scss"></style>
<template>
  <span>
    {{ props.game.team1.player1.displayName }}
    &amp;
    {{ props.game.team1.player2.displayName }}
  </span>
  <span>VS</span>
  <span>
    {{ props.game.team2.player1.displayName }}
    &amp;
    {{ props.game.team2.player2.displayName }}
  </span>
</template>
