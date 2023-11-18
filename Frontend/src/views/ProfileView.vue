<script lang="ts" setup>
import { ref } from "vue";
import { useAuthStore } from "@/store/auth-store";
import { usePlayerStore } from "@/store/player-store";
import { storeToRefs } from "pinia";
import router from "@/router";
import WaitSpinner from "@/components/WaitSpinner.vue";
import { useToast } from "vue-toastification";
import PlayerStatistics from "@/components/PlayerStatistics.vue";

const authStore = useAuthStore();
const playerStore = usePlayerStore();
const toast = useToast();

const loadingDisplayName = ref(false);
const loadingNewPassword = ref(false);
const newDisplayName = ref(authStore.displayName ?? "");
const oldPassword = ref("");
const newPassword = ref("");
const passwordConfirm = ref("");
const confirmConfirmationFailed = ref(false);
const confirmOldPasswordFailed = ref(false);

async function updateDisplayName() {
  loadingDisplayName.value = true;
  await playerStore.updateDisplayName(newDisplayName.value);
  toast.success("Anzeigename erfolgreich aktualisiert");
  loadingDisplayName.value = false;
}
async function updatePassword() {
  confirmOldPasswordFailed.value = false;
  confirmConfirmationFailed.value = false;
  loadingNewPassword.value = true;
  const updatePasswordSuccessful = await playerStore.updatePassword(
    oldPassword.value,
    newPassword.value,
  );
  loadingNewPassword.value = false;
  if (!updatePasswordSuccessful) {
    confirmOldPasswordFailed.value = true;
  }
  if (newPassword.value != passwordConfirm.value) {
    confirmConfirmationFailed.value = true;
  } else if (updatePasswordSuccessful) {
    await router.push("/overview");
  }
}

const { isGuest } = storeToRefs(authStore);

async function deleteAccount() {
  await playerStore.deleteCurrentPlayerAccount();
  toast.success("Spieler erfolgreich gelöscht");
  await router.push("/");
}
</script>

<style lang="postcss" scoped>
label {
  @apply block mb-2 text-sm font-medium text-gray-900;
}
</style>

<template>
  <div class="container mx-auto text-center flex flex-col gap-4 py-8">
    <div class="container flex flex-col md:flex-row md:justify-around gap-4">
      <form @submit.prevent="updateDisplayName" autocomplete="on">
        <div class="mb-6 flex flex-col">
          <label for="username">Benutzername</label>
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
          <label for="displayname">Anzeigename</label>
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
          Anzeigename Aktualisieren

          <WaitSpinner v-if="loadingDisplayName" size="small"></WaitSpinner>
        </button>
      </form>
      <form @submit.prevent="updatePassword" v-if="!isGuest" autocomplete="on">
        <div class="mb-6 flex flex-col">
          <label for="old-password">Altes Passwort</label>
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
        <div class="mb-6 flex flex-col">
          <label for="new-password">Neues Passwort</label>
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
        <div class="mb-6 flex flex-col">
          <label for="password-confirm">Neues Passwort Bestätigen</label>
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
          Die Bestätigung vom Passwort stimmt nicht mit dem neuen Passwort überein!
        </div>
        <button type="submit" :disabled="loadingNewPassword" class="btn btn-blue self-center">
          Passwort Ändern
          <WaitSpinner v-if="loadingNewPassword" size="small"></WaitSpinner>
        </button>
      </form>
    </div>
    <div v-if="!isGuest">
      <button @click="deleteAccount" class="btn btn-red">Konto löschen</button>
    </div>
    <PlayerStatistics />
  </div>
</template>
