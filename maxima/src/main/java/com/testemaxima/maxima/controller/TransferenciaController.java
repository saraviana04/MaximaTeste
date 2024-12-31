package com.testemaxima.maxima.controller;

import com.testemaxima.maxima.service.TransferenciaService;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    // Conectar com o banco de dados H2 (já configurado no application.properties)
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @PostMapping("/realizar")
    public String realizarTransferencia(@RequestParam String contaOrigem,
                                        @RequestParam String contaDestino,
                                        @RequestParam double valor) {
        try (Connection connection = getConnection()) {
            TransferenciaService transferenciaService = new TransferenciaService(connection);
            transferenciaService.realizarTransferencia(contaOrigem, contaDestino, valor);
            return "Transferência realizada com sucesso!";
        } catch (SQLException e) {
            return "Erro ao realizar a transferência: " + e.getMessage();
        }
    }
}
