<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import WaitSpinner from "@/components/WaitSpinner.vue";

const store = useAuthStore();
const router = useRouter();

const loginLoading = ref<boolean>(false);
const loginAsGuestLoading = ref<boolean>(false);
const loading = computed(() => loginLoading.value || loginAsGuestLoading.value);

onMounted(() => {
  if (store.loggedIn) {
    router.push("/overview");
  }
});

async function login() {
  loginLoading.value = true;
  await store.setLoggedIn();
  await router.push("/overview");
}
async function loginAsGuest() {
  loginAsGuestLoading.value = true;
  await store.setLoggedIn();
  await router.push("/overview");
}
</script>
<style lang="scss">
.home {
  width: 100%;
  display: flex;
}

.login {
  text-align: center;
  width: 50%;
}

.image {
  width: 50%;
  img {
    height: auto;
    width: 100%;
  }
}

@media (max-width: 768px) {
  .image {
    display: none;
  }
  .login {
    width: 100%;
  }
}
</style>
<template>
  <div class="home">
    <div class="image">
      <img src="../assets/jass.webp" alt="Jasskarten" />
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
              :disabled="loading"
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
              :disabled="loading"
            />
          </div>
          <button
            type="submit"
            :disabled="loading"
            class="btn btn-blue self-center"
          >
            Login

            <WaitSpinner v-if="loginLoading"></WaitSpinner>
          </button>
        </form>
      </div>
      <p>------ oder -----</p>
      <button
        type="button"
        @click="loginAsGuest"
        :disabled="loading"
        class="btn btn-blue self-center my-8"
      >
        Als Gast spielen

        <WaitSpinner v-if="loginAsGuestLoading"></WaitSpinner>
      </button>
      <div>
        <p>Neu beim JassTracker? Erstellen Sie einen Account!</p>
      </div>
    </div>
  </div>
</template>
