import { defineStore } from "pinia";
import { getContracts } from "@/services/contract-service";
import type { Contract, Round, Row } from "@/types/types";
import type { WebContract, WebGameParticipation, WebRound } from "@/services/web-model";
import { assertNonNullish } from "@/util/assert";

export const useContractStore = defineStore("contract", {
  state: () => ({
    loading: false,
    contracts: [] as WebContract[],
  }),
  getters: {
    hasContracts(state) {
      return state.contracts.length > 0;
    },
    getContract(state) {
      return (contractId: string): Contract => {
        const contract = state.contracts.find((c) => c.id === contractId);
        assertNonNullish(contract, `Couldn't find contract by id ${contractId}`);
        return contract;
      };
    },
    emptyRows(state) {
      return (players: WebGameParticipation[]): Row[] =>
        state.contracts.map((contract) => {
          return {
            contract,
            rounds: players.map(({ playerId }) => ({
              id: "",
              type: "open",
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
        assertNonNullish(contract, `Contract with ID ${round.contractId} not found`);
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
