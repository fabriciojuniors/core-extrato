import { useAuth } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import { Header } from "@/src/components/Header";
import { SafeAreaView } from "react-native-safe-area-context";

export default function Private() {
    const { signOut, user } = useAuth();

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
                onIrParaContas={() => router.push("/(private)/contasBancarias")}
            />
        </SafeAreaView>
    );
}