package services;

import repositories.VendaRepository;

public class VendaService {
    private final VendaRepository vendaRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepository();
        vendaRepository.criarTabela();
    }
}
