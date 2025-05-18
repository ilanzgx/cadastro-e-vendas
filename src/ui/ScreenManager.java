package ui;

import entities.Venda;
import ui.views.cliente.ClienteFormView;
import ui.views.cliente.ClienteListView;
import ui.views.cliente.ClienteView;
import ui.views.HomeView;
import ui.views.produto.ProdutoFormView;
import ui.views.produto.ProdutoListView;
import ui.views.produto.ProdutoView;
import ui.views.venda.VendaFormView;
import ui.views.venda.VendaListDetalhesView;
import ui.views.venda.VendaListView;
import ui.views.venda.VendaView;

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
    }

    public void showHomeView() {
        cardLayout.show(mainPanel, "HOME");
        window.setTitle("Sistema de vendas - Home");
    }

    /*
    * Views de clientes
    *   showClienteMainView - Página inicial de clientes
    *   showClienteFormView - Cadastro de um cliente
    *   showClienteListView - Listar todos os clientes
    *   TODO: showClienteUpdateView e showClienteDeleteView. OU fazer interação no listview com as respectivas funcionalidades.
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
        cardLayout.show(mainPanel, "CLIENTE_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de clientes (listar)");
    }

    /*
     * Views de produtos
     *   showProdutoMainView - Página inicial de clientes
     *   showProdutoFormView - Cadastro de um cliente
     *   showProdutoListView - Listar todos os clientes
     *   TODO: showProdutoUpdateView e showProdutoDeleteView. OU fazer interação no listview com as respectivas funcionalidades.
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
        cardLayout.show(mainPanel, "PRODUTO_LIST");
        window.setTitle("Sistema de vendas - Gerenciamento de produtos (listar)");
    }

    /*
     * Views de vendas
     *   showVendaMainView - Página inicial de clientes
     *   showVendaFormView - Cadastro de um cliente
     *   showVendaListView - Listar todos os clientes
     *   showVendaDetalhesView - Detalhes de uma venda
     *   TODO: showVendaUpdateView e showVendaDeleteView. OU fazer interação no listview com as respectivas funcionalidades.
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
}
