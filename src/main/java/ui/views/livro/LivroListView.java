package main.java.ui.views.livro;

import main.java.controllers.LivroController;
import main.java.entities.Livro;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LivroListView extends JPanel {
    private final LivroController livroController;
    private DefaultTableModel defaultTableModel;

    public LivroListView(ScreenManager screenManager) {
        this.livroController = new LivroController();

        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showLivroMainView());

        // Tabela de livros
        String[] colunas = {"ID", "Nome", "Preço", "Autor"};
        this.defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(defaultTableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        carregarLivros(defaultTableModel);

        JButton buttonAtualizar = new JButton("Atualizar lista");
        buttonAtualizar.addActionListener(event -> carregarLivros(defaultTableModel));

        JButton buttonEditar = new JButton("Editar");
        buttonEditar.setEnabled(false);
        buttonEditar.addActionListener(event -> editarLivroSelecionado(tabela, screenManager));

        JButton buttonExcluir = new JButton("Excluir");
        buttonExcluir.setEnabled(false);
        buttonExcluir.addActionListener(event -> excluirLivroSelecionado(tabela));

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
                    editarLivroSelecionado(tabela, screenManager);
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void carregarLivros(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        List<Livro> livros = livroController.listarLivros();

        for(Livro livro : livros) {
            defaultTableModel.addRow(new Object[]{
                livro.getId(),
                livro.getNome(),
                livro.getPreco(),
                livro.getAutor()
            });
        }
    }

    public void atualizarLista() {
        carregarLivros(this.defaultTableModel);
    }

    private void editarLivroSelecionado(JTable table, ScreenManager screenManager) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer livroId = (Integer) table.getValueAt(selectedRow, 0);
        String livroNome = (String) table.getValueAt(selectedRow, 1);
        Double livroPreco = (Double) table.getValueAt(selectedRow, 2);
        String livroAutor = (String) table.getValueAt(selectedRow, 3);

        String novoNome = JOptionPane.showInputDialog(this, "Editar nome", livroNome);
        if(novoNome == null)
            return;

        String novoPrecoStr = JOptionPane.showInputDialog(this, "Editar preço:", livroPreco);
        if(novoPrecoStr == null)
            return;

        String novoAutor = JOptionPane.showInputDialog(this, "Editar autor:", livroAutor);
        if(novoAutor == null)
            return;

        try {
            Double novoPreco = Double.parseDouble(novoPrecoStr);
            Livro livroEditado = new Livro();
            livroEditado.setId(livroId);
            livroEditado.setNome(novoNome);
            livroEditado.setPreco(novoPreco);
            livroEditado.setAutor(novoAutor);

            Boolean resposta = livroController.editarLivro(livroEditado);

            if (resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar livro", "Erro", JOptionPane.ERROR_MESSAGE);
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

    private void excluirLivroSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer livroId = (Integer) table.getValueAt(selectedRow, 0);
        String livroNome = (String) table.getValueAt(selectedRow, 1);

        int confirmarExclusao = JOptionPane.showConfirmDialog(
                this,
                String.format("Tem certeza que deseja excluir o livro '%s'?", livroNome),
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmarExclusao == JOptionPane.YES_OPTION) {
            Boolean resposta = livroController.deletarLivro(livroId);

            if(resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir livro", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
