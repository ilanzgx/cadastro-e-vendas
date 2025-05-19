package main.java.ui.views.produto;

import main.java.controllers.ProdutoController;
import main.java.entities.Produto;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        JButton buttonEditar = new JButton("Editar");
        buttonEditar.setEnabled(false);
        buttonEditar.addActionListener(event -> editarProdutoSelecionado(tabela, screenManager));

        JButton buttonExcluir = new JButton("Excluir");
        buttonExcluir.setEnabled(false);
        buttonExcluir.addActionListener(event -> excluirProdutoSelecionado(tabela));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonEditar);
        buttonPanel.add(buttonExcluir);
        buttonPanel.add(buttonVoltar);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            buttonEditar.setEnabled(tabela.getSelectedRow() >= 0);
            buttonExcluir.setEnabled(tabela.getSelectedRow() >= 0);
        });

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    editarProdutoSelecionado(tabela, screenManager);
                }
            }
        });

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

    private void editarProdutoSelecionado(JTable table, ScreenManager screenManager) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer produtoId = (Integer) table.getValueAt(selectedRow, 0);
        String produtoNome = (String) table.getValueAt(selectedRow, 1);
        Double produtoPreco = (Double) table.getValueAt(selectedRow, 2);

        String novoNome = JOptionPane.showInputDialog(this, "Editar nome", produtoNome);
        if(novoNome == null)
            return;

        String novoPrecoStr = JOptionPane.showInputDialog(this, "Editar preço:", produtoPreco);
        if(novoPrecoStr == null)
            return;

        try {
            Double novoPreco = Double.parseDouble(novoPrecoStr);
            Produto produtoEditado = new Produto();
            produtoEditado.setId(produtoId);
            produtoEditado.setNome(novoNome);
            produtoEditado.setPreco(novoPreco);

            Boolean resposta = produtoController.editarProduto(produtoEditado);

            if (resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar produto", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(
                this,
                "Preço inválido",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirProdutoSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer produtoId = (Integer) table.getValueAt(selectedRow, 0);
        String produtoNome = (String) table.getValueAt(selectedRow, 1);

        int confirmarExclusao = JOptionPane.showConfirmDialog(
            this,
            String.format("Tem certeza que deseja excluir o produto '%s'?", produtoNome),
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if(confirmarExclusao == JOptionPane.YES_OPTION) {
            Boolean resposta = produtoController.deletarProduto(produtoId);

            if(resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir produto", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
