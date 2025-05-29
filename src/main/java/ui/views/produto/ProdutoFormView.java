package main.java.ui.views.produto;

import main.java.controllers.ProdutoController;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProdutoFormView extends JPanel {
    private final ProdutoController produtoController;
    private final ScreenManager screenManager;

    private JTextField textNome, textPreco;
    private final ProdutoListView produtoListView;

    public ProdutoFormView(ScreenManager screenManager, ProdutoListView listView) {
        this.produtoListView = listView;
        this.screenManager = screenManager;
        this.produtoController = new ProdutoController();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Cadastro de Produto");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        formPanel.setMaximumSize(new Dimension(500, 80));

        formPanel.add(new JLabel("Nome:"));
        textNome = new JTextField();
        formPanel.add(textNome);

        formPanel.add(new JLabel("Preço:"));
        textPreco = new JTextField();
        formPanel.add(textPreco);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.setMargin(new Insets(8, 30, 8, 30));
        JButton buttonSalvar = new JButton("Salvar");
        buttonSalvar.setMargin(new Insets(8, 30, 8, 30));

        buttonSalvar.addActionListener(event -> {
            cadastrarProduto();
        });

        buttonVoltar.addActionListener(event -> screenManager.showProdutoMainView());

        buttonPanel.add(buttonSalvar);
        buttonPanel.add(buttonVoltar);
        mainPanel.add(buttonPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(mainPanel, gbc);
    }

    private void cadastrarProduto() {
        String nome_ = textNome.getText().trim();
        String preco_ = textPreco.getText().trim();

        if (nome_.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O nome do produto é obrigatório!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            textNome.requestFocus();
            return;
        }

        if (preco_.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O preço do produto é obrigatório!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            textPreco.requestFocus();
            return;
        }

        try {
            produtoController.salvarProduto(nome_, Double.parseDouble(preco_));

            JOptionPane.showMessageDialog(this, "Produto registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            produtoListView.atualizarLista();
            screenManager.showProdutoMainView();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar produto: " + error.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
