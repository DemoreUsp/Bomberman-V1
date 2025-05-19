package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Rectangle;

public abstract class Personagem implements Serializable {

    protected ImageIcon iImage;
    protected Posicao pPosicao;
    protected boolean bTransponivel; /*Pode passar por cima?*/
    protected boolean bMortal;
    private int gravidade = 2;/*Se encostar, morre?*/
    protected int vidas;

    public boolean isbMortal() {
        return bMortal;
    }

    protected Personagem(String sNomeImagePNG) {
        this.pPosicao = new Posicao(1, 1);
        this.bTransponivel = true;
        this.bMortal = false;
        this.vidas = 1;
        carregarImagem(sNomeImagePNG);
    }
    
    protected void carregarImagem(String sNomeImagePNG) {
    try {
        iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
        Image img = iImage.getImage();
        BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
}
    
    public Posicao getPosicao() {
        /*TODO: Retirar este método para que objetos externos nao possam operar
         diretamente sobre a posição do Personagem*/
        return pPosicao;
    }
    
    public void setMortal(boolean b) {
        bMortal = b;
    }
    
    public void attVidas(int qt) {
        vidas += qt;
    }
    
    public int getVidas() {
        return vidas;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho(){
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());        
    }
    
    public Rectangle getHitbox() {
        int x = this.pPosicao.getColuna() * Consts.CELL_SIDE;
        int y = this.pPosicao.getLinha() * Consts.CELL_SIDE;
        return new Rectangle(x, y, Consts.CELL_SIDE, Consts.CELL_SIDE);
    }
    
    public void atualizarFisica() {
        int velocidadeY = gravidade;
        this.setPosicao(this.pPosicao.getLinha() + velocidadeY, this.pPosicao.getColuna());
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    public boolean moveUp(int forcaPulo) {
        return this.pPosicao.moveUp(forcaPulo);
    }

    public boolean moveDown() {
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        return this.pPosicao.moveLeft();
    }
}
