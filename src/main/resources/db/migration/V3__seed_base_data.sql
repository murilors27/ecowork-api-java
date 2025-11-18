INSERT INTO empresa (nome, cnpj, endereco, created_at)
VALUES (
    'EcoWork Corp',
    '12.345.678/0001-99',
    'Av. Paulista, 1000 - São Paulo',
    NOW()
);

INSERT INTO usuario (nome, email, senha, role, pontos_totais, data_criacao, empresa_id)
VALUES (
    'Administrador Sistema',
    'admin@ecowork.com',
    '$2a$10$5dq.GwNBAERFpUmDW0gQ8eS45SI7eGoGRUjhBOm0lwb/.eQ./CGR2',  -- BCrypt
    'SYSTEM_ADMIN',
    0,
    NOW(),
    1
);

INSERT INTO usuario (nome, email, senha, role, pontos_totais, data_criacao, empresa_id)
VALUES (
    'Gestor da Empresa',
    'gestor@ecowork.com',
    '$2a$10$5dq.GwNBAERFpUmDW0gQ8eS45SI7eGoGRUjhBOm0lwb/.eQ./CGR2',
    'COMPANY_ADMIN',
    0,
    NOW(),
    1
);

INSERT INTO usuario (nome, email, senha, role, pontos_totais, data_criacao, empresa_id)
VALUES (
    'Colaborador Teste',
    'user@ecowork.com',
    '$2a$10$5dq.GwNBAERFpUmDW0gQ8eS45SI7eGoGRUjhBOm0lwb/.eQ./CGR2',
    'EMPLOYEE',
    0,
    NOW(),
    1
);

INSERT INTO sensor (tipo_sensor, localizacao, empresa_id)
VALUES
('ENERGIA', 'Andar 1 - Sala A', 1),
('AGUA', 'Andar 2 - Copa', 1),
('CO2', 'Andar 3 - Auditoria', 1);

INSERT INTO meta_sustentavel (tipo, valor_alvo, data_inicio, data_fim, status, empresa_id)
VALUES
('ENERGIA', 100.00, CURRENT_DATE - INTERVAL '7 DAYS', CURRENT_DATE + INTERVAL '30 DAYS', 'ATIVA', 1),
('AGUA', 50.00, CURRENT_DATE - INTERVAL '10 DAYS', CURRENT_DATE + INTERVAL '20 DAYS', 'ATIVA', 1);

INSERT INTO registro_consumo (tipo, valor, data_registro, usuario_id, meta_id, sensor_id)
VALUES (
    'ENERGIA',
    12.5,
    NOW(),
    3,   -- user
    1,   -- primeira meta
    1    -- primeiro sensor
);

INSERT INTO pontuacao (quantidade, data, tipo, usuario_id)
VALUES (
    10,
    NOW(),
    'BONUS',
    3
);
