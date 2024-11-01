<script setup lang="ts">
import type { WebCreateGame, WebCreateGameParticipation } from "@/services/web-model";
import { VueDraggableNext as Draggable } from "vue-draggable-next";
import { computed } from "vue";
import CreateGamePlayer from "@/components/CreateGamePlayer.vue";

export type CreateNewGameForm = Omit<WebCreateGame, "tableId">;

const props = defineProps<{
  newGameForm: CreateNewGameForm;
  existingPlayers: WebCreateGameParticipation[];
  disabled: boolean;
}>();

const emit = defineEmits<{
  (
    event: "updatePlayer",
    player: keyof CreateNewGameForm,
    participation: WebCreateGameParticipation | null,
  ): void;
}>();

const playerNameLookup = computed(() => {
  const lookup: Record<string, string> = {};
  for (const player of props.existingPlayers) {
    if (player.playerId != null) {
      lookup[player.playerId] = player.displayName;
    }
  }
  return lookup;
});

function foo(player: WebCreateGameParticipation) {
  return player.playerId != null && player.playerId in playerNameLookup.value
    ? playerNameLookup.value[player.playerId]
    : null;
}

type PlayerRef = WebCreateGameParticipation & { name: string | null };

const availablePlayers = computed<PlayerRef[]>(() => [
  ...props.existingPlayers.map<PlayerRef>((p) => ({
    ...p,
    name: p.playerId ? playerNameLookup.value[p.playerId] : null,
  })),
]);

const team1Player1 = computed({
  get() {
    return [props.newGameForm.team1Player1];
  },
  set(value: WebCreateGameParticipation[]) {
    const player: WebCreateGameParticipation | null = value[0]
      ? {
          playerId: value[0].playerId,
          displayName: value[0].displayName,
        }
      : null;
    emit("updatePlayer", "team1Player1", player);
  },
});
const team2Player1 = computed({
  get() {
    return [props.newGameForm.team2Player1];
  },
  set(value: WebCreateGameParticipation[]) {
    const player: WebCreateGameParticipation | null = value[0]
      ? {
          playerId: value[0].playerId,
          displayName: value[0].displayName,
        }
      : null;
    emit("updatePlayer", "team2Player1", player);
  },
});
const team1Player2 = computed({
  get() {
    return [props.newGameForm.team1Player2];
  },
  set(value: WebCreateGameParticipation[]) {
    const player: WebCreateGameParticipation | null = value[0]
      ? {
          playerId: value[0].playerId,
          displayName: value[0].displayName,
        }
      : null;
    emit("updatePlayer", "team1Player2", player);
  },
});
const team2Player2 = computed({
  get() {
    return [props.newGameForm.team2Player2];
  },
  set(value: WebCreateGameParticipation[]) {
    const player: WebCreateGameParticipation | null = value[0]
      ? {
          playerId: value[0].playerId,
          displayName: value[0].displayName,
        }
      : null;
    emit("updatePlayer", "team2Player2", player);
  },
});

const team1Player1DisplayName = computed({
  get() {
    return props.newGameForm.team1Player1.displayName;
  },
  set(displayName: string) {
    emit("updatePlayer", "team1Player1", {
      playerId: props.newGameForm.team1Player1.playerId,
      displayName,
    });
  },
});
const team2Player1DisplayName = computed({
  get() {
    return props.newGameForm.team2Player1.displayName;
  },
  set(displayName: string) {
    emit("updatePlayer", "team2Player1", {
      playerId: props.newGameForm.team2Player1.playerId,
      displayName,
    });
  },
});
const team1Player2DisplayName = computed({
  get() {
    return props.newGameForm.team1Player2.displayName;
  },
  set(displayName: string) {
    emit("updatePlayer", "team1Player2", {
      playerId: props.newGameForm.team1Player2.playerId,
      displayName,
    });
  },
});
const team2Player2DisplayName = computed({
  get() {
    return props.newGameForm.team2Player2.displayName;
  },
  set(displayName: string) {
    emit("updatePlayer", "team2Player2", {
      playerId: props.newGameForm.team2Player2.playerId,
      displayName,
    });
  },
});
</script>
<template>
  <div class="flex flex-col gap-4">
    <h2 class="text-center text-xl">
      <span class="bg-orange-200">Team 1</span> gegen
      <span class="bg-blue-200">Team 2</span>
    </h2>
    <div class="flex flex-col md:flex-row gap-4">
      <div class="flex justify-center">
        <Draggable
          class="no-team w-full flex flex-row flex-wrap justify-around md:flex-nowrap md:flex-col md:justify-center gap-2"
          :list="availablePlayers"
          :group="{ name: 'players', pull: 'clone', put: false }"
          :sort="false"
        >
          <div v-for="element in availablePlayers" :key="element.playerId!" class="player">
            <span v-if="element.name != null">
              {{ element.name }}
            </span>
            <span v-else class="italic">Neuer Spieler</span>
          </div>
        </Draggable>
      </div>
      <div class="flex justify-items-center justify-center grid grid-cols-2 sm:grid-cols-3 gap-4">
        <img
          loading="lazy"
          class="hidden sm:block w-32 md:w-48 h-32 md:h-48 col-start-2 row-start-2 justify-self-center m-4 pointer-events-none select-none"
          src="../assets/table.webp"
          alt="Jasstisch"
        />
        <!-- Team 1 (Top & Bottom) -->
        <Draggable
          v-model="team1Player1"
          class="player-slot team1 sm:col-start-2 sm:row-start-1 sm:self-end sm:justify-self-center"
          group="players"
          :sort="false"
        >
          <CreateGamePlayer
            v-model:display-name="team1Player1DisplayName"
            class="player"
            :username="foo(newGameForm.team1Player1)"
          />
        </Draggable>
        <Draggable
          v-model="team1Player2"
          class="player-slot team1 sm:col-start-2 sm:row-start-3 sm:self-start sm:justify-self-center"
          group="players"
          :sort="false"
        >
          <CreateGamePlayer
            v-model:display-name="team1Player2DisplayName"
            class="player"
            :username="foo(newGameForm.team1Player2)"
          />
        </Draggable>

        <!-- Team 2 (Left & Right) -->
        <Draggable
          v-model="team2Player1"
          class="player-slot team2 sm:col-start-1 sm:row-start-2 sm:self-center sm:justify-self-end"
          group="players"
          :sort="false"
        >
          <CreateGamePlayer
            v-model:display-name="team2Player1DisplayName"
            class="player"
            :username="foo(newGameForm.team2Player1)"
          />
        </Draggable>
        <Draggable
          v-model="team2Player2"
          class="player-slot team2 sm:col-start-3 sm:row-start-2 sm:self-center sm:justify-self-start"
          group="players"
          :sort="false"
        >
          <CreateGamePlayer
            v-model:display-name="team2Player2DisplayName"
            class="player"
            :username="foo(newGameForm.team2Player2)"
          />
        </Draggable>
      </div>
    </div>
  </div>
</template>
<style lang="postcss" scoped>
.player-slot {
  @apply w-36 h-36 md:w-full bg-gray-100 overflow-hidden md:h-24;
}
.player {
  @apply w-36 h-full md:w-full p-3 rounded-md text-center cursor-grab;
}
.no-team > .player {
  @apply bg-gray-300 h-16;
}
.team1 > .player {
  @apply bg-orange-200;
}
.team2 > .player {
  @apply bg-blue-200;
}
</style>
