import { defineStore } from "pinia";
import {
  getGameStatistics,
  getPlayerStatistics,
  getTableStatistics,
} from "@/services/statistics-service";
import { useToast } from "vue-toastification";

const toast = useToast();

export const useStatisticsStore = defineStore("statistics", {
  actions: {
    async getGameStatistics(gameId: string) {
      try {
        return await getGameStatistics(gameId);
      } catch {
        toast.error("Es gab ein Problem beim Laden der Spielstatistiken");
      }
    },
    async getPlayerStatistics(playerId: string) {
      try {
        return await getPlayerStatistics(playerId);
      } catch {
        toast.error("Es gab ein Problem beim Laden der Spielerstatistiken");
      }
    },
    async getTableStatistics(tableId: string) {
      try {
        return await getTableStatistics(tableId);
      } catch {
        toast.error("Es gab ein Problem beim Laden der Tischstatistiken");
      }
    },
  },
});
