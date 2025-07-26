ALTER TABLE importacoes
    ALTER COLUMN id_conta_bancaria DROP NOT NULL;

ALTER TABLE movimentacoes
    DROP COLUMN id_conta_destino;