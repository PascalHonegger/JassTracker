<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import { onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import ModalDialog from "@/components/ModalDialog.vue";
import GameItem from "@/components/GameItem.vue";
import { useMetaStore } from "@/store/meta-store";
import { useToast } from "vue-toastification";
import GameStatistics from "@/components/GameStatistics.vue";

const router = useRouter();
const route = useRoute();
const toast = useToast();

const gameStore = useGameStore();
const tableStore = useTableStore();
const metaStore = useMetaStore();

const { currentGame } = storeToRefs(gameStore);

watch(
  () => route.params.tableId,
  async (newId) => await setCurrentTableId(newId),
);

watch(
  () => route.params.gameId,
  async (newId) => await setCurrentGameId(newId),
);

onMounted(async () => {
  await setCurrentTableId(route.params.tableId);
  await setCurrentGameId(route.params.gameId);
});

onUnmounted(async () => {
  gameStore.setCurrentGame(tableStore.currentTableId, "");
  tableStore.setCurrentTable("");
});

const isModalVisible = ref(false);

async function deleteGame() {
  if (Array.isArray(route.params.gameId)) {
    return;
  }
  await gameStore.removeGame(route.params.gameId);
  toast.success("Spiel erfolgreich gelöscht");
  // do some additional checking for success / error handling
  closeModal();
  backToTable();
}
function showModal(event: MouseEvent) {
  event.preventDefault();
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}

async function setCurrentGameId(newId: string | string[]) {
  if (typeof newId !== "string") return;
  gameStore.setCurrentGame(tableStore.currentTableId, newId);
  if (newId) {
    metaStore.startLoading();
    try {
      await gameStore.loadGame(tableStore.currentTableId, newId);
    } finally {
      metaStore.stopLoading();
    }
  }
}

async function setCurrentTableId(newId: string | string[] | undefined) {
  if (typeof newId !== "string") return;
  tableStore.setCurrentTable(newId);
  await tableStore.loadTable(newId);
}

function backToTable() {
  router.push({
    name: "table",
    params: { tableId: tableStore.currentTableId },
  });
}
</script>

<template>
  <div class="container mx-auto mb-3">
    <button class="btn btn-blue mt-2" @click="backToTable">Zurück</button>
    <button class="btn btn-blue mt-2 ml-2" @click="showModal">Spiel Löschen</button>
    <template v-if="currentGame">
      <GameItem :game="currentGame" />
      <GameStatistics :game="currentGame" />
    </template>
    <ModalDialog v-if="isModalVisible" class="delete-game-modal" @close="closeModal">
      <template #header>
        <p class="font-bold">Spiel löschen</p>
      </template>
      <template #body> Sind Sie sicher, dass Sie dieses Spiel löschen möchten? </template>
      <template #footer>
        <button type="button" class="btn btn-blue" @click="deleteGame">Löschen</button>
      </template>
    </ModalDialog>
  </div>
</template>
