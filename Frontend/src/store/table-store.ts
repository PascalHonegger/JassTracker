import { defineStore } from "pinia";
import { deleteTableById, createTable, getTable, getTables } from "@/services/table-service";

import type { Table } from "@/types/types";
import { useGameStore } from "@/store/game-store";
import type { WebTable } from "@/services/web-model";

export const useTableStore = defineStore("table", {
  state: () => ({
    tables: {} as Record<string, Table>,
    currentTableId: "",
    loadedAllTables: false,
  }),
  getters: {
    tablesAsArray(state) {
      return Object.values(state.tables);
    },
    isEmpty(state) {
      return Object.values(state.tables).length === 0;
    },
    hasTable(state) {
      return (tableId: string): boolean => state.tables[tableId] != null;
    },
    currentTable(state): Table | undefined {
      return state.tables[state.currentTableId];
    },
    getTableById: (state) => {
      return (tableId: string): Table | undefined => state.tables[tableId];
    },
  },
  actions: {
    async loadTables() {
      if (!this.loadedAllTables) {
        this.loadedAllTables = true;
        const loadedTables = await getTables();
        // create object with ID to allow O(1) access time
        for (const loadedTable of loadedTables) {
          this.addTable(loadedTable);
        }
      }
    },
    setCurrentTable(id: string) {
      this.currentTableId = id;
    },
    async loadTable(id: string) {
      if (!this.hasTable(id)) {
        const loadedTable = await getTable(id);
        this.addTable(loadedTable);
      }
    },
    async createTable(tableName: string): Promise<string> {
      const createdTable = await createTable(tableName);
      this.addTable(createdTable);
      return createdTable.id;
    },
    addTable(table: WebTable) {
      this.tables[table.id] = {
        id: table.id,
        name: table.name,
        gameIds: table.gameIds,
        loadedGames: {},
        latestGameId: table.latestGame?.id ?? "",
        currentGameId: table.latestGame?.endTime == null ? table.latestGame?.id ?? "" : "",
      };
      if (table.latestGame != null) {
        const gameStore = useGameStore();
        gameStore.addGameToExistingTable(table.id, table.latestGame);
      }
    },
    async removeTable(tableId: string) {
      // remove table object in memory
      delete this.tables[tableId];
      try {
        await deleteTableById(tableId);
      } catch (e) {
        return false;
      }
      return true;
    },
  },
});
