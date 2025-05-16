package repositories;

import database.SQLiteConnection;
import entities.Produto;
import entities.Venda;

import java.sql.*;
import java.util.Map;

public class VendaRepository {
    public void criarTabela() {
        String queryVendas = """
            CREATE TABLE IF NOT EXISTS vendas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id TEXT,
                data TEXT NOT NULL,
                FOREIGN KEY (cliente_id) REFERENCES clientes(id)
            );
        """;

        String queryVendaProdutos = """
            CREATE TABLE IF NOT EXISTS venda_produtos (
                venda_id INTEGER,
                produto_id INTEGER,
                quantidade INTEGER,
                FOREIGN KEY (venda_id) REFERENCES vendas(id),
                FOREIGN KEY (produto_id) REFERENCES produtos(id),
                PRIMARY KEY (venda_id, produto_id)
            );
        """;

        try(Connection connection = SQLiteConnection.conectar(); Statement statement = connection.createStatement()) {
            statement.execute(queryVendas);
            statement.execute(queryVendaProdutos);
        } catch(SQLException error) {
            System.out.println("Erro ao criar a tabelas de vendas: " + error.getMessage());
        }
    }

    public void salvar(Venda venda) {
        String insertVenda = "INSERT INTO vendas(cliente_id, data) VALUES (?, ?)";
        String insertProdutoVenda = "INSERT INTO venda_produtos(venda_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try(Connection connection = SQLiteConnection.conectar()) {
            connection.setAutoCommit(false);

            try(PreparedStatement preparedStatementVenda = connection.prepareStatement(insertVenda, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementVenda.setString(1, venda.getCliente().getId());
                preparedStatementVenda.setString(2, venda.getData());
                preparedStatementVenda.executeUpdate();

                ResultSet generatedKeys = preparedStatementVenda.getGeneratedKeys();
                if(generatedKeys.next()) {
                    int vendaId = generatedKeys.getInt(1);

                    try(PreparedStatement preparedStatementProduto = connection.prepareStatement(insertProdutoVenda)) {
                        for(Map.Entry<Produto, Integer> entry : venda.getProdutosQuantidades().entrySet()) {
                            preparedStatementProduto.setInt(1, vendaId);
                            preparedStatementProduto.setInt(2, entry.getKey().getId());
                            preparedStatementProduto.setInt(3, entry.getValue());
                            preparedStatementProduto.addBatch();
                        }
                        preparedStatementProduto.executeBatch();
                    }

                    connection.commit();
                }
            } catch (SQLException error) {
                connection.rollback();
                throw error;
            }
        } catch(SQLException error) {
            System.out.println("Erro ao salvar venda: ");
        }
    }

    public void listarTodos() {}
    public void atualizar() {}
    public void deletar() {}
}
