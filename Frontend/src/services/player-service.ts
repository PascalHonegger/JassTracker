import { api } from "@/services/requests";
import { Player } from "@/types/types";

export async function getPlayer(id: string) {
  return api.get<Player>(`players/${id}`);
}

/*export async function updatePlayer(id: string, player: Player) {
  return api.put(`players/${id}`, player);
}*/

export async function updatePlayerDisplayName(id: string, displayName: string) {
  return api.put(`players/${id}`, displayName);
}

export async function deletePlayer(id: string) {
  return api.delete(`/players/${id}`);
}
