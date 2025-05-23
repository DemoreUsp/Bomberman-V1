package Controler;

import Modelos.Personagem;
import Modelos.Heroi;
import Modelos.Fase;
import Auxiliar.Posicao;
import Modelos.Billbala;
import Modelos.CanoBillbala;
import Modelos.Bowser;
import Modelos.Casco;
import Modelos.Goomba;
import Modelos.Koopa;
import java.util.ArrayList;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

public class ControleDeJogo {
    
    public void desenhaTudo(Fase umaFase, Heroi hero, int cameraX, int cameraY, Graphics g) {
        hero.autoDesenho();
        hero.drawHitbox(cameraX, cameraY, g);
        for (int i = 0; i < umaFase.getMapStuff().size(); i++) {
            umaFase.getMapStuff().get(i).autoDesenho();
        }
        if(umaFase.getPersonagens() == null || umaFase.getPersonagens().isEmpty()) return;
        List<Personagem> copia = new ArrayList<>(umaFase.getPersonagens());
        for(Personagem p : copia) {
            p.autoDesenho();
            p.drawHitbox(cameraX, cameraY, g);
        }
    }
    
    public void processaTudo(Fase umaFase, Heroi hero) {
        List<Personagem> paraAdicionar = new ArrayList<>();

        // Usar Iterator para remoção segura
        Iterator<Personagem> iterator = umaFase.getPersonagens().iterator();
        while(iterator.hasNext()) {
            Personagem p = iterator.next();

            // 1. Primeiro verifica colisão por CIMA
            if(p.getUpHitbox() != null && hero.getHitbox() != null && 
               p.getUpHitbox().intersects(hero.getHitbox())) {

                // Koopa -> Transformar em Casco
                if(p instanceof Koopa) {
                    Casco casco = new Casco("casco.png");
                    casco.setPosicao(p.getPosicao().getLinha(), p.getPosicao().getColuna());
                    paraAdicionar.add(casco);
                    iterator.remove(); // Remove Koopa com segurança
                    hero.moveUp();
                    continue; // Pula para próxima iteração
                }
                if(p instanceof Bowser) {
                   if(p.getVidas() > 1) {
                       p.setVidas(p.getVidas()-1);
                       hero.moveUp();
                       continue;
                   }
                }
                if(p instanceof CanoBillbala) continue;
               iterator.remove();
               hero.moveUp();
            }

            // 2. Depois verifica colisão FRONTAL
            if(p.getHitbox() != null && hero.getHitbox() != null && 
               p.getHitbox().intersects(hero.getHitbox())) {

                if(p.isbMortal()) {
                    hero.setVidas(hero.getVidas() - 1);
                    hero.moveUp();
                }
            }
        }

        // Adiciona novos elementos após a iteração
        umaFase.getPersonagens().addAll(paraAdicionar);

        // 3. Atualiza movimento de todos os personagens
        //for(Personagem p : umaFase.getPersonagens()) {
        //    if(p instanceof Goomba || p instanceof Koopa || p instanceof Casco) {
        //        p.autoDesenho();
        //    }
        //}
    }
    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    // Em ControleDeJogo.java
    public boolean ehPosicaoValida(Fase umaFase, Posicao p) {
        //System.out.println("[DEBUG] Verificando posição: " + p.getLinha() + "," + p.getColuna());
        // Verificar colisão com o mapa
        for (Personagem bloco : umaFase.getMapStuff()) {
            if (bloco.getPosicao().igual(p) && !bloco.isbTransponivel()) {
                return false;
            }
        }
    
        // Verificar colisão com outros personagens
        for (Personagem personagem : umaFase.getPersonagens()) {
            if (personagem.getPosicao().igual(p) && !personagem.isbTransponivel()) {
                return false;
            }
        }
        
        return true;
    }
}
