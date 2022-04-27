import { api } from "@/services/requests";
import {
  WebCreateGame,
  WebGame,
  WebGameParticipant,
} from "@/services/web-model";

export async function getGames(tableId: string) {
  return api.get<WebGame[]>(`games/${tableId}`);
}

export async function getGame(gameId: string) {
  return api.get<WebGame>(`games/${gameId}`);
}

export async function getCurrentPlayerOfGame(gameId: string) {
  return api.get<WebGameParticipant>(`games/${gameId}/currentPlayer`);
}

export async function updateGame(id: string, game: WebGame) {
  return api.put(`games/${id}`, game);
}

export async function createGame(game: WebCreateGame) {
  return api.post<WebGame>("games", game);
}

export async function deleteGameById(id: string) {
  return api.delete(`games/${id}`);
}
