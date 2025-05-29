package main.java.ui.views.venda;

import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class VendaView extends JPanel {
    public VendaView(ScreenManager screenManager) {
        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showHomeView());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton buttonCadastrar = new JButton("Cadastrar venda");
        buttonCadastrar.addActionListener(event -> screenManager.showVendaFormView());
        buttonCadastrar.setMargin(new Insets(10, 50, 10, 50));

        JButton buttonListar = new JButton("Listar vendas");
        buttonListar.addActionListener(event -> screenManager.showVendaListView());
        buttonListar.setMargin(new Insets(10, 50, 10, 50));

        buttonPanel.add(buttonCadastrar);
        buttonPanel.add(buttonListar);

        // Centralizar os botões
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);

        add(buttonVoltar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
