import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";
import type { WebCreateRound, WebRound } from "@/services/web-model";
import { createRound, deleteRoundById, updateRound } from "@/services/round-service";
import { useGameStore } from "@/store/game-store";
import { getCurrentPlayerOfGame } from "@/services/game-service";
import { useToast } from "vue-toastification";
const toast = useToast();

export const useRoundStore = defineStore("round", {
  actions: {
    async createRound(round: WebCreateRound) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      try {
        const newRound = await createRound(round);
        await this.handleRoundCreateOrUpdate(newRound);
      } catch (error) {
        toast.error("Es gab ein Problem mit der Erstellung der Runde");
        console.error("Error creating round:", error);
      }
    },
    async updateRound(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      try {
        await updateRound(round.id, round);
        this.removeRoundFromCurrentGame(round.id, round.playerId, round.contractId);
        await this.handleRoundCreateOrUpdate(round);
      } catch (error) {
        toast.error("Es gab ein Problem mit der Aktualisierung der Runde");
        console.error("Error updating round:", error);
      }
    },
    async handleRoundCreateOrUpdate(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      this.addRoundToCurrentGame(round);
      
      try {
        const nextPlayer = await getCurrentPlayerOfGame(gameStore.currentGame.id);
        // Force reactivity by reassigning the entire object
        if (gameStore.currentGame && nextPlayer) {
          gameStore.currentGame.currentPlayer = { ...nextPlayer };
        }
      } catch (error) {
        console.error("Error fetching current player:", error);
        toast.error("Es gab ein Problem bei der Aktualisierung des aktuellen Spielers");
      }
    },
    addRoundToCurrentGame(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      gameStore.currentGame.rounds.push(round);
      const teamPartnerIndex = this.findTeamPartnerIndex(round.playerId);
      if (teamPartnerIndex === -1) {
        toast.error("Es wurde kein Team Partner gefunden");
        return;
      }

      gameStore.currentGame.rows.forEach((row) => {
        if (row.contract.id === round.contractId) {
          row.rounds.forEach((r, i) => {
            if (i === teamPartnerIndex) {
              r.type = "locked";
            }
            if (r.playerId === round.playerId) {
              r.id = round.id;
              r.score = round.score;
              r.type = "played";
              r.number = round.number;
            }
          });
        }
      });
    },
    removeRoundFromCurrentGame(roundId: string, playerId: string, contractId: string) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      gameStore.currentGame.rounds = gameStore.currentGame.rounds.filter((r) => r.id !== roundId);
      const teamPartnerIndex = this.findTeamPartnerIndex(playerId);
      if (teamPartnerIndex === -1) {
        toast.error("Es wurde kein Team Partner gefunden");
        return;
      }
      gameStore.currentGame.rows.forEach((row) => {
        if (row.contract.id === contractId) {
          row.rounds.forEach((r, i) => {
            if (i === teamPartnerIndex) {
              r.type = "open";
            }
            if (r.playerId === playerId) {
              r.id = "";
              r.score = null;
              r.type = "open";
            }
          });
        }
      });
    },
    findTeamPartnerIndex(id: string): number {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      switch (id) {
        case gameStore.currentGame.team1.player1.playerId:
          return 1;
        case gameStore.currentGame.team1.player2.playerId:
          return 0;
        case gameStore.currentGame.team2.player1.playerId:
          return 3;
        case gameStore.currentGame.team2.player2.playerId:
          return 2;
        default:
          return -1;
      }
    },
    async removeRound(roundId: string, playerId: string, contractId: string, roundNumber: number) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      this.removeRoundFromCurrentGame(roundId, playerId, contractId);
      try {
        await deleteRoundById(roundId);
        this.updateRoundNumbers(roundNumber);
        
        const nextPlayer = await getCurrentPlayerOfGame(gameStore.currentGame.id);
        // Force reactivity by reassigning the entire object
        if (gameStore.currentGame && nextPlayer) {
          gameStore.currentGame.currentPlayer = { ...nextPlayer };
        }
      } catch (error) {
        toast.error("Es gab ein Problem bei der Löschung der Runde");
        console.error("Error removing round:", error);
        return false;
      }
      return true;
    },
    updateRoundNumbers(roundNumber: number) {
      const gameStore = useGameStore();
      assertNonNullish(gameStore.currentGame, "currentGame should not be undefined");
      gameStore.currentGame.rounds.forEach((round) => {
        if (round.number > roundNumber) {
          round.number -= 1;
        }
      });
      gameStore.currentGame.rows.forEach((row) => {
        row.rounds.forEach((rowRound) => {
          if (rowRound.number > roundNumber) {
            rowRound.number -= 1;
          }
        });
      });
    },
  },
});
