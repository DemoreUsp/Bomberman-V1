package Modelos;

// Classe que define o comportamento do cano entre fases
public class Cano extends Personagem {
    public Cano(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        bMortal = false;
    }

}
