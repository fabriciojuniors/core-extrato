import { useState, useEffect } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { LoginRequest } from "../types/login-request";

const AUTH_STORAGE_KEY = "@auth_session";
const KEYCLOAK_URL = process.env.EXPO_PUBLIC_KEYCLOAK_LOGIN_URL!;

export const useKeycloak = () => {
    const [isSignedIn, setIsSignedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [session, setSession] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        loadAuthState();
    }, []);

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
    };

    const saveAuthState = async (userData: any, sessionData: any) => {
        try {
            await AsyncStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify({
                user: userData,
                session: sessionData
            }));
        } catch (error) {
            console.error("Error saving auth state:", error);
        }
    };

    const clearAuthState = async () => {
        try {
            await AsyncStorage.removeItem(AUTH_STORAGE_KEY);
        } catch (error) {
            console.error("Error clearing auth state:", error);
        }
    };

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

            setIsSignedIn(true);
            setUser(data.user);
            setSession(data.session);

            await saveAuthState(data.user, data.session);
        } catch (error) {
            console.error("Sign-in error:", (error as Error).message);
            throw error;
        }
    };

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
    };

    return {
        isSignedIn,
        user,
        session,
        isLoading,
        signIn,
        signOut,
    };
}