-- Criando as tabelas no banco de dados H2
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    numeroConta VARCHAR(10) UNIQUE NOT NULL,
    saldo DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS transacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contaOrigem VARCHAR(10) NOT NULL,
    contaDestino VARCHAR(10) NOT NULL,
    valor DOUBLE NOT NULL,
    dataTransacao TIMESTAMP NOT NULL
);

-- Inserindo usuários de exemplo
INSERT INTO usuario (nome, idade, cpf, numeroConta, saldo) VALUES
('João Silva', 25, '12345678910', '123456', 1000.0),
('Maria Oliveira', 30, '98765432100', '987654', 1500.0),
('Carlos Souza', 35, '55555555555', '555555', 2000.0);

-- Inserindo transações de exemplo
INSERT INTO transacao (contaOrigem, contaDestino, valor, dataTransacao) VALUES
('123456', '987654', 200.0, CURRENT_TIMESTAMP),
('987654', '555555', 150.0, CURRENT_TIMESTAMP),
('555555', '123456', 300.0, CURRENT_TIMESTAMP);
