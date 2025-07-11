import { createContext, useContext } from "react";
import { useKeycloak } from "../hooks/useKeycloak";

export const AuthContext = createContext({
    isSignedIn: false,
    isLoading: true,
    user: null,
    session: null,
    signIn: async (loginRequest: { username: string; password: string }) => { },
    signOut: async () => { },
})

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const { signIn, signOut, isSignedIn, isLoading, user, session } = useKeycloak();
    return (
        <AuthContext.Provider value={{
            isSignedIn,
            isLoading,
            user,
            session,
            signIn: async ({ username, password }) => {
                try {
                    await signIn({ username, password });
                } catch (error) {
                    throw error;
                }
            },
            signOut: async () => {
                try {
                    await signOut();
                } catch (error) {
                    throw error;
                }
            },
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}