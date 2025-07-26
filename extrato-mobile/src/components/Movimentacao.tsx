import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Movimentacao } from '../types/movimentacao';

interface Props {
  movimentacao: Movimentacao;
}

export const MovimentacaoCard = ({ movimentacao }: Props) => {
  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('pt-BR');
  };

  const formatCurrency = (value: number) => {
    return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  };

  return (
    <View style={styles.card}>
      <Text style={[styles.valor, { color: movimentacao.valor < 0 ? '#EF4444' : '#10B981' }]}>{formatCurrency(movimentacao.valor)}</Text>
      <Text style={styles.data}>{formatDate(movimentacao.data)}</Text>
      {movimentacao.informacoesAdicionais ? (
        <Text style={styles.info}>{movimentacao.informacoesAdicionais}</Text>
      ) : null}
      <View style={styles.conta}>
        <Text style={styles.contaTexto}>
          Conta: {movimentacao.contaBancaria.numero} / Agência: {movimentacao.contaBancaria.agencia}
        </Text>
        <Text style={styles.contaTexto}>
          Tipo: {movimentacao.contaBancaria.tipo}
        </Text>
        <Text style={styles.contaTexto}>
          Instituição: {movimentacao.contaBancaria.instituicaoFinanceira.nome}
        </Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 15,
    marginVertical: 8,
    marginHorizontal: 16,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 3,
  },
  tipo: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
  },
  valor: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#2a9d8f',
    marginVertical: 4,
  },
  data: {
    fontSize: 14,
    color: '#666',
  },
  info: {
    fontSize: 14,
    color: '#555',
    marginTop: 8,
  },
  conta: {
    marginTop: 12,
    borderTopWidth: 1,
    borderTopColor: '#eee',
    paddingTop: 8,
  },
  contaTexto: {
    fontSize: 14,
    color: '#444',
  },
});

export default MovimentacaoCard;
