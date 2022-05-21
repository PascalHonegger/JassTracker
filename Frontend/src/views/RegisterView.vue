<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import LoginRegisterLayout from "@/components/LoginRegisterLayout.vue";

const authStore = useAuthStore();
const router = useRouter();

const registerLoading = ref<boolean>(false);
const registerFailed = ref<boolean>(false);
const loginAsGuestLoading = ref<boolean>(false);
const loading = computed(
  () => registerLoading.value || loginAsGuestLoading.value
);
const username = ref("");
const displayName = ref("");
const password = ref("");

onMounted(() => {
  if (authStore.loggedIn) {
    router.push("/overview");
  }
});

async function register() {
  registerLoading.value = true;
  registerFailed.value = false;
  const registerSuccessful = await authStore.registerPlayer(
    username.value,
    displayName.value,
    password.value
  );
  if (registerSuccessful) {
    await router.push("/overview");
  } else {
    registerLoading.value = false;
    registerFailed.value = true;
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
      <form @submit.prevent="register" autocomplete="on">
        <div class="mb-6">
          <label
            class="block mb-2 text-sm font-medium text-gray-900"
            for="username"
            >Benutzername</label
          >
          <input
            autocomplete="username"
            class="box-input"
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
            for="nickname"
            >Anzeigename</label
          >
          <input
            autocomplete="nickname"
            class="box-input"
            id="nickname"
            name="nickname"
            type="text"
            :disabled="loading"
            v-model="displayName"
          />
        </div>
        <div class="mb-6">
          <label
            class="block mb-2 text-sm font-medium text-gray-900"
            for="password"
            >Passwort</label
          >
          <input
            autocomplete="new-password"
            class="box-input"
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
          Registrieren

          <WaitSpinner v-if="registerLoading"></WaitSpinner>
        </button>
        <div v-if="registerFailed" class="text-red-600 mt-4">
          Registrierung fehlgeschlagen
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

      <WaitSpinner v-if="loginAsGuestLoading"></WaitSpinner>
    </button>
    <div class="mb-auto mx-1">
      <p>
        Bereits registriert?
        <RouterLink to="/login" class="underline"
          >Melde dich stattdessen an!</RouterLink
        >
      </p>
    </div>
  </LoginRegisterLayout>
</template>