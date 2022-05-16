import { defineStore } from "pinia";

import { RoundType } from "@/types/types";
import { WebCreateRound, WebRound } from "@/services/web-model";
import {
  createRound,
  deleteRoundById,
  updateRound,
} from "@/services/round-service";
import { useGameStore } from "@/store/game-store";
import { getCurrentPlayerOfGame } from "@/services/game-service";

export const useRoundStore = defineStore("round", {
  actions: {
    async createRound(round: WebCreateRound) {
      const gameStore = useGameStore();
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      try {
        const newRound = await createRound(round);
        await this.handleRoundCreateOrUpdate(newRound);
      } catch (e) {
        alert("There was an error with creating round");
      }
    },
    async updateRound(round: WebRound) {
      const gameStore = useGameStore();
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      try {
        await updateRound(round.id, round);
        await this.handleRoundCreateOrUpdate(round);
      } catch (e) {
        alert("There was an error with updating round");
      }
    },
    async handleRoundCreateOrUpdate(round: WebRound) {
      const gameStore = useGameStore();
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      this.addRoundToCurrentGame(round);
      gameStore.currentGame.currentPlayer = await getCurrentPlayerOfGame(
        gameStore.currentGame.id
      );
    },
    addRoundToCurrentGame(round: WebRound) {
      const gameStore = useGameStore();
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      gameStore.currentGame.rounds.push(round);
      const teamPartnerIndex = this.findTeamPartnerIndex(round.playerId);
      if (teamPartnerIndex === -1) {
        alert("no team partner found, something's wrong I can feel it");
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
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      gameStore.currentGame.rounds = gameStore.currentGame.rounds.filter(
        (r) => r.id !== roundId
      );
      const teamPartnerIndex = this.findTeamPartnerIndex(playerId);
      if (teamPartnerIndex === -1) {
        alert("no team partner found, something's wrong I can feel it");
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
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return -1;
      }
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
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      this.removeRoundFromCurrentGame(roundId, playerId, contractId);
      try {
        await deleteRoundById(roundId);
        this.updateRoundNumbers(roundNumber);
        gameStore.currentGame.currentPlayer = await getCurrentPlayerOfGame(
          gameStore.currentGame.id
        );
      } catch (e) {
        return false;
      }
      return true;
    },
    updateRoundNumbers(roundNumber: number) {
      const gameStore = useGameStore();
      if (gameStore.currentGame === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
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
