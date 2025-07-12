import { useAuth } from "@/src/context/AuthContext";
import { useRouter } from "expo-router";
import { Button, Text, View, TextInput, Alert, ActivityIndicator, TouchableOpacity, KeyboardAvoidingView, Platform } from "react-native";
import { useState } from "react";
import * as WebBrowser from 'expo-web-browser';
import * as AuthSession from "expo-auth-session";

const keyCloakLoginUrl = process.env.EXPO_PUBLIC_KEYCLOAK_LOGIN_URL!;

export default function Login() {
    const { signIn } = useAuth();
    const router = useRouter();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleLogin = async () => {
        if (!username || !password) {
            Alert.alert("Erro", "Por favor, preencha todos os campos");
            return;
        }

        setIsLoading(true);
        try {
            await signIn({ username, password });
            router.replace("/(private)");
        } catch (error) {
            console.error("Login failed:", error);
            Alert.alert("Erro", "Falha no login. Verifique suas credenciais.");
        } finally {
            setIsLoading(false);
        }
    }

    const handleRegister = async () => {
        const redirectUri = AuthSession.makeRedirectUri({ scheme: "extratomobile", path: "login" });
        const url = `${keyCloakLoginUrl}/registrations` +
            `?client_id=extrato-mobile` +
            `&response_type=code` +
            `&scope=openid` +
            `&redirect_uri=${redirectUri}`;

        await WebBrowser.openAuthSessionAsync(url, redirectUri);
    }

    return (
        <KeyboardAvoidingView
            style={{ flex: 1, justifyContent: "center", alignItems: "center", padding: 20, backgroundColor: "#F4F4F4" }}
            behavior={Platform.OS === "ios" ? "padding" : "height"}
        >
            <View style={{ alignItems: "center", marginBottom: 30 }}>
                <Text style={{ fontSize: 32 }}>💸</Text>
                <Text style={{ fontSize: 32, fontWeight: "bold", color: "#1E2A38", marginBottom: 10 }}>
                    Extrato
                </Text>
            </View>

            <Text style={{ fontSize: 20, marginBottom: 30, color: "#1E2A38" }}>
                Acesse sua conta
            </Text>

            <TextInput
                style={{
                    width: "100%",
                    height: 50,
                    borderWidth: 1,
                    borderColor: "#ccc",
                    backgroundColor: "#fff",
                    borderRadius: 10,
                    paddingHorizontal: 15,
                    marginBottom: 15,
                    fontSize: 16,
                }}
                placeholder="Usuário"
                placeholderTextColor="#999"
                value={username}
                onChangeText={setUsername}
                autoCapitalize="none"
                editable={!isLoading}
            />

            <TextInput
                style={{
                    width: "100%",
                    height: 50,
                    borderWidth: 1,
                    borderColor: "#ccc",
                    backgroundColor: "#fff",
                    borderRadius: 10,
                    paddingHorizontal: 15,
                    marginBottom: 20,
                    fontSize: 16,
                }}
                placeholder="Senha"
                placeholderTextColor="#999"
                value={password}
                onChangeText={setPassword}
                secureTextEntry
                editable={!isLoading}
            />

            <TouchableOpacity
                onPress={handleLogin}
                disabled={isLoading}
                style={{
                    width: "100%",
                    height: 50,
                    backgroundColor: "#5EC6C2",
                    borderRadius: 10,
                    justifyContent: "center",
                    alignItems: "center",
                    marginBottom: 15,
                    opacity: isLoading ? 0.6 : 1
                }}
            >
                <Text style={{ color: "#fff", fontSize: 16, fontWeight: "bold" }}>
                    {isLoading ? "Entrando..." : "Entrar"}
                </Text>
            </TouchableOpacity>

            <View style={{ flexDirection: "row", alignItems: "center", marginBottom: 20, gap: 10 }}>
                <View style={{ flex: 1, height: 1, backgroundColor: "#ccc" }} />
                <View><Text style={{ color: "#ccc", fontSize: 16 }}>Ou</Text></View>
                <View style={{ flex: 1, height: 1, backgroundColor: "#ccc" }} />
            </View>

            <TouchableOpacity
                onPress={() => handleRegister()}
                style={{
                    borderWidth: 1,
                    borderColor: "#1E2A38",
                    borderRadius: 10,
                    paddingVertical: 12,
                    paddingHorizontal: 20,
                    width: "100%",
                }}
            >
                <Text style={{ color: "#1E2A38", fontSize: 16, textAlign: "center" }}>
                    Criar uma conta
                </Text>
            </TouchableOpacity>

            {isLoading && (
                <View style={{ marginTop: 20 }}>
                    <ActivityIndicator size="small" color="#1E2A38" />
                </View>
            )}
        </KeyboardAvoidingView>
    );
}
