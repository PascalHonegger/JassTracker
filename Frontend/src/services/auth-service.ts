import { api } from "@/services/requests";

export async function loginPlayer(username: String, password: String){
    return api.post<string>("login", {username, password});
}

export async function loginGuestPlayer(){
    return api.post<string>("guestAccess");
}