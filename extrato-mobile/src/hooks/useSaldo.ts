import { useQuery } from "@tanstack/react-query";
import { useAuth } from "../context/AuthContext";

export const useSaldo = () => {
    const { session } = useAuth();

    return useQuery({
        queryKey: ["saldo"],
        queryFn: async () => {            
            if (!session?.access_token) throw new Error("Sessão inválida");

            const response = await fetch(`${process.env.EXPO_PUBLIC_API_URL}/v1/contas-bancarias/saldo`, {
                headers: {
                    'authorization': `Bearer ${session.access_token}`,
                },
            });

            if (!response.ok) throw new Error("Erro ao buscar saldo");

            return await response.json();
        },
        staleTime: 1000 * 60 * 5,
        subscribed: true,
    });
};
