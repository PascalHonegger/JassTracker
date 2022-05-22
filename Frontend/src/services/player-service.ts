import { api } from "@/services/requests";

export async function deletePlayer(id: string) {
  return api.delete(`/players/${id}`);
}
