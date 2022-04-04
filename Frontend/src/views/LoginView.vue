<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";

const store = useAuthStore();
const router = useRouter();
function login() {
  router.push("/overview");
  store.setLoggedIn();
}
function loginAsGuest() {
  router.push("/overview");
  store.setLoggedIn();
}
</script>
<template>
  <div class="home">
    <div class="image">
      <img src="../assets/jass.jpg" alt="Jasskarten" />
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
