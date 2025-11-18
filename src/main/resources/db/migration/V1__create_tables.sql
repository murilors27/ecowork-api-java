CREATE TABLE empresa (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(255) NOT NULL,
    cnpj            VARCHAR(25),
    endereco        VARCHAR(255),
    created_at      TIMESTAMP,
    CONSTRAINT uk_empresa_cnpj UNIQUE (cnpj)
);


CREATE TABLE usuario (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    role            VARCHAR(50) NOT NULL,
    pontos_totais   INTEGER DEFAULT 0,
    data_criacao    TIMESTAMP,
    empresa_id      BIGINT,
    CONSTRAINT fk_usuario_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE sensor (
    id              BIGSERIAL PRIMARY KEY,
    tipo_sensor     VARCHAR(255),
    localizacao     VARCHAR(255),
    empresa_id      BIGINT,
    CONSTRAINT fk_sensor_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE meta_sustentavel (
    id              BIGSERIAL PRIMARY KEY,
    tipo            VARCHAR(50) NOT NULL,
    valor_alvo      NUMERIC(10,2),
    data_inicio     DATE,
    data_fim        DATE,
    status          VARCHAR(50) NOT NULL,
    empresa_id      BIGINT,
    CONSTRAINT fk_meta_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE registro_consumo (
    id              BIGSERIAL PRIMARY KEY,
    tipo            VARCHAR(50) NOT NULL,
    valor           NUMERIC(10,2),
    data_registro   TIMESTAMP,
    usuario_id      BIGINT,
    meta_id         BIGINT,
    sensor_id       BIGINT,
    CONSTRAINT fk_registro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_registro_meta    FOREIGN KEY (meta_id) REFERENCES meta_sustentavel(id),
    CONSTRAINT fk_registro_sensor  FOREIGN KEY (sensor_id) REFERENCES sensor(id)
);

CREATE TABLE pontuacao (
    id              BIGSERIAL PRIMARY KEY,
    quantidade      INTEGER NOT NULL,
    data            TIMESTAMP,
    tipo            VARCHAR(50) NOT NULL,
    usuario_id      BIGINT,
    CONSTRAINT fk_pontuacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
