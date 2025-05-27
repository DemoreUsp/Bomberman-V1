package Modelos.Entities.Villains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.Tela;
import Modelos.Personagem;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Goomba extends Personagem implements Serializable {

    private boolean movingRight;
    private int moveCounter;

    public Goomba(String sNomeImagePNG) {
        super(sNomeImagePNG);
        movingRight = false; // Começa movendo para a esquerda
        moveCounter = 0;
        this.setMortal(true); // Correção aqui
    }
    
    @Override
    public Rectangle getHitbox() {
        int cellSize = Consts.CELL_SIDE;
        int coluna = this.pPosicao.getColuna();
        int linha = this.pPosicao.getLinha();
        int margem = 4;
        return new Rectangle(
            coluna * cellSize + margem,
            linha * cellSize + margem,
            cellSize - 2 * margem,
            cellSize - 2 * margem
        );
    }
    
    @Override
    public Rectangle getUpHitbox() {
        int cellSize = Consts.CELL_SIDE;
        return new Rectangle(
            pPosicao.getColuna() * cellSize,
            (pPosicao.getLinha() - 1) * cellSize, // Área acima do Goomba
            cellSize,
            cellSize
        );
    }

    @Override
    public boolean isbMortal() {
        return true; // Mata o Mario ao toque frontal
    }

    public void autoDesenho() {
        moveCounter++;
        if (moveCounter >= 15) {
            moveCounter = 0;
            
            // Verifica próxima posição
            Posicao nextPos = new Posicao(this.getPosicao().getLinha(), 
                this.getPosicao().getColuna() + (movingRight ? 1 : -1));
            
            // Verifica colisão com o mapa
            boolean canMove = Desenho.acessoATelaDoJogo().getCj().ehPosicaoValida(Desenho.acessoATelaDoJogo().getFaseAtual(), nextPos);
            
            if (canMove) {
                this.setPosicao(nextPos.getLinha(), nextPos.getColuna());
            } else {
                movingRight = !movingRight; // Inverte direção
            }
        }
        super.autoDesenho();
    }
    
}
