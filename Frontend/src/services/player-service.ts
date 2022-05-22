import { api } from "@/services/requests";

/*export async function updatePlayer(id: string, player: Player) {
  return api.put(`players/${id}`, player);
}*/

export async function updatePlayerDisplayName(id: string, displayName: string) {
  return api.put(`players/${id}/displayName`, displayName);
}

export async function deletePlayer(id: string) {
  return api.delete(`/players/${id}`);
}
