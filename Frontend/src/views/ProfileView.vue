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

<template>
  <div class="container mx-auto text-center flex flex-col gap-4 py-8">
    <div class="container flex flex-col md:flex-row md:justify-around gap-4">
      <form autocomplete="on" @submit.prevent="updateDisplayName">
        <div class="mb-6 flex flex-col">
          <label for="username">Benutzername</label>
          <input
            id="username"
            v-model="authStore.username"
            autocomplete="username"
            class="box-input"
            name="username"
            type="text"
            disabled
          />
        </div>
        <div class="mb-6 flex flex-col">
          <label for="displayname">Anzeigename</label>
          <input
            id="displayname"
            v-model="newDisplayName"
            autocomplete="nickname"
            class="box-input"
            name="displayname"
            type="text"
            :disabled="loadingDisplayName || isGuest"
          />
        </div>
        <button
          v-if="!isGuest"
          type="submit"
          :disabled="loadingDisplayName"
          class="btn btn-blue self-center"
        >
          Anzeigename Aktualisieren

          <WaitSpinner v-if="loadingDisplayName" size="small"></WaitSpinner>
        </button>
      </form>
      <form v-if="!isGuest" autocomplete="on" @submit.prevent="updatePassword">
        <div class="mb-6 flex flex-col">
          <label for="old-password">Altes Passwort</label>
          <input
            id="old-password"
            v-model="oldPassword"
            autocomplete="old-password"
            class="box-input"
            name="old-password"
            type="password"
            :disabled="loadingNewPassword"
          />
        </div>
        <div v-if="confirmOldPasswordFailed" class="text-red-600 mt-4">
          Das alte Passwort ist falsch!
        </div>
        <div class="mb-6 flex flex-col">
          <label for="new-password">Neues Passwort</label>
          <input
            id="new-password"
            v-model="newPassword"
            autocomplete="new-password"
            class="box-input"
            name="new-password"
            type="password"
            :disabled="loadingNewPassword"
          />
        </div>
        <div class="mb-6 flex flex-col">
          <label for="password-confirm">Neues Passwort Bestätigen</label>
          <input
            id="password-confirm"
            v-model="passwordConfirm"
            autocomplete="new-password"
            class="box-input"
            name="password-confirm"
            type="password"
            :disabled="loadingNewPassword"
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
      <button class="btn btn-red" @click="deleteAccount">Konto löschen</button>
    </div>
    <PlayerStatistics />
  </div>
</template>

<style lang="postcss" scoped>
label {
  @apply block mb-2 text-sm font-medium text-gray-900;
}
</style>
