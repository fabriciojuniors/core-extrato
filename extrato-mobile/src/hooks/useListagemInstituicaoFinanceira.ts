import { useInfiniteQuery } from "@tanstack/react-query";
import { useAuth } from "../context/AuthContext";
import { ApiResponse } from "../types/api-response";
import { ContaBancaria, InstituicaoFinanceira } from "../types/conta-bancaria";

export const useListagemInstituicaoFinanceira = (tamanho: number = 10, nome?: string) => {
    const { session, signOut } = useAuth();

    return useInfiniteQuery<ApiResponse<InstituicaoFinanceira>>({
        queryKey: ["instituicoes-financeiras", { nome }],
        queryFn: async ({ pageParam = 0 }) => {
            if (!session || session && !session?.access_token) {
                return await signOut()
            }

            const res = await fetch(`${process.env.EXPO_PUBLIC_API_URL}/v1/instituicoes-financeiras?pagina=${pageParam}&tamanho=${tamanho}&nome=${nome}`, {
                headers: {
                    Authorization: `Bearer ${session.access_token}`,
                },
            });

            if (!res.ok) throw new Error("Erro ao buscar instituições financeiras: " + await res.text());

            return await res.json();
        },
        getNextPageParam: (lastPage, allPages) => {
            const nextPage = lastPage.paginaAtual + 1;
            return nextPage < lastPage.totalPaginas ? nextPage : undefined;
        },
        initialPageParam: 0,
        enabled: !!session?.access_token,
        staleTime: 1000 * 60 * 5,
    });
};
