package main.java.ui.views.cliente;

import main.java.controllers.ClienteController;
import main.java.entities.Cliente;
import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class ClienteFormView extends JPanel {
    private JTextField textNome, textCpf;
    private final ClienteListView clienteListView;

    public ClienteFormView(ScreenManager screenManager, ClienteListView listView) {
        setLayout(new BorderLayout());
        this.clienteListView = listView;

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showClienteMainView());

        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Nome:"));
        textNome = new JTextField();
        formPanel.add(textNome);

        formPanel.add(new JLabel("CPF:"));
        textCpf = new JTextField();
        formPanel.add(textCpf);

        JButton buttonSalvar = new JButton("Salvar");
        buttonSalvar.addActionListener(event -> {
            ClienteController clienteController = new ClienteController();
            try {
                Cliente novoCliente = clienteController.salvarCliente(
                    textNome.getText(),
                    textCpf.getText()
                );

                clienteListView.atualizarLista();
                screenManager.showClienteMainView();
            } catch(Exception error) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao salvar cliente: " + error.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }

        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(buttonVoltar, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonSalvar, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
}
