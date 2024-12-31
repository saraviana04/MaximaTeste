package com.testemaxima.maxima.dao;

import com.testemaxima.maxima.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar usuario
    public void cadastrarUsuario(Usuario usuario) throws SQLException {
        // Verificar se a idade do usuário é 18 ou mais
        if (usuario.getIdade() < 18) {
            throw new IllegalArgumentException("Apenas usuários com 18 anos ou mais podem ser cadastrados.");
        }

        // Verificar se o CPF já está cadastrado
        if (cpfJaCadastrado(usuario.getCpf())) {
            throw new IllegalArgumentException("O CPF informado já está cadastrado.");
        }

        // Gerar um número de conta único (pode ser um valor aleatório ou baseado no id do usuário)
        String numeroConta = gerarNumeroConta(usuario);
        usuario.setNumeroConta(numeroConta);

        // Inserir o usuário no banco de dados
        String sql = "INSERT INTO usuario (nome, idade, cpf, numeroConta, saldo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdade());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getNumeroConta());
            stmt.setDouble(5, usuario.getSaldo());
            stmt.executeUpdate();
        }
    }

    // Método para buscar um usuário por id
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        }
        return null;
    }

    // Método para buscar todos os usuários
    public List<Usuario> buscarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapUsuario(rs));
            }
        }
        return usuarios;
    }

    // Verificar se o CPF já está cadastrado
    public boolean cpfJaCadastrado(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Gerar um número de conta único (aqui é uma implementação com prefixo + id)
    private String gerarNumeroConta(Usuario usuario) {
        // No caso, vamos gerar o número da conta com um prefixo "ACC" e o id do usuário
        return "ACC" + System.currentTimeMillis();
    }

    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setIdade(rs.getInt("idade"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setNumeroConta(rs.getString("numeroConta"));
        usuario.setSaldo(rs.getDouble("saldo"));
        return usuario;
    }
}
