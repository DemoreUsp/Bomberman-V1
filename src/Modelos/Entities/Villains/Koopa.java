
package Modelos.Entities.Villains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelos.Entities.Villains.AuxiliarVillains.Casco;
import Modelos.Personagem;
import java.awt.Rectangle;
import java.util.Iterator;

// Classe que define comportamento/movimentação do koopa
public class Koopa extends Personagem {
    private boolean movingRight;
    private int moveCounter;
    private boolean transformado = false;

    public Koopa(String sNomeImagePNG) {
        super(sNomeImagePNG);
        movingRight = false;
        moveCounter = 0;
        this.setMortal(true);
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
                cellSize - 2 * margem);
    }

    public void autoDesenho() {
        moveCounter++;
        if (moveCounter >= 15) {
            moveCounter = 0;

            Posicao nextPos = new Posicao(this.getPosicao().getLinha(),
                    this.getPosicao().getColuna() + (movingRight ? 1 : -1));

            boolean canMove = Desenho.acessoATelaDoJogo().getCj()
                    .ehPosicaoValida(Desenho.acessoATelaDoJogo().getFaseAtual(), nextPos);

            if (canMove) {
                this.setPosicao(nextPos.getLinha(), nextPos.getColuna());
            } else {
                movingRight = !movingRight;
            }
        }
        super.autoDesenho();
    }

    public void marcarComoTransformado() {
        this.transformado = true;
    }

    public boolean foiTransformado() {
        return transformado;
    }

    @Override
    public Rectangle getUpHitbox() {
        int cellSize = Consts.CELL_SIDE;
        return new Rectangle(
                pPosicao.getColuna() * cellSize,
                (pPosicao.getLinha() - 1) * cellSize, 
                cellSize,
                cellSize);
    }

    // Método que, quando o koopa morre, vira um casco
    public void transformarEmCasco(Iterator<Personagem> iterator) {

        Casco casco = new Casco("casco.png");
        casco.setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
        casco.setbTransponivel(false);
        casco.setMortal(true);

        Desenho.acessoATelaDoJogo().getFaseAtual().getPersonagens().add(casco);

        iterator.remove();
    }
}
