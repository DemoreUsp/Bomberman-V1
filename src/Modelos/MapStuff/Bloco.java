package Modelos.MapStuff;

import Modelos.Personagem;

// Classe que define o comportamento dos blocos (parede)
public class Bloco extends Personagem {
    public Bloco(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.setbTransponivel(false);
        this.bMortal = false;
    }
}
