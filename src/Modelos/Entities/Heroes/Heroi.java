package Modelos.Entities.Heroes;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.ControleDeJogo;
import Controler.Tela;
import Modelos.Personagem;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Rectangle;

public class Heroi extends Personagem implements Serializable{
    public Heroi(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
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
    
    public boolean moveUp() {
        for (int i = 0; i < 4; i++) { // Força do pulo = 4 células
            Posicao nextPos = new Posicao(this.pPosicao.getLinha() - 1, this.pPosicao.getColuna());
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(nextPos)) {
                return false; // Bloqueia o pulo se houver colisão
            }
            this.pPosicao.setPosicao(nextPos.getLinha(), nextPos.getColuna());
        }
        return true;
    }

    public boolean moveDown() {
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        this.setImagem("marioR.png");
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        this.setImagem("marioL.png");
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }    
    
}
