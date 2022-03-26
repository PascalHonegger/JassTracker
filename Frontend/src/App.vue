<template>
  <div>
    <nav class="bg-white border-gray-200 px-2 sm:px-4 py-2.5 dark:bg-gray-800">
      <div
        class="container flex flex-wrap justify-between items-center mx-auto"
      >
        <router-link class="flex items-center text-white" :to="this.logoLink"
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
                v-if="this.$store.state.loggedIn"
                >Profile</router-link
              >
            </li>
            <li>
              <router-link class="block py-2 pr-4 pl-3 text-white" to="/Help"
                >Help-Center</router-link
              >
            </li>
            <li>
              <a
                @click="logout"
                class="cursor-pointer block py-2 pr-4 pl-3 text-white"
                v-if="this.$store.state.loggedIn"
                >Logout</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script lang="ts">
export default {
  name: "App",
  data() {
    return {};
  },
  computed: {
    logoLink() {
      return this.$store.state.loggedIn ? "/Overview" : "/";
    },
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      this.$router.push("/");
    },
  },
};
</script>
