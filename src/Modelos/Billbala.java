package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Billbala extends Personagem implements Serializable{
            
    public Billbala(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }
    
    @Override
    protected void carregarImagem(String sNomeImagePNG) {
    try {
        iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
        Image img = iImage.getImage();
        BufferedImage bi = new BufferedImage(4*Consts.CELL_SIDE, 4*Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 4*Consts.CELL_SIDE, 4*Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
}

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        if(!this.moveLeft())
            Desenho.acessoATelaDoJogo().removePersonagem(this);
    }
    
}
