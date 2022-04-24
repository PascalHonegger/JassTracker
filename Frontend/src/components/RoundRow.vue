<script setup lang="ts">
import type { Round, Row } from "@/types/types";

const props = defineProps<{ row: Row; readonly: boolean }>();

function getClass(round: Round): string {
  return props.readonly ? "locked" : round.type;
}
</script>
<style lang="scss">
.played,
.locked {
  background-color: lightgray;
}

.open {
  background-color: lightblue;
}
</style>
<template>
  <tr>
    <th scope="row" class="border-r-2 border-slate-300">
      {{ row.contract.name }}
    </th>
    <template v-for="r in row.rounds" :key="r">
      <td>
        <input
          type="text"
          inputmode="numeric"
          class="text-center w-24"
          :disabled="r.type === 'locked' || readonly"
          :value="r.score"
          :class="getClass(r)"
          min="0"
          max="157"
        />
      </td>
    </template>
  </tr>
</template>
