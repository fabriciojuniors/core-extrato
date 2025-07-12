export interface ContaBancaria {
    id: string;
    numero: number;
    agencia: number;
    tipo: string;
    saldo: number;
    instituicaoFinanceira: {
        id: string;
        nome: string;
    };
}