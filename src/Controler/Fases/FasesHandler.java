package Controler.Fases;

import java.util.ArrayList;
import Modelos.Entities.Heroes.Heroi;
import Auxiliar.Posicao;
import Auxiliar.Desenho;
import Modelos.Personagem;
import Modelos.MapStuff.Bloco;
import Modelos.Entities.Villains.CanoBillbala;
import Modelos.Entities.Villains.Koopa;
import Modelos.Entities.Villains.Goomba;
import Modelos.Entities.Villains.Bowser;
import Controler.MenuFinal;

// Classe responsável por modelar cada fase
public class FasesHandler {
    private final ArrayList<Fase> fases = new ArrayList<>();
    private int faseAtualIndex = 0;
    private Fase faseAtual;
    Posicao inicio = new Posicao(6, 1);
    Posicao fim = new Posicao(8, 55);
    MenuFinal menu = new MenuFinal();

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
        Fase fase = new Fase(0, inicio, fim);

        gerarTerreno(fase);

        gerarPlataforma(fase, 5, 10, 10); 
        
        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(9, 8); 
        fase.adicionarPersonagem(goomba1);

        gerarPlataforma(fase, 15, 20, 10);
        
        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(9, 18); 
        fase.adicionarPersonagem(koopa1);

        gerarPlataforma(fase, 25, 30, 10);
        
        Goomba goomba2 = new Goomba("goomba.png");
        goomba2.setPosicao(9, 28); 
        fase.adicionarPersonagem(goomba2);

        gerarPlataforma(fase, 35, 40, 10);

        Koopa koopa2 = new Koopa("koopa.png");
        koopa2.setPosicao(9, 38); 
        fase.adicionarPersonagem(koopa2);

        fases.add(fase);
        faseAtual = fase;
        faseAtualIndex = 0;
        carregarFase(getFaseAtual(), hero);
    }

    private void inicializarFase2(Heroi hero) {
        Fase fase = new Fase(1, inicio, fim);

        gerarTerreno(fase);

        gerarPlataforma(fase, 5, 10, 8); 

        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(7, 7); 
        fase.adicionarPersonagem(goomba1);

        gerarPlataforma(fase, 15, 20, 8); 

        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(7, 18); 
        fase.adicionarPersonagem(koopa1);

        gerarPlataforma(fase, 35, 40, 8); 

        Goomba goomba2 = new Goomba("goomba.png");
        goomba2.setPosicao(7, 38); 
        fase.adicionarPersonagem(goomba2);

        gerarPlataforma(fase, 45, 50, 8); 

        Koopa koopa2 = new Koopa("koopa.png");
        koopa2.setPosicao(7, 48); 
        fase.adicionarPersonagem(koopa2);

        CanoBillbala inimigo2 = new CanoBillbala("Cano billBala.png");
        inimigo2.setPosicao(9, 52);
        fase.adicionarPersonagem(inimigo2);

        fases.add(fase);
        faseAtual = fase;
        faseAtualIndex = 1;
        carregarFase(getFaseAtual(), hero);
    }

    private void inicializarFase3(Heroi hero) {
        Fase fase = new Fase(2, inicio, fim);

        gerarTerreno(fase);

        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(9, 7);
        fase.adicionarPersonagem(goomba1);

        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(9, 18);
        fase.adicionarPersonagem(koopa1);

        CanoBillbala inimigo1 = new CanoBillbala("Cano billBala.png");
        inimigo1.setPosicao(8, 22);
        fase.adicionarPersonagem(inimigo1);
        Bloco bloco1 = new Bloco("bloco.png");
        bloco1.setPosicao(9, 22);
        fase.adicionarMapStuff(bloco1);

        Goomba goomba2 = new Goomba("goomba.png");
        goomba2.setPosicao(9, 28);
        fase.adicionarPersonagem(goomba2);

        Koopa koopa2 = new Koopa("koopa.png");
        koopa2.setPosicao(9, 40);
        fase.adicionarPersonagem(koopa2);

        CanoBillbala inimigo2 = new CanoBillbala("Cano billBala.png");
        inimigo2.setPosicao(9, 47);
        fase.adicionarPersonagem(inimigo2);

        fases.add(fase);
        faseAtual = fase;
        faseAtualIndex = 2;
        carregarFase(getFaseAtual(), hero);
    }

    private void inicializarFase4(Heroi hero) {
        Fase fase = new Fase(3, inicio, fim);

        gerarTerreno(fase);

        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(9, 10);
        fase.adicionarPersonagem(goomba1);

        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(9, 20);
        fase.adicionarPersonagem(koopa1);

        gerarPlataforma(fase, 28, 33, 10);
        Bloco bloco2 = new Bloco("bloco.png");
        bloco2.setPosicao(8, 33);
        fase.adicionarMapStuff(bloco2);
        
        Goomba goomba2 = new Goomba("goomba.png");
        goomba2.setPosicao(9, 31);
        fase.adicionarPersonagem(goomba2);
        
        gerarPlataforma(fase, 35, 50, 7); 

        Koopa koopa2 = new Koopa("koopa.png");
        koopa2.setPosicao(6, 40); 
        fase.adicionarPersonagem(koopa2);
        
        Goomba goomba3 = new Goomba("goomba.png");
        goomba3.setPosicao(6, 45);
        fase.adicionarPersonagem(goomba3);

        CanoBillbala inimigo1 = new CanoBillbala("Cano billBala.png");
        inimigo1.setPosicao(9, 27);
        fase.adicionarPersonagem(inimigo1);

        Bloco bloco1 = new Bloco("bloco.png");
        bloco1.setPosicao(9, 49);
        fase.adicionarMapStuff(bloco1);
        
        CanoBillbala inimigo2 = new CanoBillbala("Cano billBala.png");
        inimigo2.setPosicao(8, 49);
        fase.adicionarPersonagem(inimigo2);

        fases.add(fase);
        faseAtual = fase;
        faseAtualIndex = 3;
        carregarFase(getFaseAtual(), hero);
    }

    private void inicializarFase5(Heroi hero) {
        Fase fase = new Fase(4, inicio, fim);

        gerarTerreno(fase);

        gerarPlataforma(fase, 7, 35, 10);

        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(9, 13);
        fase.adicionarPersonagem(goomba1);

        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(9, 20);
        fase.adicionarPersonagem(koopa1);

        Goomba goomba2 = new Goomba("goomba.png");
        goomba2.setPosicao(9, 28);
        fase.adicionarPersonagem(goomba2);

        CanoBillbala inimigo1 = new CanoBillbala("Cano billBala.png");
        inimigo1.setPosicao(9, 34);
        fase.adicionarPersonagem(inimigo1);

        Bowser bowser = new Bowser("bowser.png");
        bowser.setVidas(5);
        bowser.setMortal(true);
        bowser.setbTransponivel(true);
        bowser.setPosicao(8, 43);
        fase.adicionarPersonagem(bowser);

        fases.add(fase);
        faseAtual = fase;
        faseAtualIndex = 4;
        carregarFase(getFaseAtual(), hero);
    }

    private void carregarFase(Fase f, Heroi hero) {
        for (Personagem p : f.getPersonagens()) {
            p.setPosicao(p.getPosicao().getLinha(), p.getPosicao().getColuna());
        }

        if (f.getHeroi() == null) {
            hero.setVidas(1);
            hero.setPosicao(
                    f.getPosicaoInicialHeroi().getLinha(),
                    f.getPosicaoInicialHeroi().getColuna());
            f.adicionarHeroi(hero);
        } else {
            hero.setPosicao(
                    f.getPosicaoHeroi().getLinha(),
                    f.getPosicaoHeroi().getColuna());
        }
    }

    public void inicializarFaseIndex(int index, Heroi hero) {
        switch (index) {
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
        if (faseAtual.getNumero() < 4) {
            faseAtualIndex++;
            Heroi temp = getFaseAtual().getHeroi();
            inicializarFaseIndex(faseAtualIndex, getFaseAtual().getHeroi());
            carregarFase(getFaseAtual(), temp);
        } else {
            menu.desenharParabens(Desenho.acessoATelaDoJogo().getGraphicsBuffer());
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println(e);
            }
            System.exit(0);
        }
    }

    public void gerarTerreno(Fase essaFase) {
        for (int col = 0; col < 63; col++) {
            Bloco bloco = new Bloco("bloco.png");
            bloco.setPosicao(10, col);
            essaFase.adicionarMapStuff(bloco);
            Bloco grama = new Bloco("terraComGrama.png");
            grama.setPosicao(10, col);
            essaFase.adicionarMapStuff(grama);
            for (int lin = 11; lin < 18; lin++) {
                Bloco terra = new Bloco("terraMario.png");
                terra.setPosicao(lin, col);
                essaFase.adicionarMapStuff(terra);
            }
        }

        Bloco cano1 = new Bloco("cano.png");
        cano1.setPosicao(9, 55);
        essaFase.adicionarMapStuff(cano1);

        for (int i = 10; i < 18; i++) {
            Bloco cano2 = new Bloco("cano2.png");
            cano2.setPosicao(i, 55);
            essaFase.adicionarMapStuff(cano2);
        }

        Bloco cano3 = new Bloco("cano.png");
        cano3.setPosicao(0, 1);
        essaFase.adicionarMapStuff(cano3);
        for (int i = 1; i < 5; i++) {
            Bloco cano4 = new Bloco("cano2.png");
            cano4.setPosicao(i, 1);
            essaFase.adicionarMapStuff(cano4);
        }
    }

    public void gerarPlataforma(Fase essaFase, int ini, int fim, int altura) {
        for (int col = ini; col <= fim; col++) {
            Bloco plataforma = new Bloco("bloco.png");
            plataforma.setPosicao(altura, col);
            essaFase.adicionarMapStuff(plataforma);
        }
        Bloco parede1 = new Bloco("bloco.png");
        parede1.setPosicao(altura - 1, ini);
        essaFase.adicionarMapStuff(parede1);
        Bloco parede2 = new Bloco("bloco.png");
        parede2.setPosicao(altura - 1, fim);
        essaFase.adicionarMapStuff(parede2);
    }

}