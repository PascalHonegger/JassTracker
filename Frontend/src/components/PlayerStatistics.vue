<script setup lang="ts">
import { useStatisticsStore } from "@/store/statistics-store";
import { computed, onMounted, ref } from "vue";
import type { WebPlayerStatistics } from "@/services/web-model";
import { Bar, Area, Marker, Chart, Line, Grid, Tooltip } from "vue3-charts";
import StatisticsContainer from "@/components/StatisticsContainer.vue";
import { useContractStore } from "@/store/contract-store";
import { maxScore } from "@/util/constants";
import { useAuthStore } from "@/store/auth-store";
import { assertNonNullish } from "@/util/assert";
import type { AxisConfig } from "vue3-charts/src/types";

const statisticsStore = useStatisticsStore();
const contractStore = useContractStore();
const authStore = useAuthStore();
const playerStatistics = ref<WebPlayerStatistics>();
const isLoading = ref(false);

onMounted(async () => {
  await contractStore.loadContracts();
  await refresh();
});

async function refresh() {
  isLoading.value = true;
  try {
    assertNonNullish(authStore.playerId, "PlayerId Should be defined");
    playerStatistics.value = await statisticsStore.getPlayerStatistics(authStore.playerId);
  } finally {
    isLoading.value = false;
  }
}

const scoreDistribution = computed(() => {
  return playerStatistics.value === undefined
    ? []
    : playerStatistics.value.scoreDistribution
        .map((v) => ({ ...v, score: v.score.toString() }))
        .sort((a, b) => parseInt(a.score) - parseInt(b.score));
});

const contractAverages = computed(() => {
  return playerStatistics.value === undefined
    ? []
    : Object.entries(playerStatistics.value.contractAverages)
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
    domain: [0, maxScore],
    // domain: ["dataMin", "dataMax"],
    type: "band",
    format: (w) => {
      const number = parseInt(w, 10);
      if (number === 0 || number === maxScore) {
        return w;
      }
      if (w.toString().endsWith("0")) {
        return w;
      }
      return "";
    },
  },
  secondary: {
    domain: ["dataMin", "dataMax"],
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
    title="Persönliche Statistiken"
    :is-loading="isLoading"
    @refresh="refresh"
  >
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
        <Bar
          :dataKeys="['contract', 'average']"
          :barStyle="{ fill: '#0096c7', fillOpacity: 0.4 }"
        />
        <Marker
          v-if="playerStatistics"
          :value="playerStatistics.average"
          label="Mittelwert"
          color="black"
          :strokeWidth="2"
          strokeDasharray="6 6"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            contract: { label: 'Trumpf' },
            average: { color: '#0096c7', label: 'Mittelwert', format: ',.1f' },
          }"
        />
      </template>
    </Chart>
    <h2 class="text-xl font-normal leading-normal">Häufigkeit von Punktzahlen</h2>
    <Chart
      :size="{ width, height: 400 }"
      :data="scoreDistribution"
      :margin="margin"
      :direction="'horizontal'"
      :axis="scoreAxis"
    >
      <template #layers>
        <Grid :hide-y="true" />
        <Area :dataKeys="['score', 'height']" type="monotone" :areaStyle="{ fill: 'url(#grad)' }" />
        <Line
          :dataKeys="['score', 'height']"
          :lineStyle="{ stroke: '#0096c7' }"
          :type="'monotone'"
          :hide-dot="true"
        />

        <defs>
          <linearGradient id="grad" gradientTransform="rotate(90)">
            <stop offset="0%" stop-color="#0096c7" />
            <stop offset="100%" stop-color="white" />
          </linearGradient>
        </defs>
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            score: { color: 'black', label: 'Punkte' },
            occurrences: { color: 'blue', label: 'Häufigkeit' },
            height: {
              color: '#0096c7',
              label: 'Relative Häufigkeit',
              format: ',.1f',
            },
          }"
        />
      </template>
    </Chart>
  </StatisticsContainer>
</template>
