package main.java.repositories;

import main.java.database.SQLiteConnection;
import main.java.entities.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroRepository {
    public void criarTabela() {
        /*
        String query = "CREATE TABLE IF NOT EXISTS livros(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, preco REAL NOT NULL, autor TEXT NOT NULL)";

        try(Connection connection = SQLiteConnection.conectar(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch(SQLException error) {
            System.out.println("Erro ao criar a tabela de livros: " + error.getMessage());
        }*/
    }

    public void salvar(Livro livro) {
        String insertProduto = "INSERT INTO produtos(nome, preco, tipo) VALUES (?, ?, ?)";
        String insertLivro = "INSERT INTO livros(produto_id, autor) VALUES(?, ?)";

        try(Connection connection = SQLiteConnection.conectar()) {
            connection.setAutoCommit(false);

            try(PreparedStatement preparedStatementProduto = connection.prepareStatement(insertProduto, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatementProduto.setString(1, livro.getNome());
                preparedStatementProduto.setDouble(2, livro.getPreco());
                preparedStatementProduto.setString(3, "LIVRO");
                preparedStatementProduto.executeUpdate();

                ResultSet resultSet = preparedStatementProduto.getGeneratedKeys();
                if(resultSet.next()) {
                    Integer produtoId = resultSet.getInt(1);

                    try(PreparedStatement preparedStatementLivro = connection.prepareStatement(insertLivro)) {
                        preparedStatementLivro.setInt(1, produtoId);
                        preparedStatementLivro.setString(2, livro.getAutor());
                        preparedStatementLivro.executeUpdate();
                    }

                    livro.setId(produtoId);
                    connection.commit();
                }
            } catch (SQLException error) {
                connection.rollback();
                throw error;
            }
        } catch (SQLException error) {
            System.out.println("Erro ao salvar livro: " + error.getMessage());
        }
    }
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String query = """
            SELECT p.id, p.nome, p.preco, l.autor
            FROM produtos p
            JOIN livros l ON p.id = l.produto_id
            WHERE p.tipo = 'LIVRO'
        """;

        try(Connection connection = SQLiteConnection.conectar();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Livro livro = new Livro(
                    resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    resultSet.getDouble("preco"),
                    resultSet.getString("autor")
                );
                livros.add(livro);
            }
        } catch(SQLException error) {
            System.out.println("Erro ao listar livros: " + error.getMessage());
        }
        return livros;
    }

    public void atualizar(Livro livro) {
        String updateProduto = "UPDATE produtos SET nome = ?, preco = ? WHERE id = ? AND tipo = 'LIVRO'";
        String updateLivro = "UPDATE livros SET autor = ? WHERE produto_id = ?";

        try(Connection connection = SQLiteConnection.conectar()) {
            connection.setAutoCommit(false);

            try {
                // Primeiro atualizar a tabela de produtos
                try(PreparedStatement preparedStatementProduto = connection.prepareStatement(updateProduto)) {
                    preparedStatementProduto.setString(1, livro.getNome());
                    preparedStatementProduto.setDouble(2, livro.getPreco());
                    preparedStatementProduto.setInt(3, livro.getId());

                    int rows = preparedStatementProduto.executeUpdate();

                    if(rows <= 0) {
                        throw new RuntimeException("Produto do tipo LIVRO não encontrado com o ID: " + livro.getId());
                    }
                }

                // Logo em seguida atualizar a tabela de livros
                try(PreparedStatement preparedStatementLivro = connection.prepareStatement(updateLivro)) {
                    preparedStatementLivro.setString(1, livro.getAutor());
                    preparedStatementLivro.setInt(2, livro.getId());
                    preparedStatementLivro.executeUpdate();
                }

                connection.commit();
            } catch (SQLException error) {
                connection.rollback();
                throw new RuntimeException("Erro ao atualizar livro");
            }
        } catch(SQLException error) {
            System.out.println("Erro ao atualizar livro: " + error.getMessage());
        }
    }

    public void deletar(int id) {
        String deleteLivro = "DELETE FROM livros WHERE produto_id = ?";
        String deleteProduto = "DELETE FROM produtos WHERE id = ? AND tipo = 'LIVRO'";

        try(Connection connection = SQLiteConnection.conectar()) {
            connection.setAutoCommit(false);

            try {
                // Primeiro deletar de livros
                try(PreparedStatement preparedStatementLivro = connection.prepareStatement(deleteLivro)) {
                    preparedStatementLivro.setInt(1, id);
                    preparedStatementLivro.executeUpdate();
                }

                // Depois deletar de produtos
                try(PreparedStatement preparedStatementProduto = connection.prepareStatement(deleteProduto)) {
                    preparedStatementProduto.setInt(1, id);

                    int rows = preparedStatementProduto.executeUpdate();

                    if(rows <= 0) {
                        throw new RuntimeException("Produto do tipo LIVRO não encontrado com o ID: " + id);
                    }
                }

                connection.commit();
            } catch (SQLException error) {
                connection.rollback();
                throw new RuntimeException("Erro ao deletar livro");
            }
        } catch (SQLException error) {
            System.out.println("Erro ao deletar livro: " + error.getMessage());
        }
    }
}
