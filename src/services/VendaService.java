package services;

import entities.Produto;
import entities.Venda;
import repositories.ProdutoRepository;
import repositories.VendaRepository;

import java.util.List;

public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService() {
        this.vendaRepository = new VendaRepository();
        this.produtoRepository = new ProdutoRepository();
        vendaRepository.criarTabela();
    }

    public void registrarVenda(Venda venda) {
        for(Produto produto : venda.getProdutosQuantidades().keySet()) {
            if(produto.getId() == null) {
                throw new IllegalStateException("Produto " + produto.getNome() + " não possui ID. Salve-o primeiro.");
            }
        }

        vendaRepository.salvar(venda);
    }

    public List<Venda> listarVendas() {
        return vendaRepository.listarTodos();
    }

    public Venda listarVendaId(Integer vendaId) {
        return vendaRepository.listarVendaId(vendaId);
    }
}
