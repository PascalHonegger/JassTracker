<script setup lang="ts">
import { Game } from "@/types/types";

const props = defineProps<{ game: Game }>();
</script>
<style lang="scss"></style>
<template>
  <div>
    {{ props.game.team1.player1.displayName }}
    &amp;
    {{ props.game.team1.player2.displayName }}
  </div>
  <div>VS</div>
  <div>
    {{ props.game.team2.player1.displayName }}
    &amp;
    {{ props.game.team2.player2.displayName }}
  </div>
</template>
