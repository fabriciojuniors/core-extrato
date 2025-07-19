import React, { useState } from "react";
import { StyleSheet, Text, View, TouchableOpacity, ActivityIndicator } from "react-native";
import { Ionicons } from "@expo/vector-icons"; // Ou use outro pacote de ícones
import * as DocumentPicker from 'expo-document-picker';
import useImportacao from "../hooks/useImportacao";

export default function UploadExtrato() {
    const [fileName, setFileName] = useState<DocumentPicker.DocumentPickerAsset | null>(null);
    const [error, setError] = useState<string | null>(null);
    const { mutate, isPending } = useImportacao();

    const handleSelectFile = async () => {
        const result = await DocumentPicker.getDocumentAsync({ type: '*/*' });
        if (!result.canceled) {
            const arquivo = result.assets[0];

            if (arquivo.name && !arquivo.name.endsWith('.ofx')) {
                setError("Por favor, selecione um arquivo com extensão .ofx");
                setFileName(null);
                return;
            }

            setError(null);
            setFileName(arquivo);
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.uploadBox}>
                {isPending && <ActivityIndicator size="large" color="#3B82F6" style={styles.icon} />}
                {!isPending && <Ionicons name="cloud-upload-outline" size={48} color="#3B82F6" style={styles.icon} />}
                <Text style={[styles.label, { color: error ? "#EF4444" : "#111827" }]}>{error ? error : "Selecione o arquivo de extrato"}</Text>

                <View style={{ flexDirection: "row", justifyContent: "space-between", gap: 16 }}>
                    <TouchableOpacity style={styles.button} onPress={handleSelectFile} disabled={isPending}>
                        <Text style={styles.buttonText}>Escolher arquivo</Text>
                    </TouchableOpacity>
                    {fileName && (
                        <TouchableOpacity style={styles.buttonImportar} onPress={() => mutate({ idContaBancaria: 1, arquivo: fileName })} disabled={isPending || !fileName}>
                            <Text style={styles.buttonText}>Importar</Text>
                        </TouchableOpacity>
                    )}
                </View>


                <Text style={styles.fileName}>
                    {fileName ? `📄 ${fileName.name}` : "Nenhum arquivo selecionado"}
                </Text>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        padding: 24,
        justifyContent: "center",
    },
    uploadBox: {
        backgroundColor: "#fff",
        padding: 24,
        borderRadius: 16,
        borderColor: "#E5E7EB",
        borderWidth: 1,
        alignItems: "center",
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.05,
        shadowRadius: 4,
        elevation: 3,
    },
    icon: {
        marginBottom: 12,
    },
    label: {
        fontSize: 16,
        color: "#111827",
        marginBottom: 16,
        textAlign: "center",
    },
    button: {
        backgroundColor: "#3B82F6",
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 8,
    },
    buttonImportar: {
        backgroundColor: "#10B981",
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 8,
    },
    buttonText: {
        color: "#fff",
        fontWeight: "bold",
    },
    fileName: {
        marginTop: 16,
        color: "#4B5563",
        fontSize: 14,
        fontStyle: "italic",
    },
});
