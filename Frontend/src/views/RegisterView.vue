<script setup lang="ts">
import { useAuthStore } from "@/store/auth-store";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import LoginRegisterLayout from "@/components/LoginRegisterLayout.vue";
import { useToast } from "vue-toastification";

const authStore = useAuthStore();
const router = useRouter();
const toast = useToast();

const registerLoading = ref(false);
const registerFailed = ref(false);
const loginAsGuestLoading = ref(false);
const loading = computed(() => registerLoading.value || loginAsGuestLoading.value);
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
    password.value,
  );
  if (registerSuccessful) {
    toast.success("Spieler wurde erfolgreich registriert");
    await router.push("/overview");
  } else {
    registerLoading.value = false;
    registerFailed.value = true;
  }
}
</script>

<template>
  <LoginRegisterLayout>
    <div class="flex flex-col my-8 self-center w-48">
      <form autocomplete="on" @submit.prevent="register">
        <div class="mb-6">
          <label class="block mb-2 text-sm font-medium text-gray-900" for="username"
            >Benutzername</label
          >
          <input
            id="username"
            v-model="username"
            autocomplete="username"
            class="box-input w-full"
            name="username"
            type="text"
            :disabled="loading"
          />
        </div>
        <div class="mb-6">
          <label class="block mb-2 text-sm font-medium text-gray-900" for="nickname"
            >Anzeigename</label
          >
          <input
            id="nickname"
            v-model="displayName"
            autocomplete="nickname"
            class="box-input w-full"
            name="nickname"
            type="text"
            :disabled="loading"
          />
        </div>
        <div class="mb-6">
          <label class="block mb-2 text-sm font-medium text-gray-900" for="password"
            >Passwort</label
          >
          <input
            id="password"
            v-model="password"
            autocomplete="new-password"
            class="box-input w-full"
            name="password"
            type="password"
            :disabled="loading"
          />
        </div>
        <button type="submit" :disabled="loading" class="btn btn-blue self-center">
          Registrieren

          <WaitSpinner v-if="registerLoading" size="small"></WaitSpinner>
        </button>
        <div v-if="registerFailed" class="text-red-600 mt-4">Registrierung fehlgeschlagen</div>
      </form>
    </div>
    <div class="mb-auto mx-1">
      <p>
        Bereits registriert?
        <RouterLink to="/login" class="underline">Melde dich stattdessen an!</RouterLink>
      </p>
    </div>
  </LoginRegisterLayout>
</template>
