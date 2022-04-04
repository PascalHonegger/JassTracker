<script setup lang="ts">
import { ref } from "vue";

const props = defineProps({
  scoreboard: Object,
});
const mutableScoreboard = ref({ ...props.scoreboard });

function updateScoreboard() {
  // update board name
  // update team member names
}
</script>
<template>
  <div
    class="scoreboard max-w-sm w-full lg:max-w-full lg:flex flex-col m-4 text-center"
  ></div>
</template>
