<template>
  <div>
    <nav>
      <router-link class="logo" to="/">JassTracker</router-link>
      <router-link to="/Overview" v-if="this.$store.state.loggedIn"
        >Overview</router-link
      >
      <router-link to="/Profile" v-if="this.$store.state.loggedIn"
        >Profile</router-link
      >
      <router-link to="/Help">Help-Center</router-link>
      <a
        @click="logout"
        class="cursor-pointer"
        v-if="this.$store.state.loggedIn"
        >Logout</a
      >
    </nav>
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {};
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      this.$router.push("/");
    },
  },
};
</script>
