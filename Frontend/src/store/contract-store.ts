import { defineStore } from "pinia";
import { getContracts } from "@/services/contract-service";
import { Round, RoundType, Row } from "@/types/types";
import {
  WebContract,
  WebGameParticipation,
  WebRound,
} from "@/services/web-model";

export const useContractStore = defineStore("contract", {
  state: () => ({
    loading: false,
    contracts: [] as WebContract[],
  }),
  getters: {
    hasContracts(state) {
      return state.contracts.length > 0;
    },
    emptyRows(state) {
      return (players: WebGameParticipation[]): Row[] =>
        state.contracts.map((contract) => {
          return {
            contract,
            rounds: players.map(({ playerId }) => ({
              id: "",
              type: RoundType.Open,
              number: 0,
              score: null,
              playerId,
              contractId: contract.id,
            })),
          };
        });
    },
    getCalculatedScore(state) {
      return (round: WebRound | Round) => {
        const contract = state.contracts.find((c) => c.id === round.contractId);
        if (contract === undefined) {
          throw new Error(`Contract with ID ${round.contractId} not found`);
        }
        return contract.multiplier * (round.score ?? 0);
      };
    },
  },
  actions: {
    async loadContracts() {
      if (!this.hasContracts) {
        this.loading = true;
        try {
          this.contracts = await getContracts();
        } finally {
          this.loading = false;
        }
      }
    },
  },
});
