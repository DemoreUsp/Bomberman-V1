package Controler;

import Auxiliar.Desenho;
import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class MenuFinal {

    public void desenharParabens(Graphics gOld) {
        
        Graphics2D g2 = (Graphics2D)gOld.create();
        
        
        
    // Fundo
    for (int i = 0; i < 15; i++) {
        for (int j = 0; j < 20; j++) {
            try {
                Image newImage = Toolkit.getDefaultToolkit().getImage(
                        new File(".").getCanonicalPath() + "/imagens/ceu.png");
                g2.drawImage(newImage,
                        j * 40, i * 40,
                        40, 40, null);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Texto
    String texto = "PARABÉNS!";
    g2.setFont(new Font("SansSerif", Font.BOLD, 80));
    FontMetrics fm = g2.getFontMetrics();
    int xi = (800 - fm.stringWidth(texto)) / 2;
    int yi = 600 / 2;

    g2.setColor(Color.BLACK);
    g2.drawString(texto, xi - 4, yi - 4);
    g2.drawString(texto, xi + 4, yi - 4);
    g2.drawString(texto, xi - 4, yi + 4);
    g2.drawString(texto, xi + 4, yi + 4);

    g2.setColor(Color.WHITE);
    g2.drawString(texto, xi, yi);
    
    g2.dispose();
    }

}
