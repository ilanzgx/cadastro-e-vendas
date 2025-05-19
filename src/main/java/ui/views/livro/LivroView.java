package main.java.ui.views.livro;

import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class LivroView extends JPanel {
    public LivroView(ScreenManager screenManager) {
        setLayout(new BorderLayout());

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showHomeView());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton buttonCadastrar = new JButton("Cadastrar livro");
        buttonCadastrar.addActionListener(event -> screenManager.showLivroFormView());

        JButton buttonListar = new JButton("Listar livros");
        buttonListar.addActionListener(event -> screenManager.showLivroListView());

        buttonPanel.add(buttonCadastrar);
        buttonPanel.add(buttonListar);

        // Centralizar os botões
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);

        add(buttonVoltar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
