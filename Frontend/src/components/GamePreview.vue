<script setup lang="ts">
import { Game } from "@/types/types";

defineProps<{ game: Game }>();
</script>
<style lang="scss"></style>
<template>
  <span>
    {{ game.team1.player1.displayName }}
    &amp;
    {{ game.team1.player2.displayName }}
  </span>
  <span>VS</span>
  <span>
    {{ game.team2.player1.displayName }}
    &amp;
    {{ game.team2.player2.displayName }}
  </span>
</template>
