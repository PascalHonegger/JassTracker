import { api } from "@/services/requests";
import type {
  WebGameStatistics,
  WebPlayerStatistics,
  WebTableStatistics,
} from "@/services/web-model";

export async function getGameStatistics(gameId: string) {
  return api.get<WebGameStatistics>(`statistics/game/${gameId}`);
}

export async function getPlayerStatistics(playerId: string) {
  return api.get<WebPlayerStatistics>(`statistics/player/${playerId}`);
}

export async function getTableStatistics(tableId: string) {
  return api.get<WebTableStatistics>(`statistics/table/${tableId}`);
}
