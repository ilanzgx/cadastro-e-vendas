package main.java.repositories;

import main.java.database.SQLiteConnection;
import main.java.entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    public void criarTabela() {
        String queryProdutos = """
            CREATE TABLE IF NOT EXISTS produtos(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                preco REAL NOT NULL,
                tipo TEXT
            );
        """;

        String queryLivros = """
            CREATE TABLE IF NOT EXISTS livros(
                produto_id INTEGER PRIMARY KEY,
                autor TEXT NOT NULL,
                FOREIGN KEY (produto_id) REFERENCES produtos(id)
            );
        """;

        try(Connection connection = SQLiteConnection.conectar(); Statement statement = connection.createStatement()) {
            statement.execute(queryProdutos);
            statement.execute(queryLivros);
        } catch(SQLException error) {
            System.out.println("Erro ao criar a tabela de produtos e livros: " + error.getMessage());
        }
    }

    public void salvar(Produto produto) {
        String query = "INSERT INTO produtos(nome, preco, tipo) VALUES (?, ?, ?)";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setDouble(2, produto.getPreco());
            preparedStatement.setString(3, "NORMAL");
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                produto.setId(resultSet.getInt(1));
            }
        } catch (SQLException error) {
            System.out.println("Erro ao salvar produto: " + error.getMessage());
        }
    }
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String query = "SELECT * FROM produtos";

        try(Connection connection = SQLiteConnection.conectar();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            while(resultSet.next()) {
                Produto produto = new Produto(
                    resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    resultSet.getDouble("preco")
                );
                produtos.add(produto);
            }
        } catch(SQLException error) {
            System.out.println("Erro ao listar os produtos: " + error.getMessage());
        }
        return produtos;
    }

    public void atualizar(Produto produto) {
        String query = "UPDATE produtos SET nome = ?, preco = ? WHERE id = ?";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setDouble(2, produto.getPreco());
            preparedStatement.setInt(3, produto.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException error) {
            System.out.println("Erro ao editar o produto: " + error.getMessage());
        }
    }

    public void deletar(int id) {
        String query = "DELETE FROM produtos WHERE id = ?";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException error) {
            System.out.println("Erro ao deletar o produto: " + error.getMessage());
        }
    }
}
