package Modelos.Entities.Villains.AuxiliarVillains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.ControleDeJogo;
import Modelos.Personagem;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import Auxiliar.Posicao;

public class Billbala extends Personagem {
    private int vidas = 1;
            
    public Billbala(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }
    
    @Override
    protected void carregarImagem(String sNomeImagePNG) {
    try {
        iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
        Image img = iImage.getImage();
        BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
}

    @Override
    public void autoDesenho() {
        ControleDeJogo cj = Desenho.acessoATelaDoJogo().getCj();
        if(!cj.ehPosicaoValida(Desenho.acessoATelaDoJogo().getFaseAtual(), this.getPosicao())) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            return;
        }
        if(!this.moveLeft()){
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        }
        super.autoDesenho();
    }
    
    @Override
    public Rectangle getHitbox() {
    	int cellSize = Consts.CELL_SIDE;
    	int coluna = this.pPosicao.getColuna();
    	int linha = this.pPosicao.getLinha();

    	int larguraEmCelulas = 1;
    	int alturaEmCelulas = 1;
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
        return new Rectangle(
            pPosicao.getColuna() * cellSize,
            (pPosicao.getLinha() - 1) * cellSize, // Hitbox acima do Billbala
            cellSize,
            cellSize
        );
    }
    
    public void receberDano() {
        this.vidas--;
        if (this.vidas <= 0) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        }
    }
    
}
