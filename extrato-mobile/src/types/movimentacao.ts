import { ContaBancaria } from "./conta-bancaria";

export interface Movimentacao {
    valor: number;
    tipo: string;
    data: string;
    informacoesAdicionais: string;
    contaBancaria: ContaBancaria
}