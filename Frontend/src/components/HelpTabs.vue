<script setup lang="ts">
import { ref } from "vue";

const jassTrackerHelpIsActive = ref<boolean>(true);
const jassRulesIsActive = ref<boolean>(false);
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
    <div @click="switchTab" v-show="jassTrackerHelpIsActive" />
    <div @click="switchTab" v-show="jassRulesIsActive" />
    <slot></slot>
  </div>
</template>
