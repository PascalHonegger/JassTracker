import { defineStore } from "pinia";
import { getTable, getTables } from "@/services/table-service";

import { Table } from "@/types/types";
import { useGameStore } from "@/store/game-store";
import { WebTable } from "@/services/web-model";

export const useTableStore = defineStore("table", {
  state: () => ({
    loadingCounter: 0,
    tables: {} as Record<string, Table>,
    currentTableId: "",
  }),
  getters: {
    loading(state) {
      return state.loadingCounter > 0;
    },
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
      if (this.isEmpty) {
        this.loadingCounter++;
        try {
          const loadedTables = await getTables();
          // create object with ID to allow O(1) access time
          for (const loadedTable of loadedTables) {
            this.addTable(loadedTable);
          }
        } finally {
          this.loadingCounter--;
        }
      }
    },
    setCurrentTable(id: string) {
      this.currentTableId = id;
    },
    async loadTable(id: string) {
      if (!this.hasTable(id)) {
        this.loadingCounter++;
        try {
          const loadedTable = await getTable(id);
          this.addTable(loadedTable);
        } finally {
          this.loadingCounter--;
        }
      }
    },
    addTable(table: WebTable) {
      this.tables[table.id] = this.tables[table.id] = {
        id: table.id,
        name: table.name,
        gameIds: table.gameIds,
        loadedGames: {},
        latestGameId: table.latestGame?.id ?? "",
        currentGameId: table.latestGame?.id ?? "",
      } as Table;

      if (table.latestGame != null) {
        const gameStore = useGameStore();
        gameStore.addGameToExistingTable(table.id, table.latestGame);
      }
    },
  },
});
