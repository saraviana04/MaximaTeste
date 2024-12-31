package com.testemaxima.maxima.controller;

import com.testemaxima.maxima.dao.UsuarioDAO;
import com.testemaxima.maxima.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    @PostMapping
    public String cadastrarUsuario(@RequestBody Usuario usuario) {
        try (Connection connection = getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            usuarioDAO.cadastrarUsuario(usuario);
            return "Usuário cadastrado com sucesso!";
        } catch (IllegalArgumentException e) {
            return "Erro: " + e.getMessage();
        } catch (SQLException e) {
            return "Erro ao acessar o banco de dados: " + e.getMessage();
        }
    }
}
