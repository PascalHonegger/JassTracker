<script setup lang="ts">
import { ref } from "vue";

const jassTrackerHelpIsActive = ref(true);
const jassRulesIsActive = ref(false);
function switchTab() {
  if (jassTrackerHelpIsActive.value) {
    jassTrackerHelpIsActive.value = !jassTrackerHelpIsActive.value;
  }
  if (jassRulesIsActive.value) {
    jassRulesIsActive.value = !jassRulesIsActive.value;
  }
}
</script>

<template>
  <div>
    <div v-show="jassTrackerHelpIsActive" @click="switchTab" />
    <div v-show="jassRulesIsActive" @click="switchTab" />
    <slot></slot>
  </div>
</template>
