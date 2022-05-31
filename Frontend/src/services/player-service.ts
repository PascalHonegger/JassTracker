import { api } from "@/services/requests";

export async function updatePlayerDisplayName(id: string, displayName: string) {
  return api.put<{ token: string }>(`players/${id}/displayName`, {
    displayName,
  });
}

export async function updatePlayerPassword(
  id: string,
  oldPassword: string,
  newPassword: string
) {
  return api.put<{ token: string }>(`players/${id}/password`, {
    oldPassword,
    newPassword,
  });
}

export async function deleteRegisteredPlayer(id: string) {
  return api.delete(`/players/${id}`);
}
