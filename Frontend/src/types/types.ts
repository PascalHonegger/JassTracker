import { WebRound } from "@/services/web-model";

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
  startTime: string;
  endTime?: string;
  rounds: WebRound[];
  rows: Row[];
  team1: Team;
  team2: Team;
}

export interface Team {
  player1: GameParticipant;
  player2: GameParticipant;
}

export interface GameParticipant {
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

export interface Contract {
  id: string;
  name: string;
}

export interface Player {
  id: string;
  displayName: string;
  isGuest: boolean;
  username: string;
  password: string;
}
