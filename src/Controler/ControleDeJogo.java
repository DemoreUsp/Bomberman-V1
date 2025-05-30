package Controler;

import Modelos.Personagem;
import Modelos.Entities.Heroes.Heroi;
import Controler.Fases.Fase;
import Auxiliar.Posicao;
import Modelos.Entities.Villains.CanoBillbala;
import Modelos.Entities.Villains.Bowser;
import Modelos.Entities.Villains.AuxiliarVillains.Casco;
import Modelos.Entities.Villains.Koopa;
import java.util.ArrayList;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

// Classe mantida bem próxima ao protótipo
public class ControleDeJogo implements Serializable {

    public void desenhaTudo(Fase umaFase, Heroi hero, int cameraX, int cameraY, Graphics g) {
        hero.autoDesenho();
        for (int i = 0; i < umaFase.getMapStuff().size(); i++) {
            umaFase.getMapStuff().get(i).autoDesenho();
        }
        if (umaFase.getPersonagens() == null || umaFase.getPersonagens().isEmpty())
            return;
        List<Personagem> copia = new ArrayList<>(umaFase.getPersonagens());
        for (Personagem p : copia) {
            p.autoDesenho();
        }
    }

    public void processaTudo(Fase umaFase, Heroi hero) {
        List<Personagem> paraAdicionar = new ArrayList<>();

        // Usa Iterator para remoção segura
        Iterator<Personagem> iterator = umaFase.getPersonagens().iterator();
        while (iterator.hasNext()) {
            Personagem p = iterator.next();

            // Verifica colisão por cima
            if (p.getUpHitbox() != null && hero.getHitbox() != null &&
                    p.getUpHitbox().intersects(hero.getHitbox())) {

                // Transforma koopa em casco
                if (p instanceof Koopa) {
                    Casco casco = new Casco("casco.png");
                    casco.setPosicao(p.getPosicao().getLinha(), p.getPosicao().getColuna());
                    paraAdicionar.add(casco);
                    iterator.remove(); 
                    hero.moveUp();
                    continue; 
                }
                if (p instanceof Bowser) {
                    if (p.getVidas() > 1) {
                        p.setVidas(p.getVidas() - 1);
                        hero.moveUp();
                        continue;
                    }
                }
                if (p instanceof CanoBillbala)
                    continue;
                iterator.remove();
                hero.moveUp();
            }

            // Verifica colisão frontal
            if (p.getHitbox() != null && hero.getHitbox() != null &&
                    p.getHitbox().intersects(hero.getHitbox())) {

                if (p.isbMortal()) {
                    hero.setVidas(hero.getVidas() - 1);
                    hero.moveUp();
                }
            }
        }

        // Adiciona novos elementos após a iteração
        umaFase.getPersonagens().addAll(paraAdicionar);
    }

    /*
     * Retorna true se a posicao p é válida para Hero com relacao a todos os
     * personagens no array
     */
    public boolean ehPosicaoValida(Fase umaFase, Posicao p) {
        for (Personagem bloco : umaFase.getMapStuff()) {
            if (bloco.getPosicao().igual(p) && !bloco.isbTransponivel()) {
                return false;
            }
        }

        // Verifica colisão com outros personagens
        for (Personagem personagem : umaFase.getPersonagens()) {
            if (personagem.getPosicao().igual(p) && !personagem.isbTransponivel()) {
                return false;
            }
        }

        return true;
    }
}
