import { useMutation } from "@tanstack/react-query";
import { useAuth } from "../context/AuthContext";
import { ContaBancariaFormData } from "../app/(private)/(contasBancarias)/cadastro";
import { Alert } from "react-native";
import { useRouter } from "expo-router";

export function useCadastroContaBancaria() {
    const { session, signOut } = useAuth();
    const router = useRouter();

    return useMutation({
        mutationFn: async (data: ContaBancariaFormData) => {
            if (!session || session &&!session?.access_token) {
                return await signOut()
            }

            const res = await fetch(`${process.env.EXPO_PUBLIC_API_URL}/v1/contas-bancarias`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${session.access_token}`,
                },
                body: JSON.stringify(data),
            });

            if (!res.ok) {
                const errorText = await res.text();
                const err = JSON.parse(errorText);
                throw new Error(err.mensagem);
            }
        },
        onError: (error) => {
            Alert.alert("Erro", `Não foi possível cadastrar a conta bancária: ${error.message}`);
        },
        onSuccess: () => {
            Alert.alert("Sucesso", "Conta bancária cadastrada com sucesso!");
            router.push("/(private)/(contasBancarias)");
        },
    })
}