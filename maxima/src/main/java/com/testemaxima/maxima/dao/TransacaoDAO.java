package com.testemaxima.maxima.dao;

import com.testemaxima.maxima.model.Transacao;
import com.testemaxima.maxima.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private final Connection connection;
    private final UsuarioDAO usuarioDAO;  // Para consultar os usuários

    public TransacaoDAO(Connection connection) {
        this.connection = connection;
        this.usuarioDAO = new UsuarioDAO(connection);  // Inicializando o UsuarioDAO
    }

    // Método para registrar a transação
    public void registrarTransacao(Transacao transacao) throws SQLException {
        // Verificar se as contas de origem e destino existem
        if (!contaExiste(transacao.getContaOrigem())) {
            throw new SQLException("Conta de origem não encontrada", "404", 404);  // Conta de origem não encontrada
        }
        if (!contaExiste(transacao.getContaDestino())) {
            throw new SQLException("Conta de destino não encontrada", "404", 404);  // Conta de destino não encontrada
        }

        // Verificar se o saldo da conta de origem é suficiente
        double saldoOrigem = obterSaldo(transacao.getContaOrigem());
        if (saldoOrigem < transacao.getValor()) {
            throw new SQLException("Saldo insuficiente para a transferência", "400", 400);  // Saldo insuficiente
        }

        // Registrar a transação
        String sql = "INSERT INTO transacao (contaOrigem, contaDestino, valor, dataTransacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transacao.getContaOrigem());
            stmt.setString(2, transacao.getContaDestino());
            stmt.setDouble(3, transacao.getValor());
            stmt.setTimestamp(4, Timestamp.valueOf(transacao.getDataTransacao()));
            stmt.executeUpdate();
        }

        // Atualizar os saldos das contas
        atualizarSaldoConta(transacao.getContaOrigem(), saldoOrigem - transacao.getValor());  // Subtrair do saldo da origem
        double saldoDestino = obterSaldo(transacao.getContaDestino());
        atualizarSaldoConta(transacao.getContaDestino(), saldoDestino + transacao.getValor());  // Adicionar ao saldo da destino
    }

    // Verificar se a conta existe
    private boolean contaExiste(String numeroConta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obter o saldo de uma conta
    private double obterSaldo(String numeroConta) throws SQLException {
        String sql = "SELECT saldo FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                }
            }
        }
        throw new SQLException("Conta não encontrada", "404", 404);  // Caso a conta não seja encontrada
    }

    // Atualizar o saldo de uma conta
    private void atualizarSaldoConta(String numeroConta, double novoSaldo) throws SQLException {
        String sql = "UPDATE usuario SET saldo = ? WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, novoSaldo);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        }
    }

    // Listar todas as transações
    public List<Transacao> listarTransacoes() throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacao";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transacoes.add(mapTransacao(rs));
            }
        }
        return transacoes;
    }

    // Mapear uma transação do ResultSet para objeto
    private Transacao mapTransacao(ResultSet rs) throws SQLException {
        Transacao transacao = new Transacao();
        transacao.setId(rs.getLong("id"));
        transacao.setContaOrigem(rs.getString("contaOrigem"));
        transacao.setContaDestino(rs.getString("contaDestino"));
        transacao.setValor(rs.getDouble("valor"));
        transacao.setDataTransacao(rs.getTimestamp("dataTransacao").toLocalDateTime());
        return transacao;
    }
}
