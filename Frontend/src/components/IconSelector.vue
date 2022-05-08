<script setup lang="ts">
const props = defineProps<{
  icon: string;
  classes?: string;
}>();
</script>

<template>
  <svg
    v-if="props.icon === 'trash'"
    :class="props.classes"
    xmlns="http://www.w3.org/2000/svg"
    class="h-6 w-6"
    fill="none"
    viewBox="0 0 24 24"
    stroke="currentColor"
    stroke-width="2"
  >
    <path
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
    />
  </svg>
  <svg
    v-else-if="props.icon === 'hamburger'"
    fill="#ffffff"
    viewBox="0 0 100 80"
    class="w-6 h-6"
  >
    <rect width="100" height="20"></rect>
    <rect y="30" width="100" height="20"></rect>
    <rect y="60" width="100" height="20"></rect>
  </svg>
</template>
