<style lang="scss">
h2 {
  @apply text-4xl font-normal leading-normal mt-4;
}

h3 {
  @apply text-3xl font-normal leading-normal mt-4;
}
</style>
<script lang="ts">
import { ref } from "vue";

const isOpen = ref(false);
export default {
  name: "helpView",
  props: ["title"],
  data() {
    return {
      isOpen: false,
    };
  },
};
</script>
<template>
  <div @click="isOpen = !isOpen">
    <a href="#">
      {{ title }}
    </a>
    <IconSelector icon="dropDownArrow" />
    <transition name="fade" apear>
      <div v-if="isOpen">
        <div class="block py-2 pr-4 pl-3 text-white">
          <RouterLink
            class="block py-2 pr-4 pl-3 text-white"
            to="/Help/jassTrackerHelp"
            >JassTracker Bedienung</RouterLink
          >
        </div>
        <div class="block py-2 pr-4 pl-3 text-white">
          <RouterLink
            class="block py-2 pr-4 pl-3 text-white"
            to="/Help/jassRules"
          >
            Jass Regeln</RouterLink
          >
        </div>
      </div>
    </transition>
  </div>
</template>
