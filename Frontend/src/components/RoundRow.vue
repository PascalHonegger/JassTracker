<script setup lang="ts">
import { Row } from "@/types/types";

defineProps<{ row: Row }>();
</script>
<template>
  <tr>
    <td>{{ row.contract.name }}</td>
    <template v-for="r in row.rounds" :key="r">
      <td>
        <input
          type="number"
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
