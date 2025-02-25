<script setup lang="ts">
import type { Table } from "@/types/types";
import { useStatisticsStore } from "@/store/statistics-store";
import { computed, onMounted, ref, watch } from "vue";
import type { WebTableStatistics } from "@/services/web-model";
import { Bar, Chart, Grid, Tooltip } from "vue3-charts";
import StatisticsContainer from "@/components/StatisticsContainer.vue";
import { useContractStore } from "@/store/contract-store";
import { maxScore, sumMultipliers } from "@/util/constants";
import type { AxisConfig, Data } from "vue3-charts/src/types";

const statisticsStore = useStatisticsStore();
const contractStore = useContractStore();

const props = defineProps<{ table: Table }>();
const tableStatistics = ref<WebTableStatistics>();
const isLoading = ref(false);

onMounted(async () => {
  await contractStore.loadContracts();
  await refresh();
});
watch(props.table, async () => await refresh());

async function refresh() {
  isLoading.value = true;
  try {
    tableStatistics.value = await statisticsStore.getTableStatistics(props.table.id);
  } finally {
    isLoading.value = false;
  }
}

const scoreOverGames = computed(
  () =>
    tableStatistics.value?.scoresOverTime.map((scores, index) => ({
      game: index + 1,
      team1: scores.total.team1Score ?? undefined,
      team2: scores.total.team2Score ?? undefined,
    })) ?? [],
);

const playerAverages = computed(() => {
  return tableStatistics.value === undefined
    ? []
    : tableStatistics.value.playerAverages.map((p) => ({
        label: p.displayName,
        average: p.average,
        weightedAverage: p.weightedAverage,
      }));
});

const contractAverages = computed(() => {
  return tableStatistics.value === undefined
    ? []
    : Object.entries(tableStatistics.value.contractAverages)
        .map(([contractId, average]) => ({
          contract: contractStore.getContract(contractId),
          average: average ?? undefined,
        }))
        .sort((a, b) => a.contract.multiplier - b.contract.multiplier)
        .map(({ contract, average }) => ({
          contract: `${contract.multiplier}x ${contract.name}`,
          average,
        }));
});

const margin = ref({
  left: 20,
  top: 20,
  right: 20,
  bottom: 0,
});

// Not exported by vue3-charts so we have to copy it here
interface ChartAxis {
  primary: AxisConfig;
  secondary: AxisConfig;
}

const scoreAxis = computed<ChartAxis>(() => ({
  primary: {
    domain: ["dataMin", "dataMax"],
    type: "band",
  },
  secondary: {
    domain: [0, maxScore * sumMultipliers],
    type: "linear",
  },
}));
const averageAxis = computed<ChartAxis>(() => ({
  primary: {
    domain: ["dataMin", "dataMax"],
    type: "band",
  },
  secondary: {
    domain: [0, maxScore],
    type: "linear",
  },
}));
</script>

<template>
  <StatisticsContainer
    v-slot="{ width }"
    title="Tisch Statistiken"
    :is-loading="isLoading"
    @refresh="refresh"
  >
    <h2 class="text-xl font-normal leading-normal">
      Punktzahlen pro Spiel und Team (<span style="color: orange">Team 1</span> vs
      <span style="color: blue">Team 2</span>)
    </h2>
    <Chart
      :size="{ width, height: 400 }"
      :data="scoreOverGames as Data[]"
      :margin="margin"
      :direction="'horizontal'"
      :axis="scoreAxis"
    >
      <template #layers>
        <Grid />
        <Bar :data-keys="['game', 'team1']" :bar-style="{ fill: 'orange' }" />
        <Bar :data-keys="['game', 'team2']" :bar-style="{ fill: 'blue' }" />
      </template>

      <template #widgets>
        <Tooltip
          border-color="#48CAE4"
          :config="{
            team1: { color: 'orange', label: 'Team 1' },
            team2: { color: 'blue', label: 'Team 2' },
            game: { label: 'Spiel' },
          }"
        />
      </template>
    </Chart>

    <h2 class="text-xl font-normal leading-normal">Durchschnittliche Punktzahlen pro Spieler</h2>
    <Chart
      :size="{ width, height: 300 }"
      :data="playerAverages"
      :margin="margin"
      :direction="'vertical'"
      :axis="averageAxis"
    >
      <template #layers>
        <Grid />
        <Bar :data-keys="['label', 'average']" :bar-style="{ fill: '#0096c7' }" />
        <Bar :data-keys="['label', 'weightedAverage']" :bar-style="{ fill: '#48cae4' }" />
      </template>

      <template #widgets>
        <Tooltip
          border-color="#48CAE4"
          :config="{
            label: { hide: true },
            average: { color: '#0096c7', label: 'Mittelwert', format: ',.1f' },
            weightedAverage: {
              color: '#48cae4',
              label: 'Gewichteter Mittelwert',
              format: ',.1f',
            },
          }"
        />
      </template>
    </Chart>

    <h2 class="text-xl font-normal leading-normal">Durchschnittliche Punktzahlen pro Trumpf</h2>
    <Chart
      :size="{ width, height: 300 }"
      :data="contractAverages"
      :margin="margin"
      :direction="'vertical'"
      :axis="averageAxis"
    >
      <template #layers>
        <Grid />
        <Bar :data-keys="['contract', 'average']" :bar-style="{ fill: '#0096c7' }" />
      </template>

      <template #widgets>
        <Tooltip
          border-color="#48CAE4"
          :config="{
            contract: { label: 'Trumpf' },
            average: { color: '#0096c7', label: 'Mittelwert', format: ',.1f' },
          }"
        />
      </template>
    </Chart>
  </StatisticsContainer>
</template>
