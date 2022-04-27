import { defineStore } from "pinia";

import { RoundType } from "@/types/types";
import { WebCreateRound, WebRound } from "@/services/web-model";
import { createRound, deleteRoundById } from "@/services/round-service";
import { storeToRefs } from "pinia";
import { useGameStore } from "@/store/game-store";
import { getCurrentPlayerOfGame } from "@/services/game-service";

const gameStore = useGameStore();
const { currentGame } = storeToRefs(gameStore);

export const useRoundStore = defineStore("round", {
  actions: {
    async createRound(newRound: WebCreateRound) {
      if (currentGame.value === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      try {
        const createdRound = await createRound(newRound);
        this.addRoundToCurrentGame(createdRound);
        currentGame.value.currentPlayer = await getCurrentPlayerOfGame(
          currentGame.value.id
        );
      } catch (e) {
        alert("There was an error with creating round");
      }
    },
    addRoundToCurrentGame(round: WebRound) {
      if (currentGame.value === undefined) {
        alert("currentGame should not be undefined");
        return;
      }
      currentGame.value.rounds.push(round);
      const teamPartnerIndex = this.findTeamPartnerIndex(round.playerId);
      if (teamPartnerIndex === -1) {
        alert("no team partner found, something's wrong I can feel it");
        return;
      }

      currentGame.value.rows.forEach((row) => {
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
      if (currentGame.value === undefined) {
        alert("currentGame should not be undefined");
        return -1;
      }
      switch (id) {
        case currentGame.value.team1.player1.playerId:
          return 1;
        case currentGame.value.team1.player2.playerId:
          return 0;
        case currentGame.value.team2.player1.playerId:
          return 3;
        case currentGame.value.team2.player2.playerId:
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
