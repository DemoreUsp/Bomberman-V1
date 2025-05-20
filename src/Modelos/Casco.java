package Modelos;

import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public class Casco extends Personagem implements Serializable{
            
    public Casco(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        if(!this.moveLeft())
            Desenho.acessoATelaDoJogo().removePersonagem(this);
    }
    
}