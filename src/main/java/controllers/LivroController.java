package main.java.controllers;

import main.java.entities.Livro;
import main.java.services.LivroService;

import java.util.List;

public class LivroController {
    private final LivroService livroService;

    public LivroController() {
        this.livroService = new LivroService();
    }

    public Livro salvarLivro(String nome, Double preco, String autor) {
        Livro livro = new Livro();
        livro.setNome(nome);
        livro.setPreco(preco);
        livro.setAutor(autor);

        livroService.criarLivro(livro);
        return livro;
    }

    public List<Livro> listarLivros() {
        return livroService.listarLivros();
    }

    public Boolean editarLivro(Livro livro) {
        return livroService.editarLivro(livro);
    }

    public Boolean deletarLivro(Integer id) {
        return livroService.deletarLivro(id);
    }
}
