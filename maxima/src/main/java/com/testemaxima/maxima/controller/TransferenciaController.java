package com.testemaxima.maxima.controller;

import com.testemaxima.maxima.dto.TransferenciaRequest;
import com.testemaxima.maxima.service.TransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/transacoes")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    // Construtor com injeção de dependência do serviço
    public TransferenciaController() {
        try {
            Connection connection = getConnection();
            this.transferenciaService = new TransferenciaService(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    // Método para obter a conexão com o banco de dados
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequest request) throws SQLException {
        System.out.println("Requisição recebida: " + request);
        boolean transferenciaRealizada = transferenciaService.realizarTransferencia(
                request.getContaOrigem(),
                request.getContaDestino(),
                request.getValor()
        );

        if (transferenciaRealizada) {
            return ResponseEntity.ok("Transferência realizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Transferência não realizada. Verifique as informações ou o saldo das contas.");
        }
    }
}
