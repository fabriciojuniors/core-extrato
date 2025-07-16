import { useContasBancarias } from "@/src/hooks/useListagemContaBancaria";
import { ContaBancaria } from "@/src/types/conta-bancaria";
import { useRouter } from "expo-router";
import { ActivityIndicator, FlatList, RefreshControl, Text, View } from "react-native";

export default function ContasBancarias() {
    const router = useRouter();
    const {
        data,
        fetchNextPage,
        hasNextPage,
        isFetchingNextPage,
        isLoading,
        error,
        refetch
    } = useContasBancarias(10);

    const contas: ContaBancaria[] = data?.pages.flatMap(page => page.dados) || [];

    const getSaldoFormato = (saldo: number) => {
        return saldo.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL',
        });
    };

    if (isLoading) {
        return (
            <View style={{ marginTop: 20, alignItems: "center" }}>
                <ActivityIndicator size="large" color="#0000ff" />
            </View>
        );
    }

    if (error) {
        router.back()
    }

    return (
        <FlatList
            data={contas}
            keyExtractor={(item) => item.id}
            renderItem={({ item }) => (
                <View style={{ padding: 12, backgroundColor: '#fff', margin: 8, borderRadius: 8, shadowColor: '#000', shadowOffset: { width: 0, height: 1 }, shadowOpacity: 0.2, shadowRadius: 1, elevation: 2 }}>
                    <Text style={{ fontWeight: "bold", fontSize: 16 }}>{item.instituicaoFinanceira.nome}</Text>
                    <Text style={{ fontWeight: "bold" }}>
                        Conta {item.numero} - Agência {item.agencia} - {item.tipo}
                    </Text>
                    <Text style={{ color: "#666" }}>Saldo: {getSaldoFormato(item.saldo)}</Text>
                </View>
            )}
            onEndReached={() => {
                if (hasNextPage) {
                    fetchNextPage();
                }
            }}
            onEndReachedThreshold={0.5}
            ListFooterComponent={() =>
                isFetchingNextPage ? (
                    <View style={{ padding: 20 }}>
                        <ActivityIndicator size="small" color="#1E2A38" />
                    </View>
                ) : null
            }
            refreshControl={<RefreshControl refreshing={isLoading} onRefresh={() => refetch()} />}
            contentContainerStyle={{ paddingBottom: 35 }}
            ListEmptyComponent={() => (
                <View style={{ marginTop: 20, alignItems: "center" }}>
                    <Text style={{ color: "#666" }}>Nenhuma conta bancária encontrada.</Text>
                </View>
            )}
        />
    );
}
