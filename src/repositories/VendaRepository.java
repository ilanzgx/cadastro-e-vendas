package repositories;

import database.SQLiteConnection;
import entities.Cliente;
import entities.Produto;
import entities.Venda;

import java.sql.*;
import java.util.*;

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
            System.out.println("Erro ao salvar venda: " + error.getMessage());
        }
    }

    public List<Venda> listarTodos() {
        List<Venda> vendas = new ArrayList<>();
        String queryVendas = """
            SELECT v.id, v.data, c.id as cliente_id, c.nome as cliente_nome, c.cpf as cliente_cpf
            FROM vendas v
            JOIN clientes c ON v.cliente_id = c.id
            ORDER BY v.data DESC
        """;

        String queryProdutos = """
            SELECT vp.produto_id, p.nome as produto_nome, p.preco, vp.quantidade
            FROM venda_produtos vp
            JOIN produtos p ON vp.produto_id = p.id
            WHERE vp.venda_id = ?
        """;

        try(
                Connection connection = SQLiteConnection.conectar();
                PreparedStatement preparedStatementVendas = connection.prepareStatement(queryVendas);
                ResultSet resultSet = preparedStatementVendas.executeQuery()
        ) {
            while(resultSet.next()) {
                // Cria o objeto venda
                Venda venda = new Venda();
                venda.setId(resultSet.getInt("id"));
                venda.setData(resultSet.getString("data"));

                // Cria o objeto Cliente
                Cliente cliente = new Cliente();
                cliente.setId(UUID.fromString(resultSet.getString("cliente_id")));
                cliente.setNome(resultSet.getString("cliente_nome"));
                cliente.setCpf(resultSet.getString("cliente_cpf"));
                venda.setCliente(cliente);

                Map<Produto, Integer> produtos = new HashMap<>();
                try(PreparedStatement preparedStatementProdutos = connection.prepareStatement(queryProdutos)) {
                    preparedStatementProdutos.setInt(1, venda.getId());
                    ResultSet resultSetProdutos = preparedStatementProdutos.executeQuery();

                    while(resultSetProdutos.next()) {
                        Produto produto = new Produto();
                        produto.setId(resultSetProdutos.getInt("produto_id"));
                        produto.setNome(resultSetProdutos.getString("produto_nome"));
                        produto.setPreco(resultSetProdutos.getDouble("preco"));

                        int quantidade = resultSetProdutos.getInt("quantidade");
                        produtos.put(produto, quantidade);
                    }
                }

                venda.setProdutosQuantidades(produtos);
                vendas.add(venda);
            }

        } catch (SQLException error) {
            System.out.println("Erro ao listar venda: " + error.getLocalizedMessage());
        }

        return vendas;
    }

    public Venda listarVendaId(Integer vendaId) {
        String queryVenda = """
            SELECT v.id, v.data, c.id as cliente_id, c.nome as cliente_nome, c.cpf as cliente_cpf
            FROM vendas v
            JOIN clientes c ON v.cliente_id = c.id
            WHERE v.id = ?
        """;

        String queryProdutos = """
            SELECT vp.produto_id, p.nome as produto_nome, p.preco, vp.quantidade
            FROM venda_produtos vp
            JOIN produtos p ON vp.produto_id = p.id
            WHERE vp.venda_id = ?
        """;

        try(
            Connection connection = SQLiteConnection.conectar();
            PreparedStatement preparedStatementVenda = connection.prepareStatement(queryVenda))
        {
            preparedStatementVenda.setInt(1, vendaId);
            try(ResultSet resultSetVenda = preparedStatementVenda.executeQuery()) {
                if(resultSetVenda.next()) {
                    Venda venda = new Venda();
                    venda.setId(resultSetVenda.getInt("id"));
                    venda.setData(resultSetVenda.getString("data"));

                    Cliente cliente = new Cliente();
                    cliente.setId(UUID.fromString(resultSetVenda.getString("cliente_id")));
                    cliente.setNome(resultSetVenda.getString("cliente_nome"));
                    cliente.setCpf(resultSetVenda.getString("cliente_cpf"));
                    venda.setCliente(cliente);

                    Map<Produto, Integer> produtos = new HashMap<>();
                    try(PreparedStatement preparedStatementProdutos = connection.prepareStatement(queryProdutos)) {
                        preparedStatementProdutos.setInt(1, vendaId);

                        try(ResultSet resultSetProdutos = preparedStatementProdutos.executeQuery()) {
                            while (resultSetProdutos.next()) {
                                Produto produto = new Produto();
                                produto.setId(resultSetProdutos.getInt("produto_id"));
                                produto.setNome(resultSetProdutos.getString("produto_nome"));
                                produto.setPreco(resultSetProdutos.getDouble("preco"));

                                Integer quantidade = resultSetProdutos.getInt("quantidade");
                                produtos.put(produto, quantidade);
                            }
                        }
                    }
                    venda.setProdutosQuantidades(produtos);

                    return venda;
                }
            }
        } catch (SQLException error) {
            System.out.println("Erro ao listar venda por id: " + error.getLocalizedMessage());
        }
        return null;
    }

    public void atualizar() {}
    public void deletar() {}
}
