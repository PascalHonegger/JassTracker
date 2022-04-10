import { api } from "@/services/requests";
import { Player } from "@/types/types";

export async function getPlayers(tableId: string) {
  return api.get<Player[]>(`players/byTable/${tableId}`);
}

export async function getPlayer(id: string) {
  return api.get<Player>(`players/${id}`);
}
