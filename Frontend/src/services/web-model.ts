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
