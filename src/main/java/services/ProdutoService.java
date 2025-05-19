package main.java.services;

import main.java.entities.Produto;
import main.java.repositories.ProdutoRepository;

import java.util.List;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this.produtoRepository = new ProdutoRepository();
        produtoRepository.criarTabela();
    }

    public void criarProduto(Produto produto) {
        if(produto.getNome() == null || produto.getNome().isBlank()) {
            System.out.println("Nome do produto não pode ser vazio.");
            return;
        }

        if(produto.getPreco() < 0) {
            System.out.println("Informe um preco acima de R$0");
            return;
        }

        produtoRepository.salvar(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listarTodos();
    }

    public Boolean editarProduto(Produto produto) {
        try {
            produtoRepository.atualizar(produto);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletarProduto(int id) {
        try {
            produtoRepository.deletar(id);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
