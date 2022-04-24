<script setup lang="ts">
import { Game } from "@/types/types";

defineProps<{ game: Game }>();
</script>
<template>
  <table>
    <tr>
      <th>Team 1</th>
      <th>Team 2</th>
    </tr>
    <tr>
      <td class="border-t-2 border-slate-300">
        {{ game.team1.player1.displayName }}
      </td>
      <td class="border-t-2 border-slate-300">
        {{ game.team2.player1.displayName }}
      </td>
    </tr>
    <tr>
      <td class="border-t-2 border-slate-300">
        {{ game.team1.player2.displayName }}
      </td>
      <td class="border-t-2 border-slate-300">
        {{ game.team2.player2.displayName }}
      </td>
    </tr>
  </table>
</template>
