<script lang="ts" setup>
import { ref } from "vue";
import { useAuthStore } from "@/store/auth-store";
import { usePlayerStore } from "@/store/player-store";
import { storeToRefs } from "pinia";
import router from "@/router";
import WaitSpinner from "@/components/WaitSpinner.vue";

const authStore = useAuthStore();
const playerStore = usePlayerStore();

const password = "";
const passwordConfirm = "";

const loading = ref<boolean>(false);
const newDisplayName = ref(authStore.displayName ?? "");

async function update() {
  loading.value = true;
  await playerStore.updateDisplayName(newDisplayName.value);
  loading.value = false;
}

const { isGuest } = storeToRefs(authStore);

async function deleteAccount() {
  await playerStore.deleteCurrentPlayerAccount;
  await router.push("/");
}
</script>

<template>
  <div class="container mx-auto text-center flex flex-col">
    <form @submit.prevent="update" autocomplete="on" class="pt-4">
      <div class="mb-6 flex flex-col">
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
          disabled
          v-model="authStore.username"
        />
      </div>
      <div class="mb-6 flex flex-col">
        <label
          class="block mb-2 text-sm font-medium text-gray-900"
          for="displayname"
          >Anzeige Name</label
        >
        <input
          autocomplete="nickname"
          class="box-input"
          id="displayname"
          name="displayname"
          type="text"
          :disabled="loading"
          v-model="newDisplayName"
        />
      </div>
      <!-- maybe ask for old pw too, aka old pw, new pw & new pw confirm -->
      <div class="mb-6 flex flex-col">
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
      <div class="mb-6 flex flex-col">
        <label
          class="block mb-2 text-sm font-medium text-gray-900"
          for="password-confirm"
          >Passwort Bestätigen</label
        >
        <input
          autocomplete="new-password"
          class="box-input"
          id="password-confirm"
          name="password-confirm"
          type="password"
          :disabled="loading"
          v-model="passwordConfirm"
        />
      </div>
      <button
        type="submit"
        :disabled="loading"
        class="btn btn-blue self-center"
      >
        Aktuallisieren

        <WaitSpinner v-if="loading"></WaitSpinner>
      </button>
    </form>
    <div v-if="!isGuest" class="p-4">
      <button @click="deleteAccount" class="btn btn-blue">Konto löschen</button>
    </div>
  </div>
</template>
