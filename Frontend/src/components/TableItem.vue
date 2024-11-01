<script setup lang="ts">
import type { Game, Table } from "@/types/types";
import GamePreview from "./GamePreview.vue";
import Icon from "./IconSelector.vue";
import { computed, ref } from "vue";
import Modal from "./ModalDialog.vue";
import { useToast } from "vue-toastification";

import { useTableStore } from "@/store/table-store";
const tableStore = useTableStore();
const toast = useToast();

const props = defineProps<{ table: Table }>();

const isModalVisible = ref(false);

const latestGame = computed<Game | undefined>(
  () => props.table.loadedGames[props.table.latestGameId],
);

async function deleteTable() {
  await tableStore.removeTable(props.table.id);
  toast.success("Tisch erfolgreich gelöscht");
  // do some additional checking, aka if first success false, error
  closeModal();
}

function showModal(event: PointerEvent) {
  event.preventDefault();
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}
</script>

<template>
  <RouterLink
    :to="{ name: 'table', params: { tableId: table.id } }"
    class="jass-table max-w-sm w-full lg:max-w-full flex flex-col text-center relative z-0"
  >
    <p class="font-bold">Dein Jasstisch</p>
    <p class="font-bold">{{ table.name }}</p>
    <icon icon="trash" class="absolute top-2 right-2 z-10" @click="showModal"></icon>
    <GamePreview v-if="latestGame !== undefined" :game="latestGame"></GamePreview>
  </RouterLink>

  <Modal v-show="isModalVisible" class="delete-table-modal" @close="closeModal">
    <template #header>
      <p class="font-bold">Tisch löschen</p>
    </template>
    <template #body>
      Sind Sie sicher, dass Sie den Tisch: {{ table.name }} und alle enthaltenen Spiele Löschen
      möchten?
    </template>
    <template #footer>
      <button type="button" class="btn btn-blue" @click="deleteTable">Löschen</button>
    </template>
  </Modal>
</template>
