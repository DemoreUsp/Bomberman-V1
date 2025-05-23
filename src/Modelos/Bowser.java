package Modelos;

import Auxiliar.Consts;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.lang.Math;

public class Bowser extends Personagem {
    private int moveCounter;
    private int desloc = 0;
    private int direcao = -1;

    public Bowser(String sNomeImagePNG) {
        super(sNomeImagePNG);
        moveCounter = 0;
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
        if(this.getVidas() <= 2) { 
            this.autoDesenhoRage(); 
            return;
        }
        moveCounter++;
        if (moveCounter >= 3) {
            Posicao nextPos = new Posicao(this.getPosicao().getLinha(), 
                this.getPosicao().getColuna() + this.direcao);
            this.desloc += direcao;
            this.setPosicao(nextPos.getLinha(), nextPos.getColuna());
            moveCounter = 0;
            if(Math.abs(this.desloc) >= 4) { 
                this.direcao *= -1; 
                this.desloc = 0;
            }
        }
        super.autoDesenho();
    }
    
    public void autoDesenhoRage() {
        moveCounter++;
        if (moveCounter >= 1) {
            Posicao nextPos = new Posicao(this.getPosicao().getLinha(), 
                this.getPosicao().getColuna() + this.direcao);
            this.desloc += direcao;
            this.setPosicao(nextPos.getLinha(), nextPos.getColuna());
            moveCounter = 0;
            if(Math.abs(this.desloc) >= 5) { 
                this.direcao *= -1; 
                this.desloc = 0;
            }
        }
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
    
    @Override
    public Rectangle getUpHitbox() {
        int cellSize = Consts.CELL_SIDE;
    	int coluna = this.pPosicao.getColuna();
    	int linha = this.pPosicao.getLinha()-1;

    	int larguraEmCelulas = 2;
    	int alturaEmCelulas = 1;
    	int margem = 4;

    	int largura = larguraEmCelulas * cellSize - 2 * margem;
    	int altura = alturaEmCelulas * cellSize - 2 * margem;

    	int x = coluna * cellSize + margem;
    	int y = linha * cellSize + margem;

    	return new Rectangle(x, y, largura, altura);
    }
}
