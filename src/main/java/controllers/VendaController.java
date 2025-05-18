package main.java.controllers;

import main.java.entities.Venda;
import main.java.services.VendaService;

import java.util.List;

public class VendaController {
    private final VendaService vendaService;

    public VendaController() {
        this.vendaService = new VendaService();
    }

    public void salvarVenda(Venda venda) {
        vendaService.registrarVenda(venda);
    }

    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    public Venda listarVendaId(Integer vendaId) {
        return vendaService.listarVendaId(vendaId);
    }
}
