package Modelos;

import Auxiliar.Posicao;
import java.util.ArrayList;

public class Fase {
    private int numero;
    private ArrayList<Personagem> personagens;
    private Heroi Heroi;
    private Posicao posicaoInicialHeroi;
    private Posicao PosicaoFinalMario;
    
    public Fase(int numero, Posicao posicaoHeroi, Posicao PFM) {
        this.numero = numero;
        this.personagens = new ArrayList<>();
        this.posicaoInicialHeroi = posicaoHeroi;
        this.PosicaoFinalMario = PFM;
    }
    
    public void adicionarPersonagem(Personagem p) {
        personagens.add(p);
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

    public Posicao getPosicaoInicialHeroi() {
        return posicaoInicialHeroi;
    }

    public int getNumero() {
        return numero;
    }
    
    public Posicao getPosicaoFinal() {
        return PosicaoFinalMario;
    }
}
