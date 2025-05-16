import entities.Cliente;
import entities.Livro;
import entities.Produto;
import entities.Venda;
import services.ClienteService;
import services.LivroService;
import services.ProdutoService;
import services.VendaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Inicializa os serviços
        ClienteService clienteService = new ClienteService();
        ProdutoService produtoService = new ProdutoService();
        VendaService vendaService = new VendaService();
        LivroService livroService = new LivroService();

        // Cria um cliente
        Cliente cliente = new Cliente();
        cliente.setNome("João da Silva");
        cliente.setCpf("123.456.789-00");
        clienteService.criarCliente(cliente);

        // Cria alguns produtos
        Produto produto1 = new Produto();
        produto1.setNome("Notebook Dell");
        produto1.setPreco(3500.0);
        produtoService.criarProduto(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Mouse Sem Fio");
        produto2.setPreco(120.0);
        produtoService.criarProduto(produto2);

        // Cria o mapa de produtos e quantidades para a venda
        Map<Produto, Integer> produtosVenda = new HashMap<>();
        produtosVenda.put(produto1, 1); // 1 Notebook
        produtosVenda.put(produto2, 2); // 2 Mouses

        // Cria a venda
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setProdutosQuantidades(produtosVenda);
        venda.setData(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Registra a venda
        vendaService.registrarVenda(venda);
        System.out.println("Venda registrada com sucesso!");

        // Cria um livro
        Livro livro = new Livro();
        livro.setNome("O Pequeno Principe");
        livro.setPreco(50.0);
        livro.setAutor("Antoine de Saint-Exupér");
        livroService.criarLivro(livro);

        // Edita um livro
        Livro novolivro = new Livro();
        novolivro.setId(3);
        novolivro.setNome("O Pequeno Principe");
        novolivro.setPreco(75.0);
        novolivro.setAutor("Antoine de Saint-Exupéry");
        livroService.editarLivro(novolivro);

        // Deleta um livro
        livroService.deletarLivro(6);
    }
}