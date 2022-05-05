<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import WaitSpinner from "@/components/WaitSpinner.vue";

const authStore = useAuthStore();
const router = useRouter();

const loginLoading = ref<boolean>(false);
const loginAsGuestLoading = ref<boolean>(false);
const loading = computed(() => loginLoading.value || loginAsGuestLoading.value);
const username = ref("");
const password = ref("");

onMounted(() => {
  if (authStore.loggedIn) {
    router.push("/overview");
  }
});

async function login() {
  loginLoading.value = true;
  const newLoginRequest = await authStore.createLoginRequest(username.value, password.value);
  await router.push("/overview");
  await authStore.setLoggedIn();
}
async function loginAsGuest() {
  loginAsGuestLoading.value = true;
  await authStore.setLoggedIn();
  await router.push("/overview");
}
</script>

<style lang="scss" scoped>
.login input {
  @apply bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5;
}

.default-h1 {
  @apply font-medium leading-tight text-5xl mb-2 text-blue-600;
}
</style>

<template>
  <div class="grid grid-rows-1 grid-cols-1 md:grid-cols-2 w-full h-full">
    <div class="image hidden md:block h-full">
      <img
        loading="lazy"
        class="object-cover h-full w-full"
        src="../assets/jass.webp"
        alt="Jasskarten"
      />
    </div>
    <div class="login text-center flex flex-col h-full overflow-auto">
      <h1 class="default-h1 mt-auto">JassTracker</h1>
      <p class="font-medium">Coiffer Jass auf das nächste Level anheben</p>
      <div class="flex flex-col my-8 self-center w-48">
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
              v-model="username"
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
              v-model="password"
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
      <div class="mb-auto mx-1">
        <p>Neu beim JassTracker? Erstellen Sie einen Account!</p>
      </div>
    </div>
  </div>
</template>
