import { ActivityIndicator, FlatList, RefreshControl, StyleSheet, Text, View } from "react-native";
import { useListagemMovimentacoes } from "../hooks/useListagemMovimentacoes";
import MovimentacaoCard from "./Movimentacao";

export default function Movimentacoes() {

    const { data, fetchNextPage, hasNextPage, isFetching, refetch } = useListagemMovimentacoes(5);

    return (
        <View style={styles.container}>
            <FlatList
                ListHeaderComponent={<Text style={styles.title}>Movimentações</Text>}
                data={data?.pages.flatMap(page => page.dados) || []}
                keyExtractor={(item, index) => `${index}`}
                renderItem={({ item }) => <MovimentacaoCard movimentacao={item} />}
                onEndReachedThreshold={0.5}
                onEndReached={() => {
                    if (hasNextPage && !isFetching) {
                        fetchNextPage();
                    }
                }}
                ListFooterComponent={isFetching ? <ActivityIndicator size={'small'} /> : null}
                contentContainerStyle={{ paddingBottom: 16 }}
                refreshControl={<RefreshControl refreshing={isFetching} onRefresh={() => refetch()} />}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
    },
})