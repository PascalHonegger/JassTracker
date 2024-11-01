import { defineStore } from "pinia";
import { useContractStore } from "@/store/contract-store";
import { useGameStore } from "@/store/game-store";
import { useTableStore } from "@/store/table-store";
import { useRoundStore } from "@/store/round-store";
import { loginGuestPlayer, loginPlayer, registerPlayer } from "@/services/auth-service";
import { clearToken, setToken } from "@/services/requests";
import { usePlayerStore } from "@/store/player-store";

interface JwtToken {
  // JWT Default
  aud: string;
  iss: string;
  exp: number;
  iat: number;
  // Our claims
  isGuest: boolean;
  playerId: string;
  username: string;
  displayName: string;
}

const tokenKey = "JassTrackerToken";

/**
 * Parses a JWT, supports unicode
 * @see https://stackoverflow.com/a/38552302
 * @param token
 */
function parseJwt(token: string): JwtToken | null {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join(""),
    );

    return JSON.parse(jsonPayload);
  } catch {
    return null;
  }
}

export const useAuthStore = defineStore("auth", {
  state: () => ({
    token: "",
  }),
  getters: {
    playerId(state): string | null {
      return parseJwt(state.token)?.playerId ?? null;
    },
    username(state): string | null {
      return parseJwt(state.token)?.username ?? null;
    },
    displayName(state): string | null {
      return parseJwt(state.token)?.displayName ?? null;
    },
    isGuest(state): boolean | undefined {
      return parseJwt(state.token)?.isGuest;
    },
    loggedIn(state): boolean {
      return parseJwt(state.token) != null;
    },
  },
  actions: {
    async loginPlayer(username: string, password: string): Promise<boolean> {
      try {
        const { token } = await loginPlayer(username, password);
        this.setToken(token);
        await this.loadContracts();

        return true;
      } catch {
        this.setToken("");
        return false;
      }
    },
    async registerPlayer(
      username: string,
      displayName: string,
      password: string,
    ): Promise<boolean> {
      try {
        const { token } = await registerPlayer(username, displayName, password);
        this.setToken(token);
        await this.loadContracts();
        return true;
      } catch {
        this.setToken("");
        return false;
      }
    },
    async guestAccess() {
      const { token } = await loginGuestPlayer();
      this.setToken(token);
      await this.loadContracts();
    },
    async loadContracts() {
      // Load all available contracts once after login
      // These shouldn't change and loading them
      // For each game seems excessive‡
      const contractStore = useContractStore();
      await contractStore.loadContracts();
    },
    setToken(token: string) {
      this.token = token;
      if (token) {
        setToken(token);
        localStorage.setItem(tokenKey, token);
      } else {
        clearToken();
        localStorage.removeItem(tokenKey);
      }
    },
    async loadTokenFromStorage() {
      const token = localStorage.getItem(tokenKey) ?? "";
      const jwt = parseJwt(token);
      const isValid = jwt != null && jwt.exp * 1000 >= new Date().getTime();
      if (isValid) {
        this.setToken(token);
        await this.loadContracts();
      }
      return isValid;
    },
    logout() {
      this.setToken("");
      // Reset all stores to prevent any weird behavior on logout / login
      useContractStore().$reset();
      useGameStore().$reset();
      useTableStore().$reset();
      useRoundStore().$reset();
      usePlayerStore().$reset();
    },
  },
});
