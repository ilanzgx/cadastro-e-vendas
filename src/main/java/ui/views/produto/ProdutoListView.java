package main.java.ui.views.produto;

import main.java.controllers.ProdutoController;
import main.java.entities.Produto;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoListView extends JPanel {
    private final ProdutoController produtoController;
    private DefaultTableModel defaultTableModel;

    public ProdutoListView(ScreenManager screenManager) {
        this.produtoController = new ProdutoController();

        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showProdutoMainView());

        // Tabela de produtos
        String[] colunas = {"ID", "Nome", "Preço"};
        this.defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(defaultTableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        carregarProdutos(defaultTableModel);

        JButton buttonAtualizar = new JButton("Atualizar lista");
        buttonAtualizar.addActionListener(event -> carregarProdutos(defaultTableModel));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonVoltar);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void carregarProdutos(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        List<Produto> produtos = produtoController.listarProdutos();

        for(Produto produto : produtos) {
            defaultTableModel.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco()
            });
        }
    }

    public void atualizarLista() {
        carregarProdutos(this.defaultTableModel);
    }
}
