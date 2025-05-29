package main.java.ui.views.produto;

import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class ProdutoView extends JPanel {
    public ProdutoView(ScreenManager screenManager) {
        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showHomeView());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton buttonCadastrar = new JButton("Cadastrar produto");
        buttonCadastrar.addActionListener(event -> screenManager.showProdutoFormView());
        buttonCadastrar.setMargin(new Insets(10, 50, 10, 50));

        JButton buttonListar = new JButton("Listar produtos");
        buttonListar.addActionListener(event -> screenManager.showProdutoListView());
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
