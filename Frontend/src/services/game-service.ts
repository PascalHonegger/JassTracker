import { api } from "@/services/requests";
import { WebCreateGame, WebGame } from "@/services/web-model";

export async function getGames(tableId: string) {
  return api.get<WebGame[]>(`games/${tableId}`);
}

export async function getGame(gameId: string) {
  return api.get<WebGame>(`games/${gameId}`);
}

export async function updateGame(id: string, game: WebGame) {
  return api.post(`games/${id}`, game);
}

export async function createGame(game: WebCreateGame) {
  return api.put<WebGame>("games", game);
}

export async function deleteGame(id: string) {
  return api.delete(`games/${id}`);
}
