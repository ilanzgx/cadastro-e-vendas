package ui.views.cliente;

import ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class ClienteView extends JPanel {
    public ClienteView(ScreenManager screenManager) {
        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showHomeView());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton buttonCadastrar = new JButton("Cadastrar cliente");
        buttonCadastrar.addActionListener(event -> screenManager.showClienteFormView());

        JButton buttonListar = new JButton("Listar clientes");
        buttonListar.addActionListener(event -> screenManager.showClienteListView());

        JButton buttonAtualizar = new JButton("Atualizar Cliente");

        JButton buttonDeletar = new JButton("Deletar cliente");

        buttonPanel.add(buttonCadastrar);
        buttonPanel.add(buttonListar);
        buttonPanel.add(buttonAtualizar);
        buttonPanel.add(buttonDeletar);

        // Centralizar os botões
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);

        add(buttonVoltar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
