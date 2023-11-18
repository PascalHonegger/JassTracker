import { api } from "@/services/requests";
import type { WebContract } from "@/services/web-model";

export async function getContracts() {
  return api.get<WebContract[]>("contracts");
}
