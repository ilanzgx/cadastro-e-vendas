package services;

import repositories.LivroRepository;

public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService() {
        this.livroRepository = new LivroRepository();
        livroRepository.criarTabela();
    }
}
