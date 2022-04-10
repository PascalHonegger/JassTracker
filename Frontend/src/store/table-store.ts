import { defineStore } from "pinia";
import { getTable, getTables } from "@/services/table-service";

import { Table } from "@/types/types";
import { getPlayers } from "@/services/player-service";

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
          // not map cause creates new array, but we want object soo
          for (const webTable of loadedTables) {
            this.tables[webTable.id] = {
              ...webTable,
              loadedGames: [],
              players: await getPlayers(webTable.id),
            } as Table;
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
          const webTable = await getTable(id);
          this.tables[id] = this.tables[webTable.id] = {
            ...webTable,
            loadedGames: [],
            players: await getPlayers(webTable.id),
          } as Table;
        } finally {
          this.loadingCounter--;
        }
      }
    },
  },
});
