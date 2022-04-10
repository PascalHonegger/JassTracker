import {api} from "@/services/requests";

export interface WebTable {
  id: string;
  name: string;
  ownerId: string;
  gameIds: string[];
}

export async function getTables() {
  return api.get<WebTable[]>("tables");
}

export async function getTable(id: string) {
  return api.get<WebTable>(`tables/${id}`);
}

export async function updateTable(id: string, table: WebTable) {
  return api.put(`tables/${id}`, table);
}

export async function createTable(table: WebTable) {
  return api.post("tables", table);
}

export async function deleteTable(id: string) {
  return api.delete(`tables/${id}`);
}
