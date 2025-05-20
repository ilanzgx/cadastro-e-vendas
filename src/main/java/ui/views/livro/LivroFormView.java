package main.java.ui.views.livro;

import main.java.controllers.LivroController;
import main.java.ui.ScreenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LivroFormView extends JPanel {
    private final LivroController livroController;
    private final ScreenManager screenManager;

    private JTextField textNome, textPreco, textAutor;
    private final LivroListView livroListView;

    public LivroFormView(ScreenManager screenManager, LivroListView listView) {
        this.livroController = new LivroController();
        this.screenManager = screenManager;
        this.livroListView = listView;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Cadastro de Livro");
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

        formPanel.add(new JLabel("Autor:"));
        textAutor = new JTextField();
        formPanel.add(textAutor);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.setMargin(new Insets(8, 30, 8, 30));
        JButton buttonSalvar = new JButton("Salvar");
        buttonSalvar.setMargin(new Insets(8, 30, 8, 30));

        buttonSalvar.addActionListener(event -> {
            cadastrarLivro(textNome.getText(), Double.parseDouble(textPreco.getText()), textAutor.getText());
        });

        buttonVoltar.addActionListener(event -> screenManager.showLivroMainView());

        buttonPanel.add(buttonSalvar);
        buttonPanel.add(buttonVoltar);
        mainPanel.add(buttonPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(mainPanel, gbc);
    }

    private void cadastrarLivro(String nome, Double preco, String autor) {
        try {
            livroController.salvarLivro(nome, preco, autor);

            JOptionPane.showMessageDialog(this, "Livro registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            livroListView.atualizarLista();
            screenManager.showLivroMainView();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar livro: " + error.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
