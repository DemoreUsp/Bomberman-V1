package Modelos.Entities.Villains.AuxiliarVillains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelos.Personagem;
import java.awt.Rectangle;

// Classe que transforma o koopa em casco ao morrer;
// Define o comportamento do casco
public class Casco extends Personagem {
    private boolean movingRight = true;
    private int moveCounter = 0;
    private int moveDelay = 0; 
    private static final int MOVE_DELAY_MAX = 10;

    public Casco(String sNomeImagePNG) {
        super(sNomeImagePNG);
        movingRight = true;
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
        int cellSize = Consts.CELL_SIDE;
        return new Rectangle(
            pPosicao.getColuna() * cellSize,
            (pPosicao.getLinha() - 1) * cellSize,
            cellSize,
            cellSize
        );
    }
    
}