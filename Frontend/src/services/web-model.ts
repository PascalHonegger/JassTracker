// Keep in sync with WebModel.kt !!

import type { ContractType } from "@/types/types";

export interface WebTable {
  id: string;
  name: string;
  ownerId: string;
  gameIds: string[];
  latestGame: WebGame | undefined;
}

export interface WebGame {
  id: string;
  startTime: string;
  endTime?: string;
  rounds: WebRound[];
  team1: WebTeam;
  team2: WebTeam;
  currentPlayer: WebGameParticipation;
}

export interface WebTeam {
  player1: WebGameParticipation;
  player2: WebGameParticipation;
}

export interface WebGameParticipation {
  playerId: string;
  displayName: string;
}

export interface WebCreateGame {
  tableId: string;
  team1Player1: WebCreateGameParticipation;
  team1Player2: WebCreateGameParticipation;
  team2Player1: WebCreateGameParticipation;
  team2Player2: WebCreateGameParticipation;
}

export interface WebCreateGameParticipation {
  playerId: string | null;
  displayName: string;
}

export interface WebRound {
  id: string;
  number: number;
  score: number;
  gameId: string;
  playerId: string;
  contractId: string;
}

export interface WebCreateRound {
  number: number;
  score: number;
  gameId: string;
  playerId: string;
  contractId: string;
}

export interface WebContract {
  id: string;
  name: string;
  multiplier: number;
  type: ContractType;
}

export interface WebTeamScores {
  team1Score: number | null;
  team2Score: number | null;
}

export interface WebGameScoreSummary {
  gameId: string;
  total: WebTeamScores;
}

export interface WebPlayerAverage {
  playerId: string;
  displayName: string;
  average: number;
  weightedAverage: number;
}

export interface WebGameStatistics {
  playerAverages: Array<WebPlayerAverage>;
  team1Average: number;
  team2Average: number;
  team1WeightedAverage: number;
  team2WeightedAverage: number;
  expectedScoresOverTime: Array<WebTeamScores>;
}

export interface WebTableStatistics {
  playerAverages: Array<WebPlayerAverage>;
  contractAverages: Record<string, number>;
  scoresOverTime: Array<WebGameScoreSummary>;
}

export interface WebPlayerStatistics {
  average: number;
  total: number;
  contractAverages: Record<string, number>;
  scoreDistribution: Record<string, number>;
}
