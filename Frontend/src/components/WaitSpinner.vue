<script setup lang="ts">
defineProps<{ size: "small" | "medium" | "large" }>();
</script>
<template>
  <div
    class="spinner-border animate-spin inline-block border-2 rounded-full"
    :class="{
      'w-4': size === 'small',
      'h-4': size === 'small',
      'w-8': size === 'medium',
      'h-8': size === 'medium',
      'w-12': size === 'large',
      'h-12': size === 'large',
    }"
    role="status"
  >
    <span class="sr-only">Wird geladen...</span>
  </div>
</template>
<style lang="postcss" scoped>
.spinner-border {
  border: 0.25em solid;
  border-right-color: transparent;
  vertical-align: middle;
}
</style>
