import { useRouter } from "expo-router";
import { useAuth } from "../context/AuthContext";
import { useMutation } from "@tanstack/react-query";
import { Alert } from "react-native";
import { DocumentPickerAsset } from "expo-document-picker";
import { supabase } from "../lib/supabase";

export default function useImportacao() {
    const { session, signOut } = useAuth();
    const router = useRouter();

    return useMutation({
        mutationFn: async ({ arquivo }: { arquivo: DocumentPickerAsset }) => {
            if (!session || !session.access_token) {
                return await signOut();
            }

            if (!arquivo || !arquivo.uri) {
                throw new Error("Arquivo inválido ou não selecionado.");
            }

            if (!arquivo.name || !arquivo.name.endsWith('.ofx')) {
                throw new Error("Por favor, selecione um arquivo com extensão .ofx");
            }

            if (!arquivo.uri) {
                throw new Error("Arquivo não encontrado ou inválido.");
            }

            const arquivoResponse = await fetch(arquivo.uri);

            if (!arquivoResponse.ok) {
                throw new Error("Erro ao ler o arquivo selecionado.");
            }

            const blob = await arquivoResponse.blob();
            const buffer = await new Response(blob).arrayBuffer();

            const fileName = `importacao-${new Date().toISOString()}-${arquivo.name}`
            const { error } = await supabase.storage.from('extratos').upload(fileName, buffer);

            if (error) {
                throw new Error(`Erro ao fazer upload do arquivo: ${error.message}`);
            }

            const res = await fetch(`${process.env.EXPO_PUBLIC_API_URL}/v1/importacoes`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${session.access_token}`,
                },
                body: JSON.stringify({ arquivo: fileName }),
            });

            if (!res.ok) {
                const errorText = await res.text();
                const err = JSON.parse(errorText);
                throw new Error(err.mensagem);
            }
        },
        onError: (error) => {
            Alert.alert("Erro", `Não foi possível importar o extrato: ${error.message}`);
        },
        onSuccess: () => {
            Alert.alert("Sucesso", "Extrato importado com sucesso!");
            router.push("/(private)");
        },
    });
}