<template>
  <nav>
    <router-link class="logo" to="/">JassTracker</router-link>
    <router-link to="/Overview">Overview</router-link>
    <router-link to="/Profile">Profile</router-link>
    <router-link to="/Help">Help-Center</router-link>
    <a @click="logout" class="cursor-pointer" v-if="this.$store.state.loggedIn"
      >Logout</a
    >
  </nav>
  <router-view />
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
