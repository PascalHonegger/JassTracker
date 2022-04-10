import { defineStore } from "pinia";
import { getContracts, WebContract } from "@/services/contract-service";
import { Player, RoundType, Row } from "@/types/types";

export const useContractStore = defineStore("contract", {
  state: () => ({
    loading: false,
    contracts: [] as Array<WebContract>,
  }),
  getters: {
    hasContracts(state) {
      return state.contracts.length > 0;
    },
    emptyRows(state) {
      return (players: Player[]): Row[] =>
        state.contracts.map((contract) => {
          return {
            contract,
            rounds: players.map((player, index) => ({
              id: "",
              type: RoundType.Open,
              number: index,
              score: null,
              playerId: player.id,
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
