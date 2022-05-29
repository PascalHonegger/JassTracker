<script lang="ts" setup>
import { ref } from "vue";
import { useAuthStore } from "@/store/auth-store";
import { usePlayerStore } from "@/store/player-store";
import { storeToRefs } from "pinia";
import router from "@/router";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { useToast } from "vue-toastification";

const authStore = useAuthStore();
const playerStore = usePlayerStore();
const toast = useToast();

const loadingDisplayName = ref<boolean>(false);
const loadingNewPassword = ref<boolean>(false);
const newDisplayName = ref(authStore.displayName ?? "");
const oldPassword = ref("");
const newPassword = ref("");
const passwordConfirm = ref("");
const confirmConfirmationFailed = ref<boolean>(false);
const confirmOldPasswordFailed = ref<boolean>(false);

async function updateDisplayName() {
  loadingDisplayName.value = true;
  await playerStore.updateDisplayName(newDisplayName.value);
  toast.success("Anzeigename erfolgreich aktuallisiert");
  loadingDisplayName.value = false;
}
async function updatePassword() {
  confirmOldPasswordFailed.value = false;
  confirmConfirmationFailed.value = false;
  loadingNewPassword.value = true;
  const updatePasswordSuccessful = await playerStore.updatePassword(
    oldPassword.value,
    newPassword.value
  );
  loadingNewPassword.value = false;
  if (!updatePasswordSuccessful) {
    confirmOldPasswordFailed.value = true;
  }
  if (newPassword.value != passwordConfirm.value) {
    confirmConfirmationFailed.value = true;
  }
  if (updatePasswordSuccessful) {
    await router.push("/overview");
  }
}

const { isGuest } = storeToRefs(authStore);

async function deleteAccount() {
  await playerStore.deleteCurrentPlayerAccount;
  toast.success("Spieler erfolgreich gelöscht");
  await router.push("/");
}
</script>

<template>
  <div class="container mx-auto text-center flex flex-col">
    <form @submit.prevent="updateDisplayName" autocomplete="on" class="pt-4">
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
          >Anzeigename</label
        >
        <input
          autocomplete="nickname"
          class="box-input"
          id="displayname"
          name="displayname"
          type="text"
          :disabled="loadingDisplayName || isGuest"
          v-model="newDisplayName"
        />
      </div>
      <button
        type="submit"
        :disabled="loadingDisplayName"
        v-if="!isGuest"
        class="btn btn-blue self-center"
      >
        Anzeigename Aktuallisieren

        <WaitSpinner v-if="loadingDisplayName" size="small"></WaitSpinner>
      </button>
    </form>
    <form @submit.prevent="updatePassword" autocomplete="on" class="pt-4">
      <div class="mb-6 flex flex-col" v-if="!isGuest">
        <label
          class="block mb-2 text-sm font-medium text-gray-900"
          for="old-password"
          >Altes Passwort</label
        >
        <input
          autocomplete="old-password"
          class="box-input"
          id="old-password"
          name="old-password"
          type="password"
          :disabled="loadingNewPassword"
          v-model="oldPassword"
        />
      </div>
      <div v-if="confirmOldPasswordFailed" class="text-red-600 mt-4">
        Das alte Passwort ist falsch!
      </div>
      <div class="mb-6 flex flex-col" v-if="!isGuest">
        <label
          class="block mb-2 text-sm font-medium text-gray-900"
          for="new-password"
          >Neues Passwort</label
        >
        <input
          autocomplete="new-password"
          class="box-input"
          id="new-password"
          name="new-password"
          type="password"
          :disabled="loadingNewPassword"
          v-model="newPassword"
        />
      </div>
      <div class="mb-6 flex flex-col" v-if="!isGuest">
        <label
          class="block mb-2 text-sm font-medium text-gray-900"
          for="password-confirm"
          >Neues Passwort Bestätigen</label
        >
        <input
          autocomplete="new-password"
          class="box-input"
          id="password-confirm"
          name="password-confirm"
          type="password"
          :disabled="loadingNewPassword"
          v-model="passwordConfirm"
        />
      </div>
      <div v-if="confirmConfirmationFailed" class="text-red-600 mt-4">
        Die Bestätigung vom Passwort stimmt nicht mit dem neuen Passwort
        überein!
      </div>
      <button
        type="submit"
        :disabled="loadingNewPassword"
        v-if="!isGuest"
        class="btn btn-blue self-center"
      >
        Passwort Ändern
        <WaitSpinner v-if="loadingNewPassword" size="small"></WaitSpinner>
      </button>
    </form>
    <div v-if="!isGuest" class="p-4">
      <button @click="deleteAccount" class="btn btn-blue">Konto löschen</button>
    </div>
  </div>
</template>
