import { defineStore } from "pinia";

import { RoundType } from "@/types/types";
import { WebCreateRound, WebRound } from "@/services/web-model";
import { createRound, deleteRoundById } from "@/services/round-service";

import { useGameStore } from "@/store/game-store";
const gameStore = useGameStore();

export const useRoundStore = defineStore("round", {
  actions: {
    async createRound(newRound: WebCreateRound) {
      try {
        const createdRound = await createRound(newRound);
        this.addRoundToCurrentGame(createdRound);
      } catch (e) {
        console.error("There was an error with creating round", e);
      }
    },
    addRoundToCurrentGame(round: WebRound) {
      const currentGame = gameStore.currentGame;
      if (currentGame === undefined) {
        console.error("currentGame should not be undefined");
        return;
      }
      currentGame.rounds.push(round);
      const teamPartnerIndex = this.findTeamPartnerIndex(round.playerId);
      if (teamPartnerIndex === -1) {
        console.error("no team partner found, something's wrong I can feel it");
        return;
      }

      currentGame.rows.forEach((row) => {
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
    findTeamPartnerIndex(id: string): number {
      const currentGame = gameStore.currentGame;
      if (currentGame === undefined) {
        console.error("currentGame should not be undefined");
        return -1;
      }
      switch (id) {
        case currentGame.team1.player1.playerId:
          return 1;
        case currentGame.team1.player2.playerId:
          return 0;
        case currentGame.team2.player1.playerId:
          return 3;
        case currentGame.team2.player2.playerId:
          return 2;
        default:
          return -1;
      }
    },
    async removeRound(roundId: string) {
      // TODO remove from state store,
      try {
        await deleteRoundById(roundId);
      } catch (e) {
        return false;
      }
      return true;
    },
  },
});
