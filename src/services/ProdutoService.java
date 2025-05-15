package services;

import entities.Produto;
import repositories.ProdutoRepository;

import java.util.List;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this.produtoRepository = new ProdutoRepository();
        produtoRepository.criarTabela();
    }

    public void criarProduto(String nome, double preco) {
        if(nome == null || nome.isBlank()) {
            System.out.println("Nome do produto não pode ser vazio.");
            return;
        }

        if(preco < 0) {
            System.out.println("Informe um preco acima de R$0");
            return;
        }

        Produto newProduct = new Produto(0, nome, preco);
        produtoRepository.salvar(newProduct);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listarTodos();
    }

    public void editarProduto(int id, String novoNome, double novoPreco) {
        Produto novoProduto = new Produto(id, novoNome, novoPreco);
        produtoRepository.atualizar(novoProduto);
    }

    public void deletarProduto(int id) {
        produtoRepository.deletar(id);
    }
}
