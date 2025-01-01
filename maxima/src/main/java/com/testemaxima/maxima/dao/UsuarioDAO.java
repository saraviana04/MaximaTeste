package com.testemaxima.maxima.dao;

import com.testemaxima.maxima.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para verificar se um CPF já existe no banco de dados
    public boolean cpfExiste(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Método para cadastrar um novo usuário, com validação de CPF único e geração automática do número da conta
    public void cadastrarUsuario(Usuario usuario) throws SQLException {
        if (cpfExiste(usuario.getCpf())) {
            throw new SQLException("Erro: CPF já cadastrado.");
        }

        // Gerar um número de conta automaticamente (aqui estamos utilizando UUID)
        String numeroConta = UUID.randomUUID().toString().substring(0, 10); // Gera um número único de 10 caracteres

        // Definindo o SQL para inserção no banco de dados
        String sql = "INSERT INTO usuario (nome, idade, cpf, numeroConta, saldo) VALUES (?, ?, ?, ?, ?)";

        // Preparando a consulta SQL
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setInt(2, usuario.getIdade());
            statement.setString(3, usuario.getCpf());
            statement.setString(4, numeroConta);
            statement.setDouble(5, usuario.getSaldo());

            // Executando a inserção no banco de dados
            statement.executeUpdate();
        }
    }

    // Método para buscar um usuário por ID
    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setIdade(rs.getInt("idade"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setNumeroConta(rs.getString("numeroConta"));
                    usuario.setSaldo(rs.getDouble("saldo"));
                    return usuario;
                }
            }
        }
        return null;
    }

    // Método para listar todos os usuários
    public List<Usuario> listarTodosUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setIdade(rs.getInt("idade"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setNumeroConta(rs.getString("numeroConta"));
                usuario.setSaldo(rs.getDouble("saldo"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}