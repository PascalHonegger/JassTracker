import { api } from "@/services/requests";
import {Player} from "@/types/types";

export async function createLoginRequest(username: String, password: String){
    return api.post<Player>("login", {username, password});
}