import React, { useState } from "react";
import {
    StyleSheet, Text, View, TouchableOpacity, ActivityIndicator,
    Modal, FlatList, Pressable
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import * as DocumentPicker from "expo-document-picker";
import useImportacao from "../hooks/useImportacao";
import { useContasBancarias } from "../hooks/useListagemContaBancaria";

export default function UploadExtrato() {
    const [fileName, setFileName] = useState<DocumentPicker.DocumentPickerAsset | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [contaSelecionada, setContaSelecionada] = useState<any | null>(null);
    const [expanded, setExpanded] = useState(false);
    const { mutate, isPending } = useImportacao();

    const {
        data: contasBancarias,
        fetchNextPage,
        hasNextPage,
        isFetchingNextPage,
        isLoading
    } = useContasBancarias(10);

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
        if (!contaSelecionada) {
            setError("Selecione uma conta bancária");
            return;
        }

        if (!fileName) {
            setError("Selecione um arquivo para importar");
            return;
        }

        mutate({ idContaBancaria: contaSelecionada.id, arquivo: fileName });
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

                        <TouchableOpacity style={styles.button} onPress={() => setModalVisible(true)} disabled={isPending}>
                            <Text style={styles.buttonText}>
                                {contaSelecionada ? `${contaSelecionada.numero} - ${contaSelecionada.agencia} - ${contaSelecionada.tipo}` : "Selecionar conta bancária"}
                            </Text>
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

            <Modal visible={modalVisible} animationType="slide" transparent>
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Selecione uma conta</Text>
                        <FlatList
                            data={contasBancarias?.pages.flatMap(page => page.dados) ?? []}
                            keyExtractor={(item) => item.id.toString()}
                            onEndReached={() => hasNextPage && fetchNextPage()}
                            onEndReachedThreshold={0.3}
                            renderItem={({ item }) => (
                                <Pressable
                                    style={styles.modalItem}
                                    onPress={() => {
                                        setContaSelecionada(item);
                                        setModalVisible(false);
                                    }}
                                >
                                    <View style={{ padding: 12, backgroundColor: '#fff', margin: 8, borderRadius: 8, borderWidth: 1, borderColor: '#E5E7EB' }}>
                                        <Text style={{ fontWeight: "bold", fontSize: 16 }}>{item.instituicaoFinanceira.nome}</Text>
                                        <Text style={{ fontWeight: "bold" }}>
                                            Conta {item.numero} - Agência {item.agencia} - {item.tipo}
                                        </Text>
                                    </View>
                                </Pressable>
                            )}
                            ListFooterComponent={isFetchingNextPage ? <ActivityIndicator /> : null}
                        />
                        <TouchableOpacity style={styles.modalClose} onPress={() => setModalVisible(false)}>
                            <Text style={{ color: "#fff", fontWeight: "bold" }}>Fechar</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
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
    modalContainer: {
        flex: 1,
        backgroundColor: "rgba(0,0,0,0.3)",
        justifyContent: "flex-end",
    },
    modalContent: {
        backgroundColor: "#fff",
        maxHeight: "70%",
        borderTopLeftRadius: 16,
        borderTopRightRadius: 16,
        padding: 16,
        elevation: 5,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: "bold",
        marginBottom: 16,
    },
    modalItem: {
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: "#E5E7EB",
    },
    modalClose: {
        marginTop: 16,
        backgroundColor: "#EF4444",
        paddingVertical: 10,
        borderRadius: 8,
        alignItems: "center",
    },
});
