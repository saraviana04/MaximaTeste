package com.testemaxima.maxima.controller;

import com.testemaxima.maxima.dao.UsuarioDAO;
import com.testemaxima.maxima.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    // Conectar com o banco de dados H2 (já configurado no application.properties)
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    // Método para cadastrar um novo usuário e apenas com 18 ou mais
    @PostMapping
    public String cadastrarUsuario(@RequestBody Usuario usuario) {
        // Validar a idade do usuário antes de qualquer operação
        if (usuario.getIdade() < 18) {
            return "Erro: A idade mínima para cadastro é 18 anos.";
        }

        try (Connection connection = getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            // Verifica se o CPF já existe
            if (usuarioDAO.cpfExiste(usuario.getCpf())) {
                return "Erro: CPF já cadastrado.";
            }

            // Gerar número da conta automaticamente
            String numeroConta = gerarNumeroConta();

            // Atribuindo o número da conta gerado ao usuário
            usuario.setNumeroConta(numeroConta);

            usuarioDAO.cadastrarUsuario(usuario);
            return "Usuário cadastrado com sucesso! Número da Conta: " + numeroConta;
        } catch (SQLException e) {
            return "Erro ao acessar o banco de dados: " + e.getMessage();
        }
    }

    // Método para buscar um usuário por ID
    @GetMapping("/{id}")
    public Object buscarUsuarioPorId(@PathVariable int id) {
        try (Connection connection = getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);

            if (usuario == null) {
                return "Erro: Usuário com ID " + id + " não encontrado.";
            }

            return usuario; // Retorna o objeto como JSON
        } catch (SQLException e) {
            return "Erro ao acessar o banco de dados: " + e.getMessage();
        }
    }

    // Método para listar todos os usuários
    @GetMapping
    public List<Usuario> listarTodosUsuarios() {
        try (Connection connection = getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            return usuarioDAO.listarTodosUsuarios();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    // Método para gerar um número de conta de forma simples
    private String gerarNumeroConta() {
        Random random = new Random();
        // Gerando um número aleatório de conta
        return String.format("%06d", random.nextInt(1000000)); // Garante que sempre tenha 6 dígitos
    }
}
