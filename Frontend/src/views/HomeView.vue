<template>
  <div class="home">
    <div class="image">
      <img src="../assets/jass.jpg" />
    </div>
    <div class="login">
      <p>Hello World, this is now custom content</p>
      <button @click="login">Login</button>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: "HomeView",
  data() {
    return {};
  },
  methods: {
    login(event: any) {
      event.preventDefault();
      this.$router.push("overview");
      this.$store.commit("setLoggedIn", true);
    },
  },
};
</script>
