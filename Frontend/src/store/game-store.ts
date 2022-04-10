import { defineStore } from "pinia";

import { useTableStore } from "@/store/table-store";
import { getGame } from "@/services/game-service";
import { useContractStore } from "@/store/contract-store";
import { Game, RoundType } from "@/types/types";

export const useGameStore = defineStore("game", {
  state: () => ({
    loadingCounter: 0,
    currentGameId: "",
  }),
  getters: {
    loading(state) {
      return state.loadingCounter > 0;
    },
    currentGame(state): Game | undefined {
      const tableStore = useTableStore();
      return tableStore.currentTable?.loadedGames.find(
        (g) => g.id === state.currentGameId
      );
    },
  },
  actions: {
    async loadGamesForTable(tableId: string) {
      const tableStore = useTableStore();
      const contractStore = useContractStore();
      await tableStore.loadTable(tableId);
      const table = tableStore.getTableById(tableId);
      if (table == null) {
        console.error(`Got unexpected null for tableId ${tableId}`);
        return;
      }
      await contractStore.loadContracts();
      for (const gameId of table.gameIds) {
        await this.loadGame(tableId, gameId);
      }

      if (table.loadedGames.length > 0) {
        this.setCurrentGame(table.loadedGames[0].id);
      }
    },
    setCurrentGame(id: string) {
      this.currentGameId = id;
    },
    async loadGame(tableId: string, gameId: string) {
      const tableStore = useTableStore();
      const contractStore = useContractStore();
      await tableStore.loadTable(tableId);
      const table = tableStore.getTableById(tableId);
      if (table == null) {
        console.error(`Got unexpected null for tableId ${tableId}`);
        return;
      }
      if (table.loadedGames.every((game) => game.id !== gameId)) {
        this.loadingCounter++;
        try {
          const loadedGame = await getGame(gameId);
          const rows = contractStore.emptyRows(table.players);

          for (const webRound of loadedGame.rounds) {
            for (const row of rows) {
              for (const round of row.rounds) {
                if (
                  webRound.contractId === round.contractId &&
                  webRound.playerId === round.playerId
                ) {
                  round.type = RoundType.Played;
                  round.score = webRound.score;
                  round.id = webRound.id;

                  // TODO mark round of other team member as locked
                }
              }
            }
          }

          const preparedGame: Game = {
            ...loadedGame,
            rows,
          };
          table.loadedGames.push(preparedGame);
        } finally {
          this.loadingCounter--;
        }
      }
    },
  },
});
