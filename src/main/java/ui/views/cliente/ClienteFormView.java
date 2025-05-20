package main.java.ui.views.cliente;

import main.java.controllers.ClienteController;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClienteFormView extends JPanel {
    private final ClienteController clienteController;
    private final ScreenManager screenManager;

    private JTextField textNome, textCpf;
    private final ClienteListView clienteListView;

    public ClienteFormView(ScreenManager screenManager, ClienteListView listView) {
        this.clienteController = new ClienteController();
        this.screenManager = screenManager;
        this.clienteListView = listView;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Cadastro de Cliente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        formPanel.setMaximumSize(new Dimension(500, 80));

        formPanel.add(new JLabel("Nome:"));
        textNome = new JTextField();
        formPanel.add(textNome);

        formPanel.add(new JLabel("CPF:"));
        textCpf = new JTextField();
        formPanel.add(textCpf);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.setMargin(new Insets(8, 30, 8, 30));
        JButton buttonSalvar = new JButton("Salvar");
        buttonSalvar.setMargin(new Insets(8, 30, 8, 30));

        buttonSalvar.addActionListener(event -> {
            cadastrarCliente(textNome.getText(), textCpf.getText());
        });

        buttonVoltar.addActionListener(event -> screenManager.showClienteMainView());

        buttonPanel.add(buttonSalvar);
        buttonPanel.add(buttonVoltar);
        mainPanel.add(buttonPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(mainPanel, gbc);
    }

    private void cadastrarCliente(String nome, String cpf) {
        try {
            clienteController.salvarCliente(nome, cpf);

            JOptionPane.showMessageDialog(this, "Cliente registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clienteListView.atualizarLista();
            screenManager.showClienteMainView();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar cliente: " + error.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
