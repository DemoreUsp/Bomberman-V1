package Modelos;

public class Bloco extends Personagem {
    public Bloco(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.setbTransponivel(false);
        this.bMortal = false;
    }
}
