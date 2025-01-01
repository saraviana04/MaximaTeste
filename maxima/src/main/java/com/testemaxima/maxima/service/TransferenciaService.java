package com.testemaxima.maxima.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferenciaService {

    private Connection connection;

    public TransferenciaService(Connection connection) {
        this.connection = connection;
    }

    public boolean realizarTransferencia(String contaOrigem, String contaDestino, double valor) {
        try {
            // Verificar se as contas existem
            if (!contaExiste(contaOrigem) || !contaExiste(contaDestino)) {
                return false;
            }

            // Verificar se a conta de origem tem saldo suficiente
            if (!saldoSuficiente(contaOrigem, valor)) {
                return false;
            }

            // Realizar a transferência
            realizarAtualizacaoSaldos(contaOrigem, contaDestino, valor);

            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Log no servidor
            return false;
        }
    }

    private boolean contaExiste(String numeroConta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private boolean saldoSuficiente(String numeroConta, double valor) throws SQLException {
        String sql = "SELECT saldo FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double saldo = rs.getDouble("saldo");
                return saldo >= valor;
            }
        }
        return false;
    }

    private void realizarAtualizacaoSaldos(String contaOrigem, String contaDestino, double valor) throws SQLException {
        // Atualizar saldo da conta de origem
        String updateOrigem = "UPDATE usuario SET saldo = saldo - ? WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateOrigem)) {
            stmt.setDouble(1, valor);
            stmt.setString(2, contaOrigem);
            stmt.executeUpdate();
        }

        // Atualizar saldo da conta de destino
        String updateDestino = "UPDATE usuario SET saldo = saldo + ? WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateDestino)) {
            stmt.setDouble(1, valor);
            stmt.setString(2, contaDestino);
            stmt.executeUpdate();
        }

        // Registrar a transação
        String insertTransacao = "INSERT INTO transacao (CONTA_ORIGEM, CONTA_DESTINO, valor, DATA_TRANSACAO) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = connection.prepareStatement(insertTransacao)) {
            stmt.setString(1, contaOrigem);
            stmt.setString(2, contaDestino);
            stmt.setDouble(3, valor);
            stmt.executeUpdate();
        }
    }
}
