package Modelos;

import auxiliar.Posicao;
import java.util.ArrayList;

public class Fase {
    private int numero;
    private ArrayList<Personagem> personagens;
    private Posicao posicaoInicialHeroi;
    
    public Fase(int numero, Posicao posicaoHeroi) {
        this.numero = numero;
        this.personagens = new ArrayList<>();
        this.posicaoInicialHeroi = posicaoHeroi;
    }
    
    public void adicionarPersonagem(Personagem p) {
        personagens.add(p);
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
}
