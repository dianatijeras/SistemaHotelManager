import { LoginCredentials, Usuario } from "../types/auth";
import { apiRequest } from "./api";

export const authService = {
    login: (credentials: LoginCredentials) =>
        apiRequest<Usuario>("/auth/login", {
            method: "POST",
            body: JSON.stringify(credentials),
        }),
};