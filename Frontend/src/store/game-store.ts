import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";

import { useTableStore } from "@/store/table-store";
import { createGame, deleteGameById, getGame, updateGame } from "@/services/game-service";
import { useContractStore } from "@/store/contract-store";
import type { Game } from "@/types/types";
import type { WebCreateGame, WebGame } from "@/services/web-model";

export const useGameStore = defineStore("game", {
  getters: {
    currentGame(): Game | undefined {
      const tableStore = useTableStore();
      const table = tableStore.currentTable;
      return table?.loadedGames[table.currentGameId];
    },
    latestGame(): Game | undefined {
      const tableStore = useTableStore();
      const table = tableStore.currentTable;
      return table?.loadedGames[table.latestGameId];
    },
  },
  actions: {
    async loadGamesForTable(tableId: string) {
      const tableStore = useTableStore();
      await tableStore.loadTable(tableId);
      const table = tableStore.getTableById(tableId);
      assertNonNullish(table, `Got unexpected null for tableId ${tableId}`);
      for (const gameId of table.gameIds) {
        await this.loadGame(tableId, gameId);
      }
    },
    async loadGame(tableId: string, gameId: string) {
      const tableStore = useTableStore();
      await tableStore.loadTable(tableId);
      const table = tableStore.getTableById(tableId);
      assertNonNullish(table, `Got unexpected null for tableId ${tableId}`);
      if (table.loadedGames[gameId] == null) {
        const loadedGame = await getGame(gameId);
        this.addGameToExistingTable(tableId, loadedGame);
      }
    },
    async createGame(newGame: WebCreateGame): Promise<string> {
      const createdGame = await createGame(newGame);
      const gameStore = useGameStore();
      gameStore.addGameToExistingTable(newGame.tableId, createdGame);
      this.setLatestGame(newGame.tableId, createdGame.id);
      return createdGame.id;
    },
    setCurrentGame(tableId: string, gameId: string) {
      const tableStore = useTableStore();
      const table = tableStore.getTableById(tableId);
      assertNonNullish(table, `Got unexpected null for tableId ${tableId}`);
      table.currentGameId = gameId;
    },
    setLatestGame(tableId: string, gameId: string) {
      const tableStore = useTableStore();
      const table = tableStore.getTableById(tableId);
      assertNonNullish(table, `Got unexpected null for tableId ${tableId}`);
      table.latestGameId = gameId;
    },
    addGameToExistingTable(tableId: string, game: WebGame) {
      const tableStore = useTableStore();
      const contractStore = useContractStore();
      const table = tableStore.getTableById(tableId);
      assertNonNullish(table, `Got unexpected null for tableId ${tableId}`);
      const rows = contractStore.emptyRows([
        game.team1.player1,
        game.team1.player2,
        game.team2.player1,
        game.team2.player2,
      ]);

      const getTeamMember = (playerId: string) => {
        if (game.team1.player1.playerId === playerId) return game.team1.player2.playerId;
        if (game.team1.player2.playerId === playerId) return game.team1.player1.playerId;
        if (game.team2.player1.playerId === playerId) return game.team2.player2.playerId;
        if (game.team2.player2.playerId === playerId) return game.team2.player1.playerId;

        throw new Error("Couldn't match player to team, this should not happen");
      };

      for (const webRound of game.rounds) {
        for (const row of rows) {
          for (const round of row.rounds) {
            if (webRound.contractId === round.contractId && webRound.playerId === round.playerId) {
              round.type = "played";
              round.score = webRound.score;
              round.id = webRound.id;

              const teamMember = getTeamMember(round.playerId);
              const teamMemberRound = row.rounds.find((r) => r.playerId === teamMember);
              assertNonNullish(
                teamMemberRound,
                "Couldn't match player id to player, this should not happen",
              );
              teamMemberRound.type = "locked";
            }
          }
        }
      }

      const preparedGame: Game = {
        id: game.id,
        startTime: new Date(game.startTime),
        endTime: game.endTime != null ? new Date(game.endTime) : undefined,
        rounds: game.rounds,
        team1: game.team1,
        team2: game.team2,
        currentPlayer: game.currentPlayer,
        rows,
      };
      table.loadedGames[preparedGame.id] = preparedGame;
    },
    async removeGame(gameId: string) {
      // remove game object in memory
      const tableStore = useTableStore();
      const table = tableStore.currentTable;
      delete table?.loadedGames[gameId];
      const index: number | undefined = table?.gameIds.indexOf(gameId);
      if (index != null && index !== -1) {
        table?.gameIds.splice(index, 1);
      }
      try {
        await deleteGameById(gameId);
      } catch {
        return false;
      }
      return true;
    },
    async finishGame(gameId: string) {
      const tableStore = useTableStore();
      await this.loadGame(tableStore.currentTableId, gameId);
      const table = tableStore.currentTable;
      const game = table?.loadedGames[gameId];
      if (game === undefined) {
        throw new Error(`Game with ID ${gameId} not found`);
      }

      game.endTime = new Date();
      const webGame: WebGame = {
        id: gameId,
        rounds: [],
        team1: game.team1,
        team2: game.team2,
        startTime: game.startTime.toISOString(),
        endTime: game.endTime.toISOString(),
        // TODO: make seperate API type for updates
        currentPlayer: game.team1.player1,
      };
      await updateGame(gameId, webGame);
    },
  },
});
