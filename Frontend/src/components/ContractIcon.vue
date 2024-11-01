<script setup lang="ts">
import type { Contract } from "@/types/types";

defineProps<{ contract: Contract }>();
</script>

<template>
  <div class="contract-icon flex items-center justify-center pointer-events-none select-none">
    <span v-if="contract.type === 'Guschti'">5/4</span>
    <span v-else-if="contract.type === 'Joker'" class="text-xl">⭐</span>
    <img
      v-else
      class="object-contain shrink-0 w-full h-full"
      :alt="contract.name"
      :src="`/contracts/${contract.type}.svg`"
    />
  </div>
</template>

<style lang="postcss" scoped>
.contract-icon {
  width: 40px;
  height: 40px;
}
</style>
