package Modelos;

import Auxiliar.Consts;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Bowser extends Personagem {
    private boolean bRight;
    int iContador;

    public Bowser(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        iContador = 0;
    }
    
    @Override
    protected void carregarImagem(String sNomeImagePNG) {
    try {
        iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
        Image img = iImage.getImage();
        BufferedImage bi = new BufferedImage(3*Consts.CELL_SIDE, 3*Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 3*Consts.CELL_SIDE, 3*Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
        }
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            if (bRight) {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
                System.out.println(this.getPosicao().getLinha() + ' ' + this.getPosicao().getColuna());
            } else {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
                System.out.println(this.getPosicao().getLinha() + ' ' + this.getPosicao().getColuna());
            }

            bRight = !bRight;
        }
        super.autoDesenho();
        iContador++;
    }
}
