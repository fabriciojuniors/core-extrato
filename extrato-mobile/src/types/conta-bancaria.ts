export interface ContaBancaria {
    id: string;
    numero: number;
    agencia: number;
    tipo: string;
    saldo: number;
    instituicaoFinanceira: InstituicaoFinanceira;
}

export interface InstituicaoFinanceira {
    id: string;
    nome: string;
}