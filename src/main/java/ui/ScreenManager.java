package main.java.ui;

import main.java.ui.views.cliente.ClienteFormView;
import main.java.ui.views.cliente.ClienteListView;
import main.java.ui.views.cliente.ClienteView;
import main.java.ui.views.HomeView;
import main.java.ui.views.livro.LivroFormView;
import main.java.ui.views.livro.LivroListView;
import main.java.ui.views.livro.LivroView;
import main.java.ui.views.produto.ProdutoFormView;
import main.java.ui.views.produto.ProdutoListView;
import main.java.ui.views.produto.ProdutoView;
import main.java.ui.views.venda.VendaFormView;
import main.java.ui.views.venda.VendaListDetalhesView;
import main.java.ui.views.venda.VendaListView;
import main.java.ui.views.venda.VendaView;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ScreenManager {
    private final JFrame window;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public ScreenManager(JFrame window) {
        this.window = window;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        registrarViews();

        window.add(mainPanel, BorderLayout.CENTER);
    }

    private void registrarViews() {
        // Página inicial
        mainPanel.add(new HomeView(this), "HOME");

        // Views de Cliente
        ClienteView clienteView = new ClienteView(this);
        ClienteListView clienteListView = new ClienteListView(this);
        ClienteFormView clienteFormView = new ClienteFormView(this, clienteListView);

        mainPanel.add(clienteView, "CLIENTE_MAIN");
        mainPanel.add(clienteFormView, "CLIENTE_FORM");
        mainPanel.add(clienteListView, "CLIENTE_LIST");

        // Views de Produto
        ProdutoView produtoView = new ProdutoView(this);
        ProdutoListView produtoListView = new ProdutoListView(this);
        ProdutoFormView produtoFormView = new ProdutoFormView(this, produtoListView);

        mainPanel.add(produtoView, "PRODUTO_MAIN");
        mainPanel.add(produtoFormView, "PRODUTO_FORM");
        mainPanel.add(produtoListView, "PRODUTO_LIST");

        // Views de Venda
        VendaView vendaView = new VendaView(this);
        VendaListView vendaListView = new VendaListView(this);
        VendaFormView vendaFormView = new VendaFormView(this, vendaListView);
        VendaListDetalhesView vendaListDetalhesView = new VendaListDetalhesView(this);

        mainPanel.add(vendaView, "VENDA_MAIN");
        mainPanel.add(vendaFormView, "VENDA_FORM");
        mainPanel.add(vendaListView, "VENDA_LIST");
        mainPanel.add(vendaListDetalhesView, "VENDA_DETALHES");

        // Views de Livro
        LivroView livroView = new LivroView(this);
        LivroListView livroListView = new LivroListView(this);
        LivroFormView livroFormView = new LivroFormView(this, livroListView);

        mainPanel.add(livroView, "LIVRO_MAIN");
        mainPanel.add(livroFormView, "LIVRO_FORM");
        mainPanel.add(livroListView, "LIVRO_LIST");
    }

    public void showHomeView() {
        cardLayout.show(mainPanel, "HOME");
        window.setTitle("Sistema de vendas - Página inicial");
    }

    /*
    * Views de clientes
    *   showClienteMainView - Página inicial de clientes
    *   showClienteFormView - Cadastro de um cliente
    *   showClienteListView - Listar todos os clientes
    * */
    public void showClienteMainView() {
        cardLayout.show(mainPanel, "CLIENTE_MAIN");
        window.setTitle("Sistema de vendas - Gerenciamento de clientes");
    }

    public void showClienteFormView() {
        cardLayout.show(mainPanel, "CLIENTE_FORM");
        window.setTitle("Sistema de vendas - Gerenciamento de clientes (cadastro)");
    }

    public void showClienteListView() {
        ClienteListView clienteListView = (ClienteListView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof ClienteListView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de list não encontrada"));

        clienteListView.atualizarLista();

        cardLayout.show(mainPanel, "CLIENTE_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de clientes (listar)");
    }

    /*
     * Views de produtos
     *   showProdutoMainView - Página inicial de clientes
     *   showProdutoFormView - Cadastro de um cliente
     *   showProdutoListView - Listar todos os clientes
     * */
    public void showProdutoMainView() {
        cardLayout.show(mainPanel, "PRODUTO_MAIN");
        window.setTitle("Sistema de vendas - Gerenciamento de produtos");
    }

    public void showProdutoFormView() {
        cardLayout.show(mainPanel, "PRODUTO_FORM");
        window.setTitle("Sistema de vendas - Gerenciamento de produtos (cadastro)");
    }

    public void showProdutoListView() {
        ProdutoListView produtoListView = (ProdutoListView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof ProdutoListView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de list não encontrada"));

        produtoListView.atualizarLista();

        cardLayout.show(mainPanel, "PRODUTO_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de produtos (listar)");
    }

    /*
     * Views de vendas
     *   showVendaMainView - Página inicial de clientes
     *   showVendaFormView - Cadastro de um cliente
     *   showVendaListView - Listar todos os clientes
     *   showVendaDetalhesView - Detalhes de uma venda
     * */
    public void showVendaMainView() {
        cardLayout.show(mainPanel, "VENDA_MAIN");
        window.setTitle("Sistema de vendas - Gerenciamento de vendas");
    }

    public void showVendaFormView() {
        VendaFormView vendaFormView = (VendaFormView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof VendaFormView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de cadastro não encontrada"));
        vendaFormView.carregarClientes();
        vendaFormView.carregarProdutos();

        cardLayout.show(mainPanel, "VENDA_FORM");
        window.setTitle("Sistema de vendas - Gerenciamento de vendas (cadastro)");
    }

    public void showVendaListView() {
        VendaListView vendaListView = (VendaListView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof VendaListView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de list não encontrada"));

        vendaListView.atualizarLista();

        cardLayout.show(mainPanel, "VENDA_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de vendas (listar)");
    }

    public void showVendaDetalhesView(Integer vendaId) {
        VendaListDetalhesView detalhesView = (VendaListDetalhesView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof VendaListDetalhesView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de detalhes não encontrada"));

        detalhesView.carregarVenda(vendaId);

        cardLayout.show(mainPanel, "VENDA_DETALHES");
        window.setTitle(String.format("Sistema de vendas - Gerenciamento de vendas (detalhes id: %d)", vendaId));
    }

    /*
     * Views de livros
     *   showLivrosMainView - Página inicial de livros
     *   showLivroFormView - Cadastro de um livro
     *   showLivroListView - Listar todos os livros
     * */
    public void showLivroMainView() {
        cardLayout.show(mainPanel, "LIVRO_MAIN");
        window.setTitle("Sistema de vendas - Gerenciamento de livros");
    }

    public void showLivroFormView() {
        LivroFormView vendaFormView = (LivroFormView) Arrays.stream(mainPanel.getComponents())
                .filter(c -> c instanceof LivroFormView)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("View de cadastro não encontrada"));
        //vendaFormView.carregarClientes();
        //vendaFormView.carregarProdutos();

        cardLayout.show(mainPanel, "LIVRO_FORM");
        window.setTitle("Sistema de vendas - Gerenciamento de livros (cadastro)");
    }

    public void showLivroListView() {
        cardLayout.show(mainPanel, "LIVRO_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de livros (listar)");
    }
}
