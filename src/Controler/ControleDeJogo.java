package Controler;

import Modelos.Personagem;
import Modelos.Heroi;
import Modelos.Fase;
import Auxiliar.Posicao;
import java.util.ArrayList;

public class ControleDeJogo {
    
    public void desenhaTudo(Fase umaFase, Heroi hero) {
        hero.autoDesenho();
        for (int i = 0; i < umaFase.getPersonagens().size(); i++) {
            umaFase.getPersonagens().get(i).autoDesenho();
        }
        for (int i = 0; i < umaFase.getMapStuff().size(); i++) {
            umaFase.getMapStuff().get(i).autoDesenho();
        }
    }
    
    public void processaTudo(Fase umaFase, Heroi hero) {
        Personagem pIesimoPersonagem;
        for (int i = 0; i < umaFase.getPersonagens().size(); i++) {
            pIesimoPersonagem = umaFase.getPersonagens().get(i);
            if (umaFase.getPersonagens().get(i).getPosicao().getLinha() == hero.getPosicao().getLinha() &&
                    umaFase.getPersonagens().get(i).getPosicao().getColuna() == hero.getPosicao().getColuna()) {
                if (pIesimoPersonagem.isbTransponivel()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                    if (pIesimoPersonagem.isbMortal())
                        if(pIesimoPersonagem.getVidas() < 1)
                            umaFase.getPersonagens().remove(pIesimoPersonagem);
                        else
                            umaFase.getPersonagens().get(i).attVidas(-1);
                }
            }
        }
        //for (int i = 1; i < umaFase.size(); i++) {
            //pIesimoPersonagem = umaFase.get(i);
            //if (pIesimoPersonagem instanceof Chaser) {
            //    ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
            //}
        //}
    }

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(Fase umaFase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 0; i < umaFase.getPersonagens().size(); i++) {
            pIesimoPersonagem = umaFase.getPersonagens().get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        
        Personagem dIesimoPersonagem;
        for (int i = 0;i < umaFase.getMapStuff().size(); i++) {
            dIesimoPersonagem = umaFase.getMapStuff().get(i);
            if (!dIesimoPersonagem.isbTransponivel()) {
                if (dIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
}
