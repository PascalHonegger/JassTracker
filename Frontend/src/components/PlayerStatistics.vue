<script setup lang="ts">
import { useStatisticsStore } from "@/store/statistics-store";
import { computed, onMounted, ref } from "vue";
import { WebPlayerStatistics } from "@/services/web-model";
import { Bar, Chart, Line, Grid, Tooltip } from "vue3-charts";
import { ChartAxis } from "vue3-charts/dist/types";
import StatisticsContainer from "@/components/StatisticsContainer.vue";
import { useContractStore } from "@/store/contract-store";
import { maxGamePoints } from "@/util/constants";
import { useAuthStore } from "@/store/auth-store";
import { assertNonNullish } from "@/util/assert";

const statisticsStore = useStatisticsStore();
const contractStore = useContractStore();
const authStore = useAuthStore();
const playerStatistics = ref<WebPlayerStatistics | undefined>(undefined);
const isLoading = ref(false);

onMounted(async () => {
  await contractStore.loadContracts();
  await refresh();
});

async function refresh() {
  isLoading.value = true;
  try {
    assertNonNullish(authStore.playerId, "PlayerId Should be defined");
    playerStatistics.value = await statisticsStore.getPlayerStatistics(
      authStore.playerId
    );
  } finally {
    isLoading.value = false;
  }
}

const playerAverages = computed(() => {
  if (playerStatistics.value == undefined) {
    return [];
  }
  return [
    {
      label: authStore.displayName,
      average: playerStatistics.value.average,
    },
  ];
});

const scoreOverGames = computed(() => {
  if (playerStatistics.value == undefined) {
    return [];
  }
  const values = {
    "0": 0,
    [maxGamePoints]: 0,
    ...playerStatistics.value.scoreDistribution,
  };
  return Object.entries(values)
    .map(([points, occurrence]) => ({
      points: parseInt(points),
      occurrence: occurrence,
    }))
    .sort((a, b) => a.points - b.points);
});

const maxOccurrence = computed(() => {
  return playerStatistics.value == undefined
    ? 0
    : Math.max(...Object.values(playerStatistics.value.scoreDistribution));
});

const contractAverages = computed(() => {
  if (playerStatistics.value == undefined) {
    return [];
  }
  return Object.entries(playerStatistics.value.contractAverages)
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

const scoreAxis = computed<ChartAxis>(() => ({
  primary: {
    domain: [0, maxGamePoints],
    type: "linear",
  },
  secondary: {
    domain: [0, maxOccurrence.value],
    type: "linear",
  },
}));

const averageAxis = computed<ChartAxis>(() => ({
  primary: {
    domain: ["dataMin", "dataMax"],
    type: "band",
  },
  secondary: {
    domain: [0, maxGamePoints],
    type: "linear",
  },
}));
</script>

<template>
  <StatisticsContainer
    v-slot="{ width }"
    title="Spieler Statistiken"
    :is-loading="isLoading"
    @refresh="refresh"
  >
    <h2 class="text-xl font-normal leading-normal">
      Durchschnittliche Punktzahlen
    </h2>
    <Chart
      :size="{ width, height: 100 }"
      :data="playerAverages"
      :margin="margin"
      :direction="'vertical'"
      :axis="averageAxis"
    >
      <template #layers>
        <Grid />
        <Bar :dataKeys="['label', 'average']" :barStyle="{ fill: '#0096c7' }" />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            label: { hide: true },
            average: { color: '#0096c7', label: 'Mittelwert' },
          }"
        />
      </template>
    </Chart>

    <h2 class="text-xl font-normal leading-normal">
      Durchschnittliche Punktzahlen pro Trumpf
    </h2>
    <Chart
      :size="{ width, height: 300 }"
      :data="contractAverages"
      :margin="margin"
      :direction="'vertical'"
      :axis="averageAxis"
    >
      <template #layers>
        <Grid />
        <Bar
          :dataKeys="['contract', 'average']"
          :barStyle="{ fill: '#0096c7' }"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            contract: { label: 'Trumpf' },
            average: { color: '#0096c7', label: 'Mittelwert' },
          }"
        />
      </template>
    </Chart>
    <h2 class="text-xl font-normal leading-normal">
      Häufigkeit von Punktzahlen
    </h2>
    <Chart
      :size="{ width, height: 400 }"
      :data="scoreOverGames"
      :margin="margin"
      :direction="'horizontal'"
      :axis="scoreAxis"
    >
      <template #layers>
        <Grid :hide-y="true" />
        <Line
          :dataKeys="['points', 'occurrence']"
          :lineStyle="{ stroke: 'blue' }"
          :type="'monotone'"
          :hide-dot="true"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            points: { color: '#0096c7', label: 'Punkte' },
            occurrence: { color: 'blue', label: 'Häufigkeit' },
          }"
        />
      </template>
    </Chart>
  </StatisticsContainer>
</template>
