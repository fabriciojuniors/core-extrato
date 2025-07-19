import { useEffect, useState } from "react";
import { View, Text, TouchableOpacity, ActivityIndicator } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { StyleSheet } from "react-native";
import { useSaldo } from "../hooks/useSaldo";

type Props = {
    username: string;
    onLogout: () => void;
    onConfig: () => void;
    onIrParaContas: () => void;
};

export const Header = ({ username, onLogout, onConfig, onIrParaContas }: Props) => {
    const { data: saldo, isLoading, error, refetch } = useSaldo();
    const [mostrarSaldo, setMostrarSaldo] = useState(false);

    const getSaldoFormatted = () => {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(saldo?.saldo || 0);
    }

    useEffect(() => {
        if (saldo) {
            refetch();
        }
    }, [mostrarSaldo])

    return (
        <View style={styles.container}>
            <View style={styles.topBar}>
                <Text style={styles.boasVindas}>Olá, {username}</Text>
                <View style={styles.iconeArea}>
                    <TouchableOpacity onPress={onConfig} style={styles.iconeBotao}>
                        <Ionicons name="settings-outline" size={20} color="#1E2A38" />
                    </TouchableOpacity>
                    <TouchableOpacity onPress={onLogout} style={styles.iconeBotao}>
                        <Ionicons name="log-out-outline" size={20} color="#1E2A38" />
                    </TouchableOpacity>
                </View>
            </View>

            <View style={styles.saldoBox}>
                <Text style={styles.label}>Saldo Total</Text>
                {isLoading && <Text style={styles.saldo}><ActivityIndicator size="small" color="#1E2A38" /></Text>}
                {!isLoading &&
                    <View style={{ flexDirection: 'row', gap: 5, alignItems: 'center' }}>
                        <Text style={[styles.saldo, { backgroundColor: mostrarSaldo ? "#f0f0f0" : "transparent" }]}>{!mostrarSaldo ? getSaldoFormatted() : "****"}</Text>
                        <TouchableOpacity onPress={() => setMostrarSaldo(!mostrarSaldo)}>
                            <Ionicons name={mostrarSaldo ? "eye-off-outline" : "eye-outline"} size={16} color="#1E2A38" />
                        </TouchableOpacity>
                    </View>
                }

                <TouchableOpacity onPress={onIrParaContas}>
                    <Text style={styles.linkContas}>Ver minhas contas bancárias</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};


export const styles = StyleSheet.create({
    container: {
        backgroundColor: "#fff",
        padding: 20,
    },
    topBar: {
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
    },
    boasVindas: {
        fontSize: 20,
        fontWeight: "600",
        color: "#1E2A38",
    },
    iconeArea: {
        flexDirection: "row",
        gap: 10,
    },
    iconeBotao: {
        padding: 8,
    },
    label: {
        fontSize: 14,
        color: "#666",
    },
    saldo: {
        fontSize: 28,
        fontWeight: "bold",
        color: "#1E2A38",
        marginBottom: 10,
    },
    linkContas: {
        fontSize: 16,
        color: "#5EC6C2",
        fontWeight: "500",
    },
    saldoBox: {
        marginTop: 20,
        backgroundColor: "#fff",
        padding: 24,
        borderRadius: 16,
        borderColor: "#E5E7EB",
        borderWidth: 1,
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.05,
        shadowRadius: 4,
        elevation: 3,
    },
});