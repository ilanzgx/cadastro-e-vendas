import entities.Produto;
import services.ProdutoService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProdutoService produtoService = new ProdutoService();

        produtoService.criarProduto("Camisa", 50);
        produtoService.criarProduto("Sapato", 120);

        List<Produto> produtos = produtoService.listarProdutos();
        for(Produto produto : produtos) {
            System.out.format("ID: %d | Nome: %s | Preco: R$%f\n", produto.getId(), produto.getNome(), produto.getPreco());
        }
    }
}