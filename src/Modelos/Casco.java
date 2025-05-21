package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public class Casco extends Personagem implements Serializable {
    private boolean movingRight = true;
    private int moveCounter = 0;
    private Tela tela;
    private int moveDelay = 0; // Contador de delay
    private static final int MOVE_DELAY_MAX = 10; // Ajuste este valor para controlar a velocidade

    public Casco(String sNomeImagePNG) {
        super(sNomeImagePNG);
        movingRight = true;
        tela = Desenho.acessoATelaDoJogo();
        this.setbTransponivel(false);
        this.setMortal(true);
    }
    
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    @Override
    public void autoDesenho() {
        moveCounter++;
        if(moveCounter >= 20) {
            moveCounter = 0;
            
            Posicao nextPos = new Posicao(
                this.getPosicao().getLinha(),
                this.getPosicao().getColuna() + (movingRight ? 1 : -1)
            );

            if(Desenho.acessoATelaDoJogo().ehPosicaoValida(nextPos)) {
                this.setPosicao(nextPos.getLinha(), nextPos.getColuna());
            } else {
                movingRight = !movingRight;
            }
        }
        super.autoDesenho();
    }
    
    @Override
    public Rectangle getHitbox() {
        int cellSize = Consts.CELL_SIDE;
        int margem = 4;
        return new Rectangle(
            pPosicao.getColuna() * cellSize + margem,
            pPosicao.getLinha() * cellSize + margem,
            cellSize - 2 * margem,
            cellSize - 2 * margem
        );
    }
    
    @Override
    public Rectangle getUpHitbox() {
        // Mesma área que Goomba
        int cellSize = Consts.CELL_SIDE;
        return new Rectangle(
            pPosicao.getColuna() * cellSize,
            (pPosicao.getLinha() - 1) * cellSize,
            cellSize,
            cellSize
        );
    }
    
}