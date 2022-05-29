<script setup lang="ts">
import { Responsive } from "vue3-charts";
import WaitSpinner from "@/components/WaitSpinner.vue";
import IconSelector from "@/components/IconSelector.vue";

const emits = defineEmits<{
  (event: "refresh"): Promise<void>;
}>();
defineProps<{ title: string; isLoading: boolean }>();
</script>

<template>
  <div v-if="!isLoading">
    <h1 class="text-5xl font-normal leading-normal">
      {{ title }}
      <IconSelector
        class="w-10 cursor-pointer inline"
        @click="emits('refresh')"
        icon="refresh"
      />
    </h1>
    <Responsive class="w-full">
      <template #main="{ width }">
        <slot :width="width" />
      </template>
    </Responsive>
  </div>
  <div v-else class="flex w-full h-full justify-center items-center">
    <WaitSpinner size="medium" />
  </div>
</template>
