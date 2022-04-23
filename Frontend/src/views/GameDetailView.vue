<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import { onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTableStore } from "@/store/table-store";
import ScoreboardTable from "@/components/ScoreboardTable.vue";
import WaitSpinner from "@/components/WaitSpinner.vue";
import Modal from "@/components/ModalDialog.vue";

const router = useRouter();
const route = useRoute();

const gameStore = useGameStore();
const tableStore = useTableStore();

const { currentGame } = storeToRefs(gameStore);

watch(
  () => route.params.gameId,
  async (newId) => await setCurrentGameId(newId)
);

onMounted(async () => await setCurrentGameId(route.params.gameId));

onUnmounted(async () => await setCurrentGameId(""));

const isModalVisible = ref(false);

async function deleteGame() {
  if (Array.isArray(route.params.gameId)) {
    return;
  }
  await gameStore.removeGame(route.params.gameId);
  // do some additional checking for success / error handling
  closeModal();
  backToTable();
}
function showModal(event: PointerEvent) {
  event.preventDefault();
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}

async function setCurrentGameId(newId: string | string[]) {
  if (Array.isArray(newId)) {
    return;
  }
  gameStore.setCurrentGame(tableStore.currentTableId, newId);
  if (newId) {
    await gameStore.loadGame(tableStore.currentTableId, newId);
  }
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
    <button class="btn btn-blue mt-2 ml-2" @click="showModal">
      Spiel Löschen
    </button>
    <ScoreboardTable v-if="currentGame" :game="currentGame" />
    <WaitSpinner v-else />
    <Modal
      class="delete-game-modal"
      v-show="isModalVisible"
      @close="closeModal"
    >
      <template v-slot:header>
        <p class="font-bold">Spiel löschen</p>
      </template>
      <template v-slot:body>
        Sind Sie sicher, dass Sie dieses Spiel löschen möchten?
      </template>
      <template v-slot:footer>
        <button type="button" class="btn btn-blue" @click="deleteGame">
          Löschen
        </button>
      </template>
    </Modal>
  </div>
</template>
