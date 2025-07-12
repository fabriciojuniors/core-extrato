import { useState, useEffect } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { LoginRequest } from "../types/login-request";
import { jwtDecode } from "jwt-decode";

const AUTH_STORAGE_KEY = "@auth_session";
const KEYCLOAK_URL = process.env.EXPO_PUBLIC_KEYCLOAK_LOGIN_URL!;

interface Session {
    access_token: string;
    refresh_token: string;
    expires_in: number;
    obtained_at: number;
}

export interface User {
    email: string;
    given_name: string;
    family_name: string;    
}

export const useKeycloak = () => {
    const [isSignedIn, setIsSignedIn] = useState(false);
    const [user, setUser] = useState<User | null>(null);
    const [session, setSession] = useState<Session | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        loadAuthState();
    }, [])

    useEffect(() => {
        if (!session || !isSignedIn) return;

        const interval = setInterval(() => {
            const now = Date.now();
            const obtainedAt = session.obtained_at || now;
            const expiresIn = (session.expires_in || 300) * 1000;

            if (now - obtainedAt >= expiresIn - 60000) {
                refreshToken();
            }
        }, 60000)

        return () => clearInterval(interval);
    }, [session, isSignedIn]);


    const loadAuthState = async () => {
        try {
            const authData = await AsyncStorage.getItem(AUTH_STORAGE_KEY);
            if (authData) {
                const { user: savedUser, session: savedSession } = JSON.parse(authData);
                setUser(savedUser);
                setSession(savedSession);
                setIsSignedIn(true);
            }
        } catch (error) {
            console.error("Error loading auth state:", error);
        } finally {
            setIsLoading(false);
        }
    }

    const saveAuthState = async (userData: any, sessionData: any) => {
        try {
            await AsyncStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify({
                user: userData,
                session: sessionData
            }));
        } catch (error) {
            console.error("Error saving auth state:", error);
        }
    }

    const clearAuthState = async () => {
        try {
            await AsyncStorage.removeItem(AUTH_STORAGE_KEY);
        } catch (error) {
            console.error("Error clearing auth state:", error);
        }
    }

    const signIn = async ({ username, password }: LoginRequest) => {
        try {
            const params = new URLSearchParams({
                client_id: process.env.EXPO_PUBLIC_KEYCLOAK_CLIENT_ID!,
                grant_type: process.env.EXPO_PUBLIC_KEYCLOAK_GRANT_TYPE! || "password",
                username,
                password,
            });

            const response = await fetch(
                `${KEYCLOAK_URL}/token`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: params.toString(),
                }
            );

            const data = await response.json();
            if (!response.ok) {
                throw new Error(`Sign-in failed: ${data.error_description || data.error}`);
            }

            const decodedToken = jwtDecode(data.access_token) as any;
            
            setUser({
                email: decodedToken.email,
                given_name: decodedToken.given_name,
                family_name: decodedToken.family_name,
            });
            setIsSignedIn(true);
            setSession({
                ...data.session,
                obtained_at: Date.now()
            });

            await saveAuthState(data.user, data.session);
        } catch (error) {
            console.error("Sign-in error:", (error as Error).message);
            throw error;
        }
    }

    const signOut = async () => {
        try {
            setIsSignedIn(false);
            setUser(null);
            setSession(null);

            await clearAuthState();
        } catch (error) {
            console.error("Sign-out error:", error);
            throw error;
        }
    }

    const refreshToken = async () => {
        if (!session?.refresh_token) return;

        try {
            const params = new URLSearchParams({
                client_id: process.env.EXPO_PUBLIC_KEYCLOAK_CLIENT_ID!,
                grant_type: "refresh_token",
                refresh_token: session.refresh_token,
            });

            const response = await fetch(
                `${KEYCLOAK_URL}/token`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: params.toString(),
                }
            );

            const data = await response.json();

            if (!response.ok) {
                console.error("Erro ao renovar token:", data);
                await signOut();
                return;
            }

            const newSession = {
                ...session,
                access_token: data.access_token,
                refresh_token: data.refresh_token || session.refresh_token,
                expires_in: data.expires_in,
                obtained_at: Date.now()
            };

            setSession(newSession);
            await saveAuthState(user, newSession);
            console.log("Token atualizado com sucesso");
        } catch (error) {
            console.error("Erro ao atualizar token:", error);
            await signOut();
        }
    }


    return {
        isSignedIn,
        user,
        session,
        isLoading,
        signIn,
        signOut,
    };
}