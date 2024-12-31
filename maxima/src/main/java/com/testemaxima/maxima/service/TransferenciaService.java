package com.testemaxima.maxima.service;


import com.testemaxima.maxima.dao.TransacaoDAO;
import com.testemaxima.maxima.dao.UsuarioDAO;
import com.testemaxima.maxima.model.Transacao;
import com.testemaxima.maxima.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferenciaService {

    private final TransacaoDAO transacaoDAO;
    private final UsuarioDAO usuarioDAO;

    public TransferenciaService(Connection connection) {
        this.transacaoDAO = new TransacaoDAO(connection);
        this.usuarioDAO = new UsuarioDAO(connection);
    }

    public void realizarTransferencia(String contaOrigem, String contaDestino, double valor) throws SQLException {
        // Criar uma nova transação
        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);
        transacao.setDataTransacao(java.time.LocalDateTime.now());  // Usando o timestamp atual

        // Registrar a transação
        transacaoDAO.registrarTransacao(transacao);
    }
}
