-- Tipos ENUM
CREATE TYPE tipo_conta AS ENUM ('CORRENTE', 'POUPANCA', 'SALARIO');
CREATE TYPE tipo_movimentacao AS ENUM ('ENTRADA', 'SAIDA', 'TRANSFERENCIA');

CREATE SEQUENCE contas_bancarias_id_seq;
CREATE SEQUENCE movimentacoes_id_seq;
CREATE SEQUENCE importacoes_id_seq;

-- Tabelas
CREATE TABLE "contas_bancarias"
(
    id                       BIGINT PRIMARY KEY DEFAULT nextval('contas_bancarias_id_seq'),
    "id_usuario"             varchar(36),
    "numero"                 integer    NOT NULL,
    "agencia"                integer    NOT NULL,
    "instituicao_financeira" integer    NOT NULL,
    "tipo"                   tipo_conta NOT NULL,
    "saldo"                  decimal            DEFAULT 0,
    "criado_em"              timestamp  NOT NULL,
    "modificado_em"          timestamp
);

CREATE TABLE "movimentacoes"
(
    id                       BIGINT PRIMARY KEY DEFAULT nextval('movimentacoes_id_seq'),
    "id_conta_bancaria"      integer,
    "data"                   date              NOT NULL,
    "valor"                  decimal           NOT NULL,
    "informacoes_adicionais" text,
    "tipo_movimentacao"      tipo_movimentacao NOT NULL,
    "id_conta_destino"       integer,
    "criado_em"              timestamp         NOT NULL,
    "modificado_em"          timestamp
);

CREATE TABLE "importacoes"
(
    id                  BIGINT PRIMARY KEY DEFAULT nextval('importacoes_id_seq'),
    "id_conta_bancaria" integer,
    "id_usuario"        varchar(36),
    "data"              date      NOT NULL,
    "arquivo"           varchar(255),
    "criado_em"         timestamp NOT NULL,
    "modificado_em"     timestamp
);

-- Relacionamentos (FKs)
ALTER TABLE "contas_bancarias"
    ADD FOREIGN KEY ("id_usuario") REFERENCES "user_entity" ("id");

ALTER TABLE "movimentacoes"
    ADD FOREIGN KEY ("id_conta_bancaria") REFERENCES "contas_bancarias" ("id");

ALTER TABLE "movimentacoes"
    ADD FOREIGN KEY ("id_conta_destino") REFERENCES "contas_bancarias" ("id");

ALTER TABLE "importacoes"
    ADD FOREIGN KEY ("id_conta_bancaria") REFERENCES "contas_bancarias" ("id");

ALTER TABLE "importacoes"
    ADD FOREIGN KEY ("id_usuario") REFERENCES "user_entity" ("id");
