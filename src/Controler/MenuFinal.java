package Controler;

import Auxiliar.Desenho;
import java.awt.*;

// Classe que printa a mensagem final do jogo, 
// uma vez que todas as fases são completadas
public class MenuFinal {
    public void desenharParabens(Graphics gOld) {
        Tela t = Desenho.acessoATelaDoJogo();
        Graphics g = t.getBufferStrategy().getDrawGraphics();

        Graphics g2 = g.create(t.getInsets().left, t.getInsets().top,
                t.getWidth() - t.getInsets().right,
                t.getHeight() - t.getInsets().top);

        Color fundoAzulClaro = new Color(102, 204, 255); 
        g2.setColor(fundoAzulClaro);
        g2.fillRect(0, 0, t.getWidth(), t.getHeight());

        String[] linhas = {
                "Parabéns!",
                "Criadores:",
                "Julio Cesar, Arthur Araujo,",
                " Leonardo Demore"
        };

        g2.setFont(new Font("SansSerif", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();

        int totalAltura = linhas.length * (fm.getHeight() + 10); 
        int startY = (t.getHeight() - totalAltura) / 2;

        g2.setColor(Color.WHITE);

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];
            int textWidth = fm.stringWidth(linha);
            int x = (t.getWidth() - textWidth) / 2;
            int y = startY + i * (fm.getHeight() + 10);
            g2.drawString(linha, x, y);
        }

        g.dispose();
        g2.dispose();

        if (!t.getBufferStrategy().contentsLost()) {
            t.getBufferStrategy().show();
        }
    }

}
