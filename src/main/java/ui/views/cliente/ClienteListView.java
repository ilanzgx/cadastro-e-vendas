package main.java.ui.views.cliente;

import main.java.controllers.ClienteController;
import main.java.entities.Cliente;
import main.java.ui.ScreenManager;
import main.java.utils.CpfUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.UUID;

public class ClienteListView extends JPanel {
    private final ClienteController clienteController;
    private DefaultTableModel defaultTableModel;

    public ClienteListView(ScreenManager screenManager) {
        this.clienteController = new ClienteController();
        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showClienteMainView());

        // Tabela de clientes
        String[] colunas = {"ID", "Nome", "CPF"};
        this.defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(defaultTableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        carregarClientes(defaultTableModel);

        JButton buttonAtualizar = new JButton("Atualizar lista");
        buttonAtualizar.addActionListener(event -> carregarClientes(defaultTableModel));

        JButton buttonEditar = new JButton("Editar");
        buttonEditar.setEnabled(false);
        buttonEditar.addActionListener(event -> editarClienteSelecionado(tabela, screenManager));

        JButton buttonExcluir = new JButton("Excluir");
        buttonExcluir.setEnabled(false);
        buttonExcluir.addActionListener(event -> excluirClienteSelecionado(tabela));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonVoltar);
        buttonPanel.add(buttonEditar);
        buttonPanel.add(buttonExcluir);

        tabela.getSelectionModel().addListSelectionListener(event -> {
            buttonEditar.setEnabled(tabela.getSelectedRow() >= 0);
            buttonExcluir.setEnabled(tabela.getSelectedRow() >= 0);
        });

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    editarClienteSelecionado(tabela, screenManager);
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void carregarClientes(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        List<Cliente> clientes = clienteController.listarClientes();

        for(Cliente cliente : clientes) {
            defaultTableModel.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                CpfUtils.format(cliente.getCpf())
            });
        }
    }

    public void atualizarLista() {
        carregarClientes(this.defaultTableModel);
    }

    private void editarClienteSelecionado(JTable table, ScreenManager screenManager) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clienteId = (String) table.getValueAt(selectedRow, 0);
        String clienteNome = (String) table.getValueAt(selectedRow, 1);
        String clienteCpf = (String) table.getValueAt(selectedRow, 2);

        String novoNome = JOptionPane.showInputDialog(this, "Editar nome", clienteNome);
        if(novoNome == null)
            return;

        String novoCpf = JOptionPane.showInputDialog(this, "Editar Cpf", clienteCpf);
        if(novoCpf == null)
            return;

        if(!CpfUtils.isValid(novoCpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cliente clienteEditado = new Cliente();
            clienteEditado.setId(UUID.fromString(clienteId));
            clienteEditado.setNome(novoNome);
            clienteEditado.setCpf(CpfUtils.clean(novoCpf));

            Boolean resposta = clienteController.editarCliente(clienteEditado);

            if (resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar cliente", "Erro", JOptionPane.ERROR_MESSAGE);
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

    private void excluirClienteSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clienteId = (String) table.getValueAt(selectedRow, 0);
        String clienteNome = (String) table.getValueAt(selectedRow, 1);

        int confirmarExclusao = JOptionPane.showConfirmDialog(
                this,
                String.format("Tem certeza que deseja excluir o cliente '%s'?", clienteNome),
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmarExclusao == JOptionPane.YES_OPTION) {
            Boolean resposta = clienteController.deletarCliente(clienteId);

            if(resposta) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir cliente", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
