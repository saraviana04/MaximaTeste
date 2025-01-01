package com.testemaxima.maxima.dao;

import com.testemaxima.maxima.model.Transacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  TransacaoDAO {

    private final Connection connection;
    private final UsuarioDAO usuarioDAO;

    public TransacaoDAO(Connection connection) {
        this.connection = connection;
        this.usuarioDAO = new UsuarioDAO(connection);
    }

    public void registrarTransacao(Transacao transacao) throws SQLException {
        if (!contaExiste(transacao.getContaOrigem())) {
            throw new SQLException("Conta de origem não encontrada", "404", 404);
        }
        if (!contaExiste(transacao.getContaDestino())) {
            throw new SQLException("Conta de destino não encontrada", "404", 404);
        }

        double saldoOrigem = obterSaldo(transacao.getContaOrigem());
        if (saldoOrigem < transacao.getValor()) {
            throw new SQLException("Saldo insuficiente para a transferência", "400", 400);
        }

        String sql = "INSERT INTO transacao (contaOrigem, contaDestino, valor, dataTransacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transacao.getContaOrigem());
            stmt.setString(2, transacao.getContaDestino());
            stmt.setDouble(3, transacao.getValor());
            stmt.setTimestamp(4, Timestamp.valueOf(transacao.getDataTransacao()));
            stmt.executeUpdate();
        }

        atualizarSaldoConta(transacao.getContaOrigem(), saldoOrigem - transacao.getValor());
        double saldoDestino = obterSaldo(transacao.getContaDestino());
        atualizarSaldoConta(transacao.getContaDestino(), saldoDestino + transacao.getValor());
    }

    public boolean contaExiste(String numeroConta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public double obterSaldo(String numeroConta) throws SQLException {
        String sql = "SELECT saldo FROM usuario WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                }
            }
        }
        throw new SQLException("Conta não encontrada", "404", 404);
    }

    public void atualizarSaldoConta(String numeroConta, double novoSaldo) throws SQLException {
        String sql = "UPDATE usuario SET saldo = ? WHERE numeroConta = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, novoSaldo);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        }
    }

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
