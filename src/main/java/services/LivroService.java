package main.java.services;

import main.java.entities.Livro;
import main.java.repositories.LivroRepository;

import java.util.List;

public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService() {
        this.livroRepository = new LivroRepository();
        livroRepository.criarTabela();
    }

    public void criarLivro(Livro livro) {
        if(livro.getNome() == null || livro.getNome().isBlank()) {
            System.out.println("Nome do livro não pode ser vazio.");
            return;
        }

        if(livro.getPreco() < 0) {
            System.out.println("Informe um preco acima de R$0");
            return;
        }

        livroRepository.salvar(livro);
    }

    public List<Livro> listarLivros() {
        return livroRepository.listarTodos();
    }

    public Boolean editarLivro(Livro livro) {
        try {
            livroRepository.atualizar(livro);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deletarLivro(int id) {
        try {
            livroRepository.deletar(id);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
