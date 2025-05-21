package Controler;

import Modelos.Personagem;
import Modelos.Heroi;
import Modelos.Fase;
import Auxiliar.Posicao;
import Modelos.Billbala;
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
        for (int i = 0; i < umaFase.getPersonagens().size(); i++) {
            umaFase.getPersonagens().get(i).autoDesenho();
            umaFase.getPersonagens().get(i).drawHitbox(cameraX, cameraY, g);
        }
        for (int i = 0; i < umaFase.getMapStuff().size(); i++) {
            umaFase.getMapStuff().get(i).autoDesenho();
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
                // Goomba -> Remover
                else if(p instanceof Goomba) {
                    iterator.remove();
                    hero.moveUp();
                    continue;
                }
                // Billbala -> remover
                else if(p instanceof Billbala) {
                    iterator.remove();
                    hero.moveUp();
                    continue;
                }
                
                // Dentro do bloco de colisão por CIMA
                else if (p instanceof Casco) {
                    iterator.remove(); // Remove o Casco
                    hero.moveUp();
                    continue;
                }
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
        for(Personagem p : umaFase.getPersonagens()) {
            if(p instanceof Goomba || p instanceof Koopa || p instanceof Casco) {
                p.autoDesenho();
            }
        }
    }
    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    // Em ControleDeJogo.java
    public boolean ehPosicaoValida(Fase umaFase, Posicao p) {
        System.out.println("[DEBUG] Verificando posição: " + p.getLinha() + "," + p.getColuna());
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
