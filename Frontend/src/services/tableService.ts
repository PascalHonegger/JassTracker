import { api } from "@/services/requests";

type Table = {
  id: string;
  // TODO
};

export async function getTables() {
  return api.get<Table[]>("tables");
}

export async function getTable(id: string) {
  return api.get<Table>(`tables/${id}`);
}

export async function updateTable(id: string, table: Table) {
  return api.put(`tables/${id}`, table);
}

export async function createTable(table: Table) {
  return api.post("tables", table);
}

export async function deleteTable(id: string) {
  return api.delete(`tables/${id}`);
}
