package entities;

import java.util.Map;

public class Venda {
    private Integer id;
    private Cliente cliente;
    private Map<Produto, Integer> produtosQuantidades;
    private String data;

    public Venda() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Map<Produto, Integer> getProdutosQuantidades() {
        return produtosQuantidades;
    }

    public void setProdutosQuantidades(Map<Produto, Integer> produtosQuantidades) {
        this.produtosQuantidades = produtosQuantidades;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
