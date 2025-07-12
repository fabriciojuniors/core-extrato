export interface ApiResponse<T> {
    dados: T[];
    paginaAtual: number;
    totalPaginas: number;
    totalRegistros: number;
}