package main.java.repositories;

import main.java.database.SQLiteConnection;
import main.java.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClienteRepository {
    public void criarTabela() {
        String query = "CREATE TABLE IF NOT EXISTS clientes(id TEXT PRIMARY KEY, nome TEXT NOT NULL, cpf TEXT NOT NULL)";

        try(Connection connection = SQLiteConnection.conectar(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch(SQLException error) {
            System.out.println("Erro ao criar a tabela de clientes: " + error.getMessage());
        }
    }

    public void salvar(Cliente cliente) {
        String query = "INSERT INTO clientes(id, nome, cpf) VALUES (?, ?, ?)";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cliente.getId());
            preparedStatement.setString(2, cliente.getNome());
            preparedStatement.setString(3, cliente.getCpf());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Erro ao salvar cliente: " + error.getMessage());
        }
    }
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM clientes";

        try(Connection connection = SQLiteConnection.conectar();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            while(resultSet.next()) {
                Cliente cliente = new Cliente(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("nome"),
                    resultSet.getString("cpf")
                );
                clientes.add(cliente);
            }
        } catch(SQLException error) {
            System.out.println("Erro ao listar clientes: " + error.getMessage());
        }

        return clientes;
    }

    public void atualizar(Cliente cliente) {
        String query = "UPDATE clientes SET nome = ?, cpf = ? WHERE id = ?";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getCpf());
            preparedStatement.setString(3, cliente.getId());
            preparedStatement.executeUpdate();
        } catch(SQLException error) {
            System.out.println("Erro ao atualizar cliente: " + error.getMessage());
        }
    }

    public void deletar(String id) {
        String query = "DELETE FROM clientes WHERE id = ?";

        try(Connection connection = SQLiteConnection.conectar(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Erro ao deletar cliente: " + error.getMessage());
        }
    }
}
