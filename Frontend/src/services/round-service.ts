import { api } from "@/services/requests";
import type { WebCreateRound, WebRound } from "@/services/web-model";

export async function updateRound(id: string, round: WebRound) {
  return api.put<WebRound>(`rounds/${id}`, round);
}

export async function createRound(round: WebCreateRound) {
  return api.post<WebRound>("rounds", round);
}

export async function deleteRoundById(id: string) {
  return api.delete(`rounds/${id}`);
}
