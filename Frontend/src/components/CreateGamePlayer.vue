<script setup lang="ts">
defineProps<{
  username: string | null;
  displayName: string;
}>();
const emit = defineEmits<{
  (event: "update:displayName", createNewGameForm: string): void;
}>();
</script>
<template>
  <div>
    <div v-if="username != null">
      {{ username }}
    </div>
    <div v-else class="italic">Neuer Spieler</div>
    <div class="flex flex-col md:flex-row items-center gap-4">
      <label>Alias: </label>
      <input
        type="text"
        autocomplete="nickname"
        class="box-input w-full"
        required
        :value="displayName"
        @input="emit('update:displayName', ($event.target as HTMLInputElement).value)"
      />
    </div>
  </div>
</template>
