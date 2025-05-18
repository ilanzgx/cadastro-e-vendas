package ui.views.venda;

import controllers.VendaController;
import entities.Venda;
import ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VendaListDetalhesView extends JPanel {
    private ScreenManager screenManager;
    private VendaController vendaController;

    private Venda vendaAtual;
    private JLabel labelId, labelData, labelCliente, labelTotal;
    private DefaultTableModel defaultTableModel;

    public VendaListDetalhesView(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.vendaController = new VendaController();
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("ID da Venda:"));
        labelId = new JLabel();
        infoPanel.add(labelId);

        infoPanel.add(new JLabel("Data:"));
        labelData = new JLabel();
        infoPanel.add(labelData);

        infoPanel.add(new JLabel("Cliente:"));
        labelCliente = new JLabel();
        infoPanel.add(labelCliente);

        infoPanel.add(new JLabel("Total:"));
        labelTotal = new JLabel();
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        infoPanel.add(labelTotal);

        // Tabela de produtos
        String[] colunas = {"Produto", "Preço Unitário", "Quantidade", "Subtotal"};
        defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable produtosTable = new JTable(defaultTableModel);
        produtosTable.setAutoCreateRowSorter(true);

        // Botão de voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(event -> voltarParaLista());

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnVoltar);

        // Layout principal
        add(infoPanel, BorderLayout.NORTH);
        add(new JScrollPane(produtosTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void carregarVenda(int vendaId) {
        vendaAtual = vendaController.listarVendaId(vendaId);
        if (vendaAtual != null) {
            atualizarInformacoes();
            atualizarTabelaProdutos();
        } else {
            JOptionPane.showMessageDialog(this, "Venda não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaLista();
        }
    }

    private void atualizarInformacoes() {
        labelId.setText(String.valueOf(vendaAtual.getId()));
        labelData.setText(vendaAtual.getData());
        labelCliente.setText(vendaAtual.getCliente().getNome() + " (CPF: " + vendaAtual.getCliente().getCpf() + ")");

        double total = vendaAtual.getProdutosQuantidades().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();
        labelTotal.setText(String.format("R$ %.2f", total));
    }

    private void atualizarTabelaProdutos() {
        defaultTableModel.setRowCount(0);

        vendaAtual.getProdutosQuantidades().forEach((produto, quantidade) -> {
            double subtotal = produto.getPreco() * quantidade;
            defaultTableModel.addRow(new Object[]{
                    produto.getNome(),
                    String.format("R$ %.2f", produto.getPreco()),
                    quantidade,
                    String.format("R$ %.2f", subtotal)
            });
        });
    }

    private void voltarParaLista() {
        screenManager.showVendaListView();
    }
}
