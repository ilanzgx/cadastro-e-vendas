package main.java.ui.views.venda;

import main.java.controllers.ClienteController;
import main.java.controllers.ProdutoController;
import main.java.controllers.VendaController;
import main.java.entities.Cliente;
import main.java.entities.Produto;
import main.java.entities.Venda;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class VendaFormView extends JPanel {
    private final VendaController vendaController;
    private final ProdutoController produtoController;
    private final ClienteController clienteController;

    private JComboBox<Cliente> clienteCombo;
    private JComboBox<Produto> produtoCombo;
    private JSpinner quantidadeSpinner;
    private DefaultTableModel produtosTableModel;
    private JLabel labelTotal;
    private Map<Produto, Integer> produtosVenda;

    public VendaFormView(ScreenManager screenManager, VendaListView listView) {
        this.vendaController = new VendaController();
        this.produtoController = new ProdutoController();
        this.clienteController = new ClienteController();
        this.produtosVenda = new HashMap<>();

        setLayout(new BorderLayout(10, 10));

        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Cliente: "));
        clienteCombo = new JComboBox<>();
        carregarClientes();
        topPanel.add(clienteCombo);

        // Painel central - Adição de produtos
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Painel para seleção de produtos
        JPanel produtoSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        produtoSelectPanel.add(new JLabel("Produto:"));
        produtoCombo = new JComboBox<>();
        carregarProdutos();
        produtoSelectPanel.add(produtoCombo);

        produtoSelectPanel.add(new JLabel("Quantidade:"));
        quantidadeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        produtoSelectPanel.add(quantidadeSpinner);

        JButton buttonAddProduto = new JButton("Adicionar");
        buttonAddProduto.addActionListener(e -> adicionarProduto());
        produtoSelectPanel.add(buttonAddProduto);

        // Tabela de produtos da venda
        String[] colunas = {"Produto", "Preço Unitário", "Quantidade", "Subtotal"};
        produtosTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable produtosTable = new JTable(produtosTableModel);

        JButton buttonRemover = new JButton("Remover");
        buttonRemover.setEnabled(false);

        // Listener para habilitar/desabilitar o botão
        produtosTable.getSelectionModel().addListSelectionListener(e -> {
            buttonRemover.setEnabled(produtosTable.getSelectedRow() >= 0);
        });

        // Ação do botão remover
        buttonRemover.addActionListener(e -> {
            int selectedRow = produtosTable.getSelectedRow();
            if (selectedRow >= 0) {
                String nomeProduto = (String) produtosTableModel.getValueAt(selectedRow, 0);
                produtosVenda.keySet().removeIf(p -> p.getNome().equals(nomeProduto));
                atualizarTabelaProdutos();
                atualizarTotal();
            }
        });

        // Painel de botões
        JPanel produtoButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        produtoButtonPanel.add(buttonAddProduto);
        produtoButtonPanel.add(buttonRemover);

        centerPanel.add(produtoSelectPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(produtosTable), BorderLayout.CENTER);
        centerPanel.add(produtoButtonPanel, BorderLayout.SOUTH);

        // Painel inferior - Total e botões
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total:"));
        labelTotal = new JLabel("R$ 0,00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(labelTotal);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFinalizar = new JButton("Finalizar Venda");
        btnFinalizar.addActionListener(e -> finalizarVenda(screenManager));

        JButton btnCancelar = new JButton("Voltar");
        btnCancelar.addActionListener(e -> screenManager.showVendaMainView());

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnFinalizar);

        bottomPanel.add(totalPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona todos os painéis
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void carregarClientes() {
        clienteCombo.removeAllItems();
        clienteController.listarClientes().forEach(clienteCombo::addItem);
    }

    public void carregarProdutos() {
        produtoCombo.removeAllItems();
        produtoController.listarProdutos().forEach(produtoCombo::addItem);
    }

    private void adicionarProduto() {
        Produto produto = (Produto) produtoCombo.getSelectedItem();
        int quantidade = (Integer) quantidadeSpinner.getValue();

        if (produto != null) {
            // Adiciona ou atualiza a quantidade no mapa
            produtosVenda.merge(produto, quantidade, Integer::sum);

            // Atualiza a tabela
            atualizarTabelaProdutos();
            atualizarTotal();
        }
    }

    private void atualizarTabelaProdutos() {
        produtosTableModel.setRowCount(0);

        produtosVenda.forEach((produto, quantidade) -> {
            double subtotal = produto.getPreco() * quantidade;
            produtosTableModel.addRow(new Object[]{
                produto.getNome(),
                String.format("R$ %.2f", produto.getPreco()),
                quantidade,
                String.format("R$ %.2f", subtotal)
            });
        });
    }

    private void atualizarTotal() {
        double total = produtosVenda.entrySet().stream()
            .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
            .sum();

        labelTotal.setText(String.format("R$ %.2f", total));
    }

    private void finalizarVenda(ScreenManager screenManager) {
        if (clienteCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (produtosVenda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cliente cliente = (Cliente)clienteCombo.getSelectedItem();

            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setProdutosQuantidades(produtosVenda);
            venda.setData(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            vendaController.salvarVenda(venda);

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            screenManager.showVendaMainView();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao registrar venda: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
