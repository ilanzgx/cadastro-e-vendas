package main.java.ui.views;

import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {
    public HomeView(ScreenManager screenManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Sistemas de vendas");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JButton buttonClientes = new JButton("Gerenciar Clientes");
        buttonClientes.setMargin(new Insets(20,100,20,100));

        JButton buttonProdutos = new JButton("Gerenciar Produtos");
        buttonProdutos.setMargin(new Insets(20,100,20,100));

        JButton buttonLivros = new JButton("Gerenciar Livros");
        buttonLivros.setMargin(new Insets(20,100,20,100));

        JButton buttonVendas = new JButton("Gerenciar Vendas");
        buttonVendas.setMargin(new Insets(20,100,20,100));

        buttonClientes.addActionListener(event -> screenManager.showClienteMainView());
        buttonProdutos.addActionListener(event -> screenManager.showProdutoMainView());
        buttonLivros.addActionListener(event -> screenManager.showLivroMainView());
        buttonVendas.addActionListener(event -> screenManager.showVendaMainView());

        gridBagConstraints.gridy = 0;
        add(title, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        add(buttonClientes, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        add(buttonProdutos, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        add(buttonLivros, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        add(buttonVendas, gridBagConstraints);
    }
}
