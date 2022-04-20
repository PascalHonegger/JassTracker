<script setup lang="ts">
import { Table } from "@/types/types";
import GamePreview from "./GamePreview.vue";
import Icon from "./Icon.vue";
import { computed, ref } from "vue";
import Modal from "./Modal.vue";

import { useTableStore } from "@/store/table-store";
const tableStore = useTableStore();

const props = defineProps<{ table: Table }>();

const isModalVisible = ref(false);

const latestGame = computed(
  () => props.table.loadedGames[props.table.latestGameId]
);

async function deleteTable() {
  const removalSuccess = await tableStore.removeTable(props.table.id);
  // do some additional checking, aka if first success false, error
  // on second success try again or just load tables again in overview?
  closeModal();
}

function showModal(event: any) {
  event.preventDefault();
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
}
</script>
<template>
  <RouterLink
    :to="{ name: 'table', params: { id: table.id } }"
    class="table max-w-sm w-full lg:max-w-full lg:flex flex-col text-center relative z-0"
  >
    <p class="font-bold">{{ table.name }}</p>
    <icon
      @click="showModal"
      icon="trash"
      classes="absolute top-2 right-2 z-10"
    ></icon>
    <GamePreview v-if="latestGame != null" :game="latestGame"></GamePreview>
  </RouterLink>

  <Modal class="delete-table-modal" v-show="isModalVisible" @close="closeModal">
    <template v-slot:header>
      <p class="font-bold">Tisch löschen</p>
    </template>
    <template v-slot:body>
      Sind Sie sicher, dass Sie den Tisch: {{ props.table.name }} und alle
      enthaltenen Spiele Löschen möchten?
    </template>
    <template v-slot:footer>
      <button type="button" class="btn btn-blue" @click="deleteTable">
        Löschen
      </button>
    </template>
  </Modal>
</template>
