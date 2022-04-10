<script setup lang="ts">
import { ref } from "vue";

const props = defineProps({
  round: Object,
});
const mutableRound = ref({ ...props.round });
</script>
<template>
  <tr>
    <td>{{ mutableRound.contract.name }}</td>
    <template v-for="r in mutableRound.rounds" :key="r">
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
