<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { computed } from "vue";
import { useRouter } from "vue-router";

const store = useAuthStore();
const router = useRouter();

const logoLink = computed(() => (store.loggedIn ? "/overview" : "/login"));

function logout() {
  store.logout();
  router.push("/login");
}
</script>
<style lang="scss">
nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
<template>
  <div>
    <nav class="bg-white border-gray-200 px-2 sm:px-4 py-2.5 dark:bg-gray-800">
      <div
        class="container flex flex-wrap justify-between items-center mx-auto"
      >
        <router-link class="flex items-center text-white" :to="logoLink"
          >JassTracker</router-link
        >
        <div class="hidden w-full md:block md:w-auto" id="mobile-menu">
          <ul
            class="flex flex-col mt-4 md:flex-row md:space-x-8 md:mt-0 md:text-sm md:font-medium text-white"
          >
            <li>
              <router-link
                class="block py-2 pr-4 pl-3 text-white"
                to="/Profile"
                v-if="store.loggedIn"
                >Profile</router-link
              >
            </li>
            <li>
              <router-link class="block py-2 pr-4 pl-3 text-white" to="/Help"
                >Help-Center</router-link
              >
            </li>
            <li>
              <button
                @click="logout"
                class="block py-2 pr-4 pl-3 text-white"
                v-if="store.loggedIn"
              >
                Logout
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>
