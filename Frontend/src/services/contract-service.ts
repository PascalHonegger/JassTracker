import { api } from "@/services/requests";
import { WebContract } from "@/services/web-model";

export async function getContracts() {
  return api.get<WebContract[]>("contracts");
}
