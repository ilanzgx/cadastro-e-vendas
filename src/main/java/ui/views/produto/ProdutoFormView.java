package main.java.ui.views.produto;

import main.java.controllers.ProdutoController;
import main.java.entities.Produto;
import main.java.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class ProdutoFormView extends JPanel {
    private final ProdutoController produtoController;

    private JTextField textNome, textPreco;
    private final ProdutoListView produtoListView;

    public ProdutoFormView(ScreenManager screenManager, ProdutoListView listView) {
        setLayout(new BorderLayout());
        this.produtoListView = listView;
        this.produtoController = new ProdutoController();

        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> screenManager.showProdutoMainView());

        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Nome:"));
        textNome = new JTextField();
        formPanel.add(textNome);

        formPanel.add(new JLabel("Preço:"));
        textPreco = new JTextField();
        formPanel.add(textPreco);

        JButton buttonSalvar = new JButton("Salvar");
        buttonSalvar.addActionListener(event -> {
            try {
                Produto novoProduto = produtoController.salvarProduto(
                    textNome.getText(),
                    Double.parseDouble(textPreco.getText())
                );

                produtoListView.atualizarLista();
                screenManager.showProdutoMainView();
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(this,
                "Preço inválido! Digite um valor numérico.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            } catch(Exception error) {
                JOptionPane.showMessageDialog(this,
                "Erro ao salvar produto: " + error.getMessage(),
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
