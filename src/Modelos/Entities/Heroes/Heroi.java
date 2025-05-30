package Modelos.Entities.Heroes;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelos.Personagem;
import java.awt.Rectangle;

// Classe que define o comportamento específico do Mario
public class Heroi extends Personagem {
    public Heroi(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    public void voltaAUltimaPosicao() {
        this.pPosicao.volta();
    }

    public boolean setPosicao(int linha, int coluna) {
        if (this.pPosicao.setPosicao(linha, coluna)) {
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;
    }

    private boolean validaPosicao() {
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
        for (int i = 0; i < 4; i++) { 
            Posicao nextPos = new Posicao(this.pPosicao.getLinha() - 1, this.pPosicao.getColuna());
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(nextPos)) {
                return false; 
            }
            this.pPosicao.setPosicao(nextPos.getLinha(), nextPos.getColuna());
        }
        return true;
    }

    public boolean moveDown() {
        if (super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        this.setImagem("marioR.png");
        if (super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        this.setImagem("marioL.png");
        if (super.moveLeft())
            return validaPosicao();
        return false;
    }

}
