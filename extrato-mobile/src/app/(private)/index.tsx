import { useAuth } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import { Header } from "@/src/components/Header";
import { SafeAreaView } from "react-native-safe-area-context";
import UploadExtrato from "@/src/components/UploadExtrato";
import Movimentacoes from "@/src/components/Movimentacoes";
import { Modal, TouchableOpacity } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useState } from "react";
import AddMovimentacao from "@/src/components/AddMovimentacao";

export default function Private() {
    const { signOut, user } = useAuth();
    const [showModal, setShowModal] = useState(false);

    const router = useRouter();

    const handleLogout = async () => {
        try {
            await signOut();
        } finally {        
            router.replace("/(auth)/login");
        }
    };

    return (
        <SafeAreaView style={{ flex: 1, backgroundColor: '#fff' }}>
            <Header
                username={user && user.given_name || "Usuário"}
                onLogout={handleLogout}
                onConfig={() => {}}
                onIrParaContas={() => router.push("/(private)/(contasBancarias)")}
            />
            <UploadExtrato />
            <Movimentacoes />
            <TouchableOpacity style={{ position: 'absolute', bottom: 100, right: 10, backgroundColor: 'green', borderRadius: 50, padding: 10, elevation: 5 }}
                onPress={() => setShowModal(true)}>
                <Ionicons name="add" size={24} color="#fff" />
            </TouchableOpacity>
            <Modal visible={showModal} animationType="slide" transparent={true}>
                <AddMovimentacao onClose={() => setShowModal(false)} />
            </Modal>
        </SafeAreaView>
    );
}