package Controler.Fases;

import Controler.Fases.Fase;
import java.util.ArrayList;
import Modelos.Entities.Heroes.Heroi;
import Auxiliar.Posicao;
import Auxiliar.Desenho;
import Modelos.Personagem;
import Modelos.MapStuff.Nuvem;
import Controler.Fases.FasesHandler;
import Controler.Fases.Fase;
import Modelos.MapStuff.Bloco;
import Modelos.Entities.Villains.CanoBillbala;
import Modelos.Entities.Heroes.Heroi;
import Modelos.Entities.Villains.Koopa;
import Modelos.Entities.Villains.Goomba;
import Modelos.Entities.Villains.Bowser;
/**
 *
 * @author Julean
 */
public class FasesHandler {
    private ArrayList<Fase> fases = new ArrayList<>();
    private int faseAtualIndex = 0;
    private Fase faseAtual;
    public Fase getFase(int index) {
        return fases.get(index);
    }
    public Fase getFaseAtual() {
        return faseAtual;
    }
    public int getFaseAtualIndex() {
        return faseAtualIndex;
    }
    private void inicializarFase1(Heroi hero) {
        Posicao inicioFase1 = new Posicao(9, 3);
        Posicao finalFase1 = new Posicao(9, 55);
        Fase fase1 = new Fase(0, inicioFase1, finalFase1);
        
        for(int col = 0; col < 65; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(10, col);
            fase1.adicionarMapStuff(blococol);
            Bloco grama = new Bloco("terraComGrama.png");
            grama.setPosicao(10, col);
            fase1.adicionarMapStuff(grama);
            for(int lin = 11; lin < 18; lin++) {
                Bloco blocolin = new Bloco("terraMario.png");
                blocolin.setPosicao(lin, col);
                fase1.adicionarMapStuff(blocolin);
            }
        }
        
        // Adicionar inimigos
        
        // Plataforma elevada para Goomba (colunas 5-10, linha 8)
        for(int col = 5; col <= 10; col++) {
            Bloco plataforma = new Bloco("bloco.png");
            plataforma.setPosicao(8, col);
            fase1.adicionarMapStuff(plataforma);
        }
        Bloco parede1 = new Bloco("bloco.png");
        parede1.setPosicao(7, 5);
        fase1.adicionarMapStuff(parede1);
        Bloco parede2 = new Bloco("bloco.png");
        parede2.setPosicao(7, 10);
        fase1.adicionarMapStuff(parede2);
    
        // Goomba na plataforma
        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(7, 7); // Acima da plataforma
        fase1.adicionarPersonagem(goomba1);
    
        // Plataforma elevada para Koopa (colunas 15-20, linha 8)
        for(int col = 15; col <= 20; col++) {
            Bloco plataforma = new Bloco("bloco.png");
            plataforma.setPosicao(8, col);
            fase1.adicionarMapStuff(plataforma);
        }
        
        Bloco parede3 = new Bloco("bloco.png");
        parede3.setPosicao(7, 15);
        fase1.adicionarMapStuff(parede3);
        
        Bloco parede4 = new Bloco("bloco.png");
        parede4.setPosicao(7, 20);
        fase1.adicionarMapStuff(parede4);
    
        // Koopa na plataforma
        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(7, 18); // Acima da plataforma
        fase1.adicionarPersonagem(koopa1);
        
        CanoBillbala inimigo1 = new CanoBillbala("");
        inimigo1.setPosicao(9, 22);
        fase1.adicionarPersonagem(inimigo1);
        
        fases.add(fase1);
        faseAtual = fase1;
        faseAtualIndex = 0;
        carregarFase(getFaseAtual(), hero);
    }
    private void inicializarFase2(Heroi hero) {
        Posicao inicioFase2 = new Posicao(9, 3);
        Posicao finalFase2 = new Posicao(9, 22);
        Fase fase2 = new Fase(1, inicioFase2, finalFase2);
        for(int col = 0; col < 50; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(0, col);
            fase2.adicionarMapStuff(blococol);
            Bloco grama = new Bloco("terraComGrama.png");
            grama.setPosicao(10, col);
            fase2.adicionarMapStuff(grama);
            for(int lin = 11; lin < 18; lin++) {
                Bloco blocolin = new Bloco("terraMario.png");
                blocolin.setPosicao(lin, col);
                fase2.adicionarMapStuff(blocolin);
            }
        }
        
        Bowser inimigo2 = new Bowser("bowserL.png");
        inimigo2.setVidas(5);
        inimigo2.setMortal(true);
        inimigo2.setbTransponivel(true);
        inimigo2.setPosicao(8, 20);
        fase2.adicionarPersonagem(inimigo2);
        
        fases.add(fase2);
        faseAtualIndex = 1;
        faseAtual = fase2;
        carregarFase(getFaseAtual(), hero);
    }
    private void inicializarFase3(Heroi hero) {
        Posicao inicioFase3 = new Posicao(0, 0);
        Posicao finalFase3 = new Posicao(0, 0);
        Fase fase3 = new Fase(2, inicioFase3, finalFase3);
        //adicionar mobs e mapstuff
        fases.add(fase3);
        faseAtualIndex = 2;
        faseAtual = fase3;
        carregarFase(getFaseAtual(), hero);
    }
    private void inicializarFase4(Heroi hero) {
        Posicao inicioFase4 = new Posicao(0, 0);
        Posicao finalFase4 = new Posicao(0, 0);
        Fase fase4 = new Fase(3, inicioFase4, finalFase4);
        //adicionar mobs e mapstuff
        fases.add(fase4);
        faseAtualIndex = 3;
        faseAtual = fase4;
        carregarFase(getFaseAtual(), hero);
    }
    private void inicializarFase5(Heroi hero) {
        Posicao inicioFase5 = new Posicao(0, 0);
        Posicao finalFase5 = new Posicao(0, 0);
        Fase fase5 = new Fase(4, inicioFase5, finalFase5);
        //adicionar mobs e mapstuff
        fases.add(fase5);
        faseAtualIndex = 4;
        faseAtual = fase5;
        carregarFase(getFaseAtual(), hero);
    }
    private void carregarFase(Fase f, Heroi hero) {
        // Posiciona os Personagens onde estavam
        for(Personagem p: f.getPersonagens()) {
            p.setPosicao(p.getPosicao().getLinha(), p.getPosicao().getColuna());
        }
        
        if(f.getHeroi() == null) {
            hero.setVidas(2);
            hero.setPosicao(
                f.getPosicaoInicialHeroi().getLinha(),
                f.getPosicaoInicialHeroi().getColuna()
            );
            f.adicionarHeroi(hero);
        } else {
        hero.setPosicao(
            f.getPosicaoHeroi().getLinha(),
            f.getPosicaoHeroi().getColuna()
            );
        }
    }
    public void inicializarFaseIndex(int index, Heroi hero) {
        switch(index) {
            case 0:
                inicializarFase1(hero);
                break;
            case 1: 
                inicializarFase2(hero);
                break;
            case 2:
                inicializarFase3(hero);
                break;
            case 3:
                inicializarFase4(hero);
                break;
            case 4:
                inicializarFase5(hero);
                break;
        }
    }
    public void carregarFaseLoaded(Fase f) {
        fases.add(f);
        faseAtual = f;
        faseAtualIndex = faseAtual.getNumero();
        f.getNumero();
        carregarFase(f, f.getHeroi());
    }
    
    public void proximaFase() {
        if(faseAtual.getNumero() < 4) {
            faseAtualIndex++;
            Heroi temp = getFaseAtual().getHeroi();
            inicializarFaseIndex(faseAtualIndex, getFaseAtual().getHeroi());
            carregarFase(getFaseAtual(), temp);
        } else {
            System.out.println("Parabens"); //adicionar tela de fim de jogo
        }
    }
}
