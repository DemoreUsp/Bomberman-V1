import Controler.Tela;
import Controler.MenuInicial;

// Main próxima ao protótipo; menu inicial inserido

public class Main {
    public static void main(String[] args) {
        
        // Mostra o menu inicial
        MenuInicial menu = new MenuInicial();
        
        // Aguarda até que o usuário escolha uma opção
        while(!menu.deveIniciarJogo()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        // Inicia o jogo com a opção selecionada
        java.awt.EventQueue.invokeLater(() -> {
            Tela tTela = new Tela(menu.deveCarregarJogo());
            tTela.setVisible(true);
            tTela.createBufferStrategy(2);
            tTela.go();
        });
    }
}