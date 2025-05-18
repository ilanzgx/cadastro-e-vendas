package main.java;

import main.java.services.ClienteService;
import main.java.services.LivroService;
import main.java.services.ProdutoService;
import main.java.services.VendaService;
import main.java.ui.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClienteService();
                new LivroService();
                new ProdutoService();
                new VendaService();

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Window().setVisible(true);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }
}