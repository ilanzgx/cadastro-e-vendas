package repositories;

import database.SQLiteConnection;
import entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    public void criarTabela() {
        String query = "CREATE TABLE IF NOT EXISTS produtos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, preco REAL)";

        try(Connection connection = SQLiteConnection.conectar(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch(SQLException error) {
            System.out.println("Erro ao criar a tabela de produto: " + error.getMessage());
        }
    }

    public void salvar(Produto produto) {
        String query = "INSERT INTO produtos(nome, preco) VALUES (?, ?)";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setDouble(2, produto.getPreco());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Erro ao salvar produto: " + error.getMessage());
        }
    }
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String query = "SELECT * FROM produtos";

        try(Connection connection = SQLiteConnection.conectar();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query))
        {
            System.out.println(resultSet);

            while(resultSet.next()) {
                Produto p = new Produto(resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    resultSet.getDouble("preco")
                );
                produtos.add(p);
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
