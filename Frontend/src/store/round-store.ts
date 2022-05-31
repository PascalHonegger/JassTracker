import { defineStore } from "pinia";
import { assertNonNullish } from "@/util/assert";
import { RoundType } from "@/types/types";
import { WebCreateRound, WebRound } from "@/services/web-model";
import {
  createRound,
  deleteRoundById,
  updateRound,
} from "@/services/round-service";
import { useGameStore } from "@/store/game-store";
import { getCurrentPlayerOfGame } from "@/services/game-service";
import { useToast } from "vue-toastification";
const toast = useToast();

export const useRoundStore = defineStore("round", {
  actions: {
    async createRound(round: WebCreateRound) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
      try {
        const newRound = await createRound(round);
        await this.handleRoundCreateOrUpdate(newRound);
      } catch (e) {
        toast.error("Es gab ein Problem mit der Erstellung der Runde");
      }
    },
    async updateRound(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
      try {
        await updateRound(round.id, round);
        this.removeRoundFromCurrentGame(
          round.id,
          round.playerId,
          round.contractId
        );
        await this.handleRoundCreateOrUpdate(round);
      } catch (e) {
        toast.error("Es gab ein Problem mit der Aktualisierung der Runde");
      }
    },
    async handleRoundCreateOrUpdate(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
      this.addRoundToCurrentGame(round);
      gameStore.currentGame.currentPlayer = await getCurrentPlayerOfGame(
        gameStore.currentGame.id
      );
    },
    addRoundToCurrentGame(round: WebRound) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
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
              r.type = RoundType.Locked;
            }
            if (r.playerId === round.playerId) {
              r.id = round.id;
              r.score = round.score;
              r.type = RoundType.Played;
              r.number = round.number;
            }
          });
        }
      });
    },
    removeRoundFromCurrentGame(
      roundId: string,
      playerId: string,
      contractId: string
    ) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
      gameStore.currentGame.rounds = gameStore.currentGame.rounds.filter(
        (r) => r.id !== roundId
      );
      const teamPartnerIndex = this.findTeamPartnerIndex(playerId);
      if (teamPartnerIndex === -1) {
        toast.error("Es wurde kein Team Partner gefunden");
        return;
      }
      gameStore.currentGame.rows.forEach((row) => {
        if (row.contract.id === contractId) {
          row.rounds.forEach((r, i) => {
            if (i === teamPartnerIndex) {
              r.type = RoundType.Open;
            }
            if (r.playerId === playerId) {
              r.id = "";
              r.score = null;
              r.type = RoundType.Open;
            }
          });
        }
      });
    },
    findTeamPartnerIndex(id: string): number {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
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
    async removeRound(
      roundId: string,
      playerId: string,
      contractId: string,
      roundNumber: number
    ) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
      this.removeRoundFromCurrentGame(roundId, playerId, contractId);
      try {
        await deleteRoundById(roundId);
        this.updateRoundNumbers(roundNumber);
        gameStore.currentGame.currentPlayer = await getCurrentPlayerOfGame(
          gameStore.currentGame.id
        );
      } catch (e) {
        toast.error("Es gab ein Problem bei der Löschung der Runde");
        return false;
      }
      return true;
    },
    updateRoundNumbers(roundNumber: number) {
      const gameStore = useGameStore();
      assertNonNullish(
        gameStore.currentGame,
        "currentGame should not be undefined"
      );
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
