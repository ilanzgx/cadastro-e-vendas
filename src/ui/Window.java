package ui;

import javax.swing.*;

public class Window extends JFrame {
    public Window() {
        setTitle("Sistema de Cadastro e Vendas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ScreenManager screenManager = new ScreenManager(this);
        screenManager.showHomeView();
    }
}
