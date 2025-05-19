package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;

public class CanoBillbala extends Personagem implements Serializable{
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
            f.setPosicao(pPosicao.getLinha()-2,pPosicao.getColuna()+1);
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }

    public Personagem getBillBala() {
        return f;
    }
    
}
