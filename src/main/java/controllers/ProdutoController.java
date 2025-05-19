package main.java.controllers;

import main.java.entities.Produto;
import main.java.services.ProdutoService;

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

    public Boolean editarProduto(Produto produto) {
        return produtoService.editarProduto(produto);
    }

    public Boolean deletarProduto(Integer id) {
        return produtoService.deletarProduto(id);
    }
}
