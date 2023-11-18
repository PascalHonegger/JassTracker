<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import IconSelector from "@/components/IconSelector.vue";
import { useMetaStore } from "@/store/meta-store";
import { storeToRefs } from "pinia";
import WaitSpinner from "@/components/WaitSpinner.vue";

const store = useAuthStore();
const router = useRouter();
const metaStore = useMetaStore();

const { isLoading } = storeToRefs(metaStore);

const logoLink = computed(() => (store.loggedIn ? "/overview" : "/login"));
const hideMobileMenu = ref(true);

onMounted(async () => {
  await store.loadTokenFromStorage();
});

function logout() {
  store.logout();
  router.push("/login");
}
</script>
<style lang="postcss" scoped>
/*noinspection CssUnusedSymbol*/
.router-link-exact-active {
  @apply text-emerald-400;
}
</style>
<template>
  <div class="flex flex-col h-screen overflow-hidden">
    <nav class="border-gray-200 px-2 sm:px-4 py-2.5 bg-gray-800 grow-0">
      <div class="container flex flex-wrap justify-between items-center mx-auto">
        <div class="flex flow-row items-center gap-2">
          <button
            :aria-label="`Menü ${hideMobileMenu ? 'ausklappen' : 'einklappen'}`"
            class="md:hidden h-full text-white"
            @click.prevent="hideMobileMenu = !hideMobileMenu"
          >
            <IconSelector icon="hamburger" />
          </button>
          <RouterLink class="flex items-center text-white" :to="logoLink">JassTracker</RouterLink>
        </div>
        <div :class="{ hidden: hideMobileMenu }" class="w-full md:block md:w-auto" id="mobile-menu">
          <ul
            class="flex flex-col mt-4 md:flex-row md:space-x-8 md:mt-0 md:text-sm md:font-medium text-white"
          >
            <li>
              <RouterLink
                class="block py-2 pr-4 pl-3 text-white"
                to="/Profile"
                v-if="store.loggedIn"
                >Profil</RouterLink
              >
            </li>
            <li>
              <RouterLink class="block py-2 pr-4 pl-3 text-white" to="/help/jassTrackerHelp"
                >Hilfezentrum</RouterLink
              >
            </li>
            <li>
              <button @click="logout" class="block py-2 pr-4 pl-3 text-white" v-if="store.loggedIn">
                Ausloggen
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <div v-if="isLoading" class="flex w-full h-full justify-center items-center">
      <WaitSpinner size="large" />
    </div>
    <div class="overflow-auto grow" :class="{ invisible: isLoading }">
      <RouterView />
    </div>
  </div>
</template>
