package main.java.controllers;

import main.java.entities.Produto;
import main.java.services.ProdutoService;

import javax.swing.*;
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

        try {
            produtoService.criarProduto(produto);
        } catch (Exception error) {
            System.out.println("Erro ao criar produto:" + error.getMessage());
        }

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
