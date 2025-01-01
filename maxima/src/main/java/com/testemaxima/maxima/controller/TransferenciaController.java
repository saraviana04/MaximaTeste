package com.testemaxima.maxima.controller;

import com.testemaxima.maxima.dto.TransferenciaRequest;
import com.testemaxima.maxima.service.TransferenciaService;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/transacoes")
public class TransferenciaController {

    // Método para obter a conexão com o banco de dados H2 em memória
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
    }

    // Método para realizar a transferência com dados recebidos no corpo da requisição em formato JSON
    @PostMapping("/realizar") // Endereço para realizar a transferência
    public String realizarTransferencia(@RequestBody TransferenciaRequest request) {
        System.out.println("Requisição recebida: " + request);
        try (Connection connection = getConnection()) {
            TransferenciaService transferenciaService = new TransferenciaService(connection);
            // Chama o serviço para realizar a transferência com os dados do request
            boolean transferenciaRealizada = transferenciaService.realizarTransferencia(
                    request.getContaOrigem(),
                    request.getContaDestino(),
                    request.getValor()
            );
            if (transferenciaRealizada) {
                return "Transferência realizada com sucesso!";
            } else {
                return "Erro: Transferência não realizada. Verifique as informações ou o saldo das contas.";
            }
        } catch (SQLException e) {
            return "Erro ao realizar a transferência: " + e.getMessage();
        }
    }
}
