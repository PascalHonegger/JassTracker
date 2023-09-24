import { mande } from "mande";
import type { OptionsRaw } from "mande";

const apiUrl = import.meta.env.PROD ? "/api/" : `//${window.location.hostname}:8080/api/`;

const globalOptions: OptionsRaw = {
  responseAs: "json",
};

export const api = mande(apiUrl, globalOptions);

export function setToken(token: string) {
  api.options.headers.Authorization = "Bearer " + token;
}

export function clearToken() {
  delete api.options.headers.Authorization;
}
