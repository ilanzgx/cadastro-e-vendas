import services.ClienteService;
import services.LivroService;
import services.ProdutoService;
import services.VendaService;
import ui.Window;

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