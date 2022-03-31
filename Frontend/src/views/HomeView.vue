<template>
  <div class="home">
    <div class="image">
      <img src="../assets/jass.jpg" />
    </div>
    <div class="login flex flex-col justify-center">
      <h1 class="default-h1">JassTracker</h1>
      <p class="font-medium">Slogan TBD</p>
      <div class="flex flex-col my-8 self-center">
        <form @submit.prevent="login" autocomplete="on">
          <div class="mb-6">
            <label
              class="block mb-2 text-sm font-medium text-gray-900"
              for="username"
              >Benutzername</label
            >
            <input
              autocomplete="username"
              id="username"
              name="username"
              type="text"
            />
          </div>
          <div class="mb-6">
            <label
              class="block mb-2 text-sm font-medium text-gray-900"
              for="password"
              >Passwort</label
            >
            <input
              autocomplete="current-password"
              id="password"
              name="password"
              type="password"
            />
          </div>
          <button type="submit" class="btn btn-blue self-center">Login</button>
        </form>
      </div>
      <p>------ oder -----</p>
      <button @click="loginAsGuest" class="btn btn-blue self-center my-8">
        Als Gast spielen
      </button>
      <div>
        <p>Neu beim JassTracker? Erstellen Sie einen Account!</p>
      </div>
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
    loginAsGuest(event: any) {
      event.preventDefault();
      this.$router.push("overview");
      this.$store.commit("setLoggedIn", true);
    },
  },
};
</script>
