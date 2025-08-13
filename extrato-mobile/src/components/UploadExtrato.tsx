import React, { useState } from "react";
import {
    StyleSheet, Text, View, TouchableOpacity, ActivityIndicator
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import * as DocumentPicker from "expo-document-picker";
import useImportacao from "../hooks/useImportacao";

export default function UploadExtrato() {
    const [fileName, setFileName] = useState<DocumentPicker.DocumentPickerAsset | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [expanded, setExpanded] = useState(false);
    const { mutate, isPending } = useImportacao();

    const handleSelectFile = async () => {
        const result = await DocumentPicker.getDocumentAsync({ type: '*/*' });
        if (!result.canceled) {
            const arquivo = result.assets[0];
            if (!arquivo.name.endsWith(".ofx")) {
                setError("Por favor, selecione um arquivo com extensão .ofx");
                setFileName(null);
                return;
            }
            setError(null);
            setFileName(arquivo);
        }
    };

    const handleImportar = () => {
        if (!fileName) {
            setError("Selecione um arquivo para importar");
            return;
        }

        mutate({ arquivo: fileName });
    };

    return (
        <View style={styles.container}>
            <TouchableOpacity style={[styles.uploadBox, { padding: expanded ? 24 : 12 }]}
                activeOpacity={0.8}
                onPress={() => setExpanded(!expanded)}
            >
                {!expanded && (
                    <View style={{ justifyContent: "space-between", flexDirection: "row", width: "100%", paddingHorizontal: 16 }}>
                        <Ionicons name="cloud-upload-outline" size={24} color="#3B82F6" />
                        <Text style={styles.label}>Clique para carregar extrato</Text>
                        <Ionicons name="chevron-down-outline" size={24} color="#3B82F6" />
                    </View>
                )}

                {expanded && (
                    <>
                        {isPending && <ActivityIndicator size="large" color="#3B82F6" />}
                        {!isPending && <Ionicons name="cloud-upload-outline" size={48} color="#3B82F6" />}
                        <Text style={[styles.label, { color: error ? "#EF4444" : "#111827" }]}>
                            {error ? error : "Selecione o arquivo de extrato"}
                        </Text>

                        <TouchableOpacity style={styles.button} onPress={handleSelectFile} disabled={isPending}>
                            <Text style={styles.buttonText}>Escolher arquivo</Text>
                        </TouchableOpacity>

                        {fileName && (
                            <TouchableOpacity style={styles.buttonImportar} onPress={handleImportar} disabled={isPending}>
                                <Text style={styles.buttonText}>Importar</Text>
                            </TouchableOpacity>
                        )}

                        <Text style={styles.fileName}>
                            {fileName ? `📄 ${fileName.name}` : "Nenhum arquivo selecionado"}
                        </Text>
                    </>
                )}

            </TouchableOpacity>
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
    label: {
        fontSize: 16,
        color: "#111827",
        textAlign: "center",
    },
    button: {
        backgroundColor: "#3B82F6",
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 8,
        marginTop: 12,
    },
    buttonImportar: {
        backgroundColor: "#10B981",
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 8,
        marginTop: 12,
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
