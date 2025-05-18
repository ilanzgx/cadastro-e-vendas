package controllers;

import entities.Produto;
import services.ProdutoService;

import java.util.List;

public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public Produto salvarProduto(String nome, Double preco) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);

        produtoService.criarProduto(produto);
        return produto;
    }

    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }
}
