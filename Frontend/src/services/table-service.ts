import { api } from "@/services/requests";
import { WebTable } from "@/services/web-model";

export async function getTables() {
  return api.get<WebTable[]>("tables");
}

export async function getTable(id: string) {
  return api.get<WebTable>(`tables/${id}`);
}

export async function updateTable(id: string, table: WebTable) {
  return api.post(`tables/${id}`, table);
}

export async function createTable(name: string) {
  return api.put<WebTable>("tables", { name });
}

export async function deleteTable(id: string) {
  return api.delete(`tables/${id}`);
}
