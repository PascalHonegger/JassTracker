import type { WebRound } from "@/services/web-model";

export interface Table {
  id: string;
  name: string;
  latestGameId: string;
  currentGameId: string;
  gameIds: string[];
  loadedGames: Record<string, Game>;
}

export interface Game {
  id: string;
  startTime: Date;
  endTime?: Date;
  rounds: WebRound[];
  rows: Row[];
  team1: Team;
  team2: Team;
  currentPlayer: GameParticipation;
}

export interface Team {
  player1: GameParticipation;
  player2: GameParticipation;
}

export interface GameParticipation {
  playerId: string;
  displayName: string;
}

export interface Row {
  contract: Contract;
  rounds: Round[];
}

export type Round = PlayedRound | OpenRound | LockedRound;

export type BaseRound = {
  id: string;
  number: number;
  playerId: string;
  contractId: string;
};

export type PlayedRound = BaseRound & {
  type: RoundType.Played;
  score: number;
};

export type LockedRound = BaseRound & {
  type: RoundType.Locked;
  score: number;
};

export type OpenRound = BaseRound & {
  type: RoundType.Open;
  score: null;
};

export enum RoundType {
  Played = "played",
  Open = "open",
  Locked = "locked",
}

export type ContractType =
  | "Acorns"
  | "Roses"
  | "Shields"
  | "Bells"
  | "TopsDown"
  | "BottomsUp"
  | "Joker"
  | "Slalom"
  | "Guschti";

export interface Contract {
  id: string;
  name: string;
  multiplier: number;
  type: ContractType;
}
