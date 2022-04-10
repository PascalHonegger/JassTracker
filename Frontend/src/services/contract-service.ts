import { api } from "@/services/requests";

export interface WebContract {
  id: string;
  name: string;
  multiplier: number;
  type:
    | "Acorns"
    | "Roses"
    | "Shields"
    | "Bells"
    | "TopsDown"
    | "BottomsUp"
    | "Joker"
    | "Slalom"
    | "Guschti";
}

export async function getContracts() {
  return api.get<WebContract[]>("contracts");
}
