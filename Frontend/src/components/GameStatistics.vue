<script setup lang="ts">
import { Game } from "@/types/types";
import { useStatisticsStore } from "@/store/statistics-store";
import { computed, onMounted, ref, watch } from "vue";
import { WebGameStatistics } from "@/services/web-model";
import { Bar, Chart, Grid, Line, Marker, Tooltip } from "vue3-charts";
import { ChartAxis } from "vue3-charts/dist/types";
import StatisticsContainer from "@/components/StatisticsContainer.vue";
import { maxGamePoints } from "@/util/constants";

const statisticsStore = useStatisticsStore();

const props = defineProps<{ game: Game }>();
const gameStatistics = ref<WebGameStatistics>();
const isLoading = ref(false);

const axisPadding = 200;

onMounted(async () => await refresh());
watch(props.game, async () => await refresh());

async function refresh() {
  isLoading.value = true;
  try {
    gameStatistics.value = await statisticsStore.getGameStatistics(
      props.game.id
    );
  } finally {
    isLoading.value = false;
  }
}

const scoreOverRound = computed(
  () =>
    gameStatistics.value?.expectedScoresOverTime.map((scores, index) => ({
      round: index + 1,
      team1: scores.team1Score ?? undefined,
      team2: scores.team2Score ?? undefined,
      diff:
        scores.team1Score != null && scores.team2Score != null
          ? scores.team2Score - scores.team1Score
          : undefined,
    })) ?? []
);

const averages = computed(() => {
  return gameStatistics.value === undefined
    ? []
    : [
        {
          label: "Team 1",
          average: gameStatistics.value.team1Average,
          weightedAverage: gameStatistics.value.team1WeightedAverage,
        },
        {
          label: "Team 2",
          average: gameStatistics.value.team2Average,
          weightedAverage: gameStatistics.value.team2WeightedAverage,
        },
        ...gameStatistics.value.playerAverages.map((p) => ({
          label: p.displayName,
          average: p.average,
          weightedAverage: p.weightedAverage,
        })),
      ];
});

const scoreOverRoundBoundaries = computed<[number, number]>(() => {
  let min = Number.MAX_SAFE_INTEGER;
  let max = Number.MIN_SAFE_INTEGER;
  scoreOverRound.value.forEach(({ team1, team2 }) => {
    if (team1 != null && team1 < min) {
      min = team1;
    }
    if (team2 != null && team2 < min) {
      min = team2;
    }
    if (team1 != null && team1 > max) {
      max = team1;
    }
    if (team2 != null && team2 > max) {
      max = team2;
    }
  });
  return [min - axisPadding, max + axisPadding];
});

const margin = ref({
  left: 20,
  top: 20,
  right: 20,
  bottom: 0,
});
const scoreAxis = computed<ChartAxis>(() => ({
  primary: {
    domain: ["dataMin", "dataMax"],
    type: "band",
  },
  secondary: {
    domain: scoreOverRoundBoundaries.value,
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
    title="Spielverlauf"
    :is-loading="isLoading"
    @refresh="refresh"
  >
    <h2 class="text-xl font-normal leading-normal">
      Hochgerechnete Punktzahlen pro Runde (<span style="color: orange"
        >Team 1</span
      >
      vs <span style="color: blue">Team 2</span>)
    </h2>
    <Chart
      :size="{ width, height: 400 }"
      :data="scoreOverRound"
      :margin="margin"
      :direction="'horizontal'"
      :axis="scoreAxis"
    >
      <template #layers>
        <Grid :hide-y="true" />
        <Line
          :dataKeys="['round', 'team1']"
          :lineStyle="{ stroke: 'orange' }"
          :type="'monotone'"
          :hide-dot="true"
        />
        <Line
          :dataKeys="['round', 'team2']"
          :lineStyle="{ stroke: 'blue' }"
          :type="'monotone'"
          :hide-dot="true"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            team1: { color: 'orange', label: 'Team 1' },
            team2: { color: 'blue', label: 'Team 2' },
            round: { label: 'Runde' },
            diff: { hide: true },
          }"
        />
      </template>
    </Chart>

    <h2 class="text-xl font-normal leading-normal">
      Punktunterschied (Positive Werte sind besser für Team 1)
    </h2>
    <Chart
      :size="{ width, height: 300 }"
      :data="scoreOverRound"
      :margin="margin"
      :direction="'horizontal'"
    >
      <template #layers>
        <Grid :hide-y="true" />
        <Marker :value="0" :color="'gray'" :label="'Ausgeglichen'" />
        <Line
          :dataKeys="['round', 'diff']"
          :lineStyle="{ stroke: 'red' }"
          :type="'monotone'"
          :hide-dot="true"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
          :config="{
            team1: { color: 'orange', label: 'Team 1' },
            team2: { color: 'blue', label: 'Team 2' },
            round: { label: 'Runde' },
            diff: { color: 'red', label: 'Punktunterschied' },
          }"
        />
      </template>
    </Chart>

    <h2 class="text-xl font-normal leading-normal">
      Durchschnittliche Punktzahlen
    </h2>
    <Chart
      :size="{ width, height: 300 }"
      :data="averages"
      :margin="margin"
      :direction="'vertical'"
      :axis="averageAxis"
    >
      <template #layers>
        <Grid />
        <Bar :dataKeys="['label', 'average']" :barStyle="{ fill: '#0096c7' }" />
        <Bar
          :dataKeys="['label', 'weightedAverage']"
          :barStyle="{ fill: '#48cae4' }"
        />
      </template>

      <template #widgets>
        <Tooltip
          borderColor="#48CAE4"
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
  </StatisticsContainer>
</template>
