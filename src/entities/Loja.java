package entities;

public class Loja {
    private Cliente[] clientes;
    private Produto[] produtos;
    private Venda[] vendas;

    public Loja(Cliente[] clientes, Produto[] produtos, Venda[] vendas) {
        this.clientes = new Cliente[100];
        this.produtos = new Produto[100];
        this.vendas = new Venda[100];
    }

    public Cliente[] getClientes() {
        return clientes;
    }

    public void setClientes(Cliente[] clientes) {
        this.clientes = clientes;
    }

    public Produto[] getProdutos() {
        return produtos;
    }

    public void setProdutos(Produto[] produtos) {
        this.produtos = produtos;
    }

    public Venda[] getVendas() {
        return vendas;
    }

    public void setVendas(Venda[] vendas) {
        this.vendas = vendas;
    }
}
