package services;

import repositories.LojaRepository;

public class LojaService {
    private final LojaRepository lojaRepository;

    public LojaService() {
        this.lojaRepository = new LojaRepository();
        lojaRepository.criarTabela();
    }
}
