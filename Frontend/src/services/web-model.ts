// Keep in sync with WebModel.kt !!

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
}

export interface WebTeam {
  player1: WebGameParticipant;
  player2: WebGameParticipant;
}

export interface WebGameParticipant {
  playerId: string;
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

export interface WebContract {
  id: string;
  name: string;
  multiplier: number;
  type:
    | "Acorns"
    | "Roses"
    | "Shields"
    | "Bells"
    | "TopsDown"
    | "BottomsUp"
    | "Joker"
    | "Slalom"
    | "Guschti";
}
