package main.java.ui.views.cliente;

import main.java.controllers.ClienteController;
import main.java.entities.Cliente;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClienteListView extends JPanel {
    private DefaultTableModel defaultTableModel;

    public ClienteListView(ScreenManager screenManager) {
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonVoltar);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void carregarClientes(DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        ClienteController clienteController = new ClienteController();
        List<Cliente> clientes = clienteController.listarClientes();

        for(Cliente cliente : clientes) {
            defaultTableModel.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf()
            });
        }
    }

    public void atualizarLista() {
        carregarClientes(this.defaultTableModel);
    }
}
