
package Modelos.Entities.Villains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.Tela;
import Modelos.Entities.Villains.AuxiliarVillains.Casco;
import Modelos.Personagem;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Iterator;

public class Koopa extends Personagem implements Serializable {
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
            cellSize - 2 * margem
        );
    }

    public void autoDesenho() {
        moveCounter++;
        if (moveCounter >= 15) {
            moveCounter = 0;
            
            Posicao nextPos = new Posicao(this.getPosicao().getLinha(), 
                this.getPosicao().getColuna() + (movingRight ? 1 : -1));
            
            boolean canMove = Desenho.acessoATelaDoJogo().getCj().ehPosicaoValida(Desenho.acessoATelaDoJogo().getFaseAtual(), nextPos);
            
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
            (pPosicao.getLinha() - 1) * cellSize, // Área acima do Koopa
            cellSize,
            cellSize
        );
    }
    
    public void transformarEmCasco(Iterator<Personagem> iterator) {
        System.out.println("[DEBUG] Transformando Koopa em Casco");

        // Cria novo Casco
        Casco casco = new Casco("casco.png");
        casco.setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
        casco.setbTransponivel(false);
        casco.setMortal(true);

        // Adiciona à lista de personagens
        Desenho.acessoATelaDoJogo().getFaseAtual().getPersonagens().add(casco);

        // Remove o Koopa usando o iterator
        iterator.remove();

        System.out.println("[DEBUG] Casco adicionado na posição: " + casco.getPosicao());
    }
}
