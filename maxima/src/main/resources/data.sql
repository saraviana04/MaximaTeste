CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    numeroConta VARCHAR(10) UNIQUE NOT NULL,
    saldo DOUBLE NOT NULL
);

CREATE TABLE transacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contaOrigem VARCHAR(10) NOT NULL,
    contaDestino VARCHAR(10) NOT NULL,
    valor DOUBLE NOT NULL,
    dataTransacao TIMESTAMP NOT NULL
);
