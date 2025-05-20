package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
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
        BufferedImage bi = new BufferedImage(2*Consts.CELL_SIDE, 2*Consts.CELL_SIDE, 2*BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, 2*Consts.CELL_SIDE, 2*Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
}

    @Override
    public void autoDesenho() {
        if(!this.moveLeft())
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        super.autoDesenho();
    }
    
    @Override
    public Rectangle getHitbox() {
    	int cellSize = Consts.CELL_SIDE;
    	int coluna = this.pPosicao.getColuna();
    	int linha = this.pPosicao.getLinha();

    	int larguraEmCelulas = 2;
    	int alturaEmCelulas = 2;
    	int margem = 4;

    	int largura = larguraEmCelulas * cellSize - 2 * margem;
    	int altura = alturaEmCelulas * cellSize - 2 * margem;

    	int x = coluna * cellSize + margem;
    	int y = linha * cellSize + margem;

    	return new Rectangle(x, y, largura, altura);
    }
    
    
}
