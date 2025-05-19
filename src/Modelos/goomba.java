package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Goomba extends Personagem implements Serializable {

    private boolean bRight;
    int iContador;

    public Goomba(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        iContador = 0;
    }

    public void autoDesenho() {
        int andouTudo = 0;
        if (iContador == 1) {
            iContador = 0;
            if (andouTudo < 5) {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
                andouTudo++;
            } else {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
                andouTudo++;
            }
            if (andouTudo == 10)
                andouTudo = 0;
        }
        super.autoDesenho();
        iContador++;
    }
}
