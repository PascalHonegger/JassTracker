import { api } from "@/services/requests";
import { Player } from "@/types/types";

export interface WebGame {
  id: string;
  startTime: string;
  endTime?: string;
  rounds: WebRound[];
}

export interface WebRound {
  id: string;
  number: number;
  score: number;
  gameId: string;
  playerId: string;
  contractId: string;
}

export async function getGames(tableId: string) {
  return api.get<WebGame[]>(`games/${tableId}`);
}

export async function getGame(gameId: string) {
  return api.get<WebGame>(`games/${gameId}`);
}

export async function updateGame(id: string, game: WebGame) {
  return api.put(`games/${id}`, game);
}

export async function createGame(game: WebGame) {
  return api.post("games", game);
}

export async function deleteGame(id: string) {
  return api.delete(`games/${id}`);
}
