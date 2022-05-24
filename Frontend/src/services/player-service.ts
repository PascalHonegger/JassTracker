import { api } from "@/services/requests";

export async function updatePlayerDisplayName(id: string, displayName: string) {
  return api.put<{ token: string }>(`players/${id}/displayName`, {
    displayName,
  });
}

export async function deletePlayer(id: string) {
  return api.delete(`/players/${id}`);
}
