<script setup lang="ts">
import { Row } from "@/types/types";

defineProps<{ row: Row }>();
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
          :disabled="r.type === 'locked'"
          :value="r.score"
          :class="r.type"
          min="0"
          max="157"
        />
      </td>
    </template>
  </tr>
</template>
