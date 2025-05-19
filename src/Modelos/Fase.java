package Modelos;

import Auxiliar.Posicao;
import java.util.ArrayList;

public class Fase {
    private int numero;
    private ArrayList<Personagem> mapStuff;
    private ArrayList<Personagem> personagens;
    private Heroi Heroi;
    private Posicao posicaoInicialHeroi;
    private Posicao PosicaoFinalHeroi;
    
    public Fase(int numero, Posicao posicaoHeroi, Posicao PFM) {
        this.numero = numero;
        this.personagens = new ArrayList<>();
        this.mapStuff = new ArrayList<>();
        this.posicaoInicialHeroi = posicaoHeroi;
        this.PosicaoFinalHeroi = PFM;
    }
    
    public void adicionarPersonagem(Personagem p) {
        personagens.add(p);
    }
    
    public void adicionarMapStuff(Personagem p) {
        mapStuff.add(p);
    }
    
    public void adicionarHeroi(Heroi p) {
        Heroi = p;
    }
    
    public Heroi getHeroi() {
        return this.Heroi;
    }

    public ArrayList<Personagem> getPersonagens() {
        return this.personagens;
    }
    
    public ArrayList<Personagem> getMapStuff() {
        return this.mapStuff;
    }

    public Posicao getPosicaoInicialHeroi() {
        return posicaoInicialHeroi;
    }

    public int getNumero() {
        return numero;
    }
    
    public Posicao getPosicaoFinal() {
        return PosicaoFinalHeroi;
    }
}
