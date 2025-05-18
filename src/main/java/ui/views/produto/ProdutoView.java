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

        JButton buttonListar = new JButton("Listar produtos");
        buttonListar.addActionListener(event -> screenManager.showProdutoListView());

        JButton buttonAtualizar = new JButton("Atualizar produto");

        JButton buttonDeletar = new JButton("Deletar produto");

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
