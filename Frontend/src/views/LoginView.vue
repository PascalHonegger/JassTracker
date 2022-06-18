<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import LoginRegisterLayout from "@/components/LoginRegisterLayout.vue";

const authStore = useAuthStore();
const router = useRouter();

const loginLoading = ref<boolean>(false);
const loginFailed = ref<boolean>(false);
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
  loginFailed.value = false;
  const loginSuccessful = await authStore.loginPlayer(
    username.value,
    password.value
  );
  if (loginSuccessful) {
    await router.push("/overview");
  } else {
    loginLoading.value = false;
    loginFailed.value = true;
  }
}
async function loginAsGuest() {
  loginAsGuestLoading.value = true;
  await authStore.guestAccess();
  await router.push("/overview");
}
</script>

<template>
  <LoginRegisterLayout>
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
            class="box-input w-full"
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
            class="box-input w-full"
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

          <WaitSpinner v-if="loginLoading" size="small"></WaitSpinner>
        </button>
        <div v-if="loginFailed" class="text-red-600 mt-4">
          Ungültiger Benutzername oder Passwort
        </div>
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

      <WaitSpinner v-if="loginAsGuestLoading" size="small"></WaitSpinner>
    </button>
    <div class="mb-auto mx-1">
      <p>
        Neu beim JassTracker?
        <RouterLink to="/register" class="underline"
          >Erstelle einen neuen Account!</RouterLink
        >
      </p>
    </div>
  </LoginRegisterLayout>
</template>
