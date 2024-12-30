package com.testemaxima.maxima.dao;


import com.testemaxima.maxima.model.Transacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private final Connection connection;

    public TransacaoDAO(Connection connection) {
        this.connection = connection;
    }

    public void registrarTransacao(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacao (contaOrigem, contaDestino, valor, dataTransacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transacao.getContaOrigem());
            stmt.setString(2, transacao.getContaDestino());
            stmt.setDouble(3, transacao.getValor());
            stmt.setTimestamp(4, Timestamp.valueOf(transacao.getDataTransacao()));
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
