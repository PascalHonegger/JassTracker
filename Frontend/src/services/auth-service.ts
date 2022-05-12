import { api } from "@/services/requests";

export async function loginPlayer(username: string, password: string) {
  return api.post<string>("login", { username, password });
}

export async function loginGuestPlayer() {
  return api.post<{ token: string }>("guestAccess");
}
