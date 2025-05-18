package ui.views.venda;

import controllers.VendaController;
import entities.Venda;
import ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VendaListView extends JPanel {
    private final VendaController vendaController;

    private DefaultTableModel defaultTableModel;
    private JTable tabela;

    public VendaListView(ScreenManager screenManager) {
        this.vendaController = new VendaController();

        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showVendaMainView());

        // Tabela da tabela
        String[] colunas = {"ID", "Data", "Cliente", "Total", "Qtd. Items"};
        this.defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        this.tabela = new JTable(defaultTableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoCreateRowSorter(true);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150); // Data
        tabela.getColumnModel().getColumn(2).setPreferredWidth(200); // Cliente
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100); // Total
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80); // Quantidade items

        JButton buttonDetalhes = new JButton("Ver detalhes");
        buttonDetalhes.setEnabled(false);
        buttonDetalhes.addActionListener(event -> mostrarDetalhesVenda(screenManager));

        JButton buttonAtualizar = new JButton("Atualizar lista");
        buttonAtualizar.addActionListener(event -> carregarVendas());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonDetalhes);
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonVoltar);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            buttonDetalhes.setEnabled(tabela.getSelectedRow() >= 0);
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        carregarVendas();
    }

    private void carregarVendas() {
        defaultTableModel.setRowCount(0);

        List<Venda> vendas = vendaController.listarVendas();

        for(Venda venda : vendas) {
            Double total = venda.getProdutosQuantidades().entrySet().stream()
                    .mapToDouble(event -> event.getKey().getPreco() * event.getValue()).sum();

            Integer quantidadeItems = venda.getProdutosQuantidades().values().stream().mapToInt(Integer::intValue).sum();

            defaultTableModel.addRow(new Object[]{
                venda.getId(),
                venda.getData(),
                venda.getCliente().getNome(),
                String.format("R$ %.2f", total),
                quantidadeItems
            });
        }
    }

    private void mostrarDetalhesVenda(ScreenManager screenManager) {
        Integer selectedRow = tabela.getSelectedRow();

        if(selectedRow >= 0) {
            Integer vendaId = (Integer) defaultTableModel.getValueAt(selectedRow, 0);
            screenManager.showVendaDetalhesView(vendaId);
        }
    }

    public void atualizarLista() {
        carregarVendas();
    }
}
