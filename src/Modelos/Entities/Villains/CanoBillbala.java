package Modelos.Entities.Villains;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.Tela;
import Modelos.Entities.Villains.AuxiliarVillains.Billbala;
import Modelos.Personagem;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public class CanoBillbala extends Personagem {
    private int iContaIntervalos;
    private Personagem f;
    
    public CanoBillbala(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        bMortal = false;
        this.iContaIntervalos = 0;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Billbala f = new Billbala("billbala.png");
            f.setPosicao(pPosicao.getLinha(),pPosicao.getColuna()-1);
            f.setMortal(true);
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }

    public Personagem getBillBala() {
        return f;
    }
    
    @Override
    public Rectangle getHitbox() {
        int cellSize = Consts.CELL_SIDE;
        return new Rectangle(
            pPosicao.getColuna() * cellSize,
            pPosicao.getLinha() * cellSize,
            cellSize,
            2 * cellSize
        );
    }
}
