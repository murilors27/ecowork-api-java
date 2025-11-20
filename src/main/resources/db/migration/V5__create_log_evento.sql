CREATE TABLE log_evento (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    mensagem TEXT NOT NULL,
    data_hora TIMESTAMP NOT NULL
);