import { mande, OptionsRaw } from "mande";

const apiUrl =
  process.env.NODE_ENV === "production"
    ? "/api/"
    : `//${window.location.hostname}:8080/api/`;

const globalOptions: OptionsRaw = {
  responseAs: "json",
};

export const api = mande(apiUrl, globalOptions);
