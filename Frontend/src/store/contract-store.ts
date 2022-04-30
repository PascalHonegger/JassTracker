import { defineStore } from "pinia";
import { getContracts } from "@/services/contract-service";
import { RoundType, Row } from "@/types/types";
import { WebContract, WebGameParticipation } from "@/services/web-model";

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
            rounds: players.map(({ playerId }, index) => ({
              id: "",
              type: RoundType.Open,
              number: index,
              score: null,
              playerId,
              contractId: contract.id,
            })),
          };
        });
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
