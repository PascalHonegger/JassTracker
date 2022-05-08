<script setup lang="ts">
import {
  WebCreateGame,
  WebCreateGameParticipation,
} from "@/services/web-model";

export type PartialCreateGame = Omit<WebCreateGame, "tableId">;

const props = defineProps<{
  newGame: PartialCreateGame;
  disabled: boolean;
}>();

const emit = defineEmits<{
  (event: "update:newGame", createGame: PartialCreateGame): void;
}>();

function setAnonymousPlayer(key: keyof PartialCreateGame, value: string) {
  const anonymousPlayer: WebCreateGameParticipation = {
    playerId: null,
    displayName: value,
  };
  emit("update:newGame", {
    ...props.newGame,
    [key]: anonymousPlayer,
  });
}
</script>
<style lang="scss" scoped>
input {
  border-bottom: solid black 1px;
  @apply w-full;
}

label {
  @apply block my-2 text-sm font-medium text-gray-900;
}
</style>
<template>
  <div class="mb-4 w-1/2">
    <label for="player1">Spieler 1</label>
    <input
      autocomplete="username"
      id="player1"
      :value="newGame.team1Player1.displayName"
      @input="setAnonymousPlayer('team1Player1', $event.target.value)"
      :disabled="disabled"
    />

    <label for="player2">Spieler 2</label>
    <input
      class="w-full"
      autocomplete="username"
      id="player2"
      :value="newGame.team1Player2.displayName"
      @input="setAnonymousPlayer('team1Player2', $event.target.value)"
      :disabled="disabled"
    />
  </div>
  <div class="mb-4 w-1/2">
    <label for="player3">Spieler 3</label>
    <input
      autocomplete="username"
      id="player3"
      :value="newGame.team2Player1.displayName"
      @input="setAnonymousPlayer('team2Player1', $event.target.value)"
      :disabled="disabled"
    />

    <label for="player4">Spieler 4</label>
    <input
      autocomplete="username"
      :value="newGame.team2Player2.displayName"
      @input="setAnonymousPlayer('team2Player2', $event.target.value)"
      :disabled="disabled"
      id="player4"
    />
  </div>
</template>
