package Modelos;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Color;

// Classe que define o comportamento geral dos personagens
public abstract class Personagem implements Serializable {
    protected ImageIcon iImage;
    protected Posicao pPosicao;
    protected boolean bTransponivel; 
    protected boolean bMortal;
    private final int gravidade = 1;
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

    public void setImagem(String sNomeImagePNG) {
        carregarImagem(sNomeImagePNG);
    }

    public Posicao getPosicao() {
        return pPosicao;
    }

    public void setMortal(boolean b) {
        bMortal = b;
    }

    public void setVidas(int qt) {
        vidas = qt;
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

    public void autoDesenho() {
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());
    }

    public Rectangle getHitbox() {
        return null;
    }

    public Rectangle getUpHitbox() {
        Rectangle hitbox = new Rectangle(
                pPosicao.getColuna() * Consts.CELL_SIDE + 4,
                (pPosicao.getLinha() - 1) * Consts.CELL_SIDE + 4,
                Consts.CELL_SIDE - 8,
                Consts.CELL_SIDE - 8);
        return hitbox;
    }

    public void drawHitbox(int cameraOffsetX, int cameraOffsetY, Graphics g2) {
        Rectangle hitbox = getHitbox();

        if (hitbox != null) {
            g2.setColor(new Color(255, 0, 0, 70)); 
            g2.fillRect(hitbox.x - cameraOffsetX, hitbox.y - cameraOffsetY, hitbox.width, hitbox.height);
            g2.setColor(Color.RED);
            g2.drawRect(hitbox.x - cameraOffsetX, hitbox.y - cameraOffsetY, hitbox.width, hitbox.height);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + (hitbox.x - cameraOffsetX) + "," + (hitbox.y - cameraOffsetY) + ")",
                    (hitbox.x - cameraOffsetX), hitbox.y - 5);
        }
    }

    public void atualizarFisica() {
        Posicao abaixo = new Posicao(pPosicao.getLinha() + gravidade, pPosicao.getColuna());
        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(abaixo)) {
            this.setPosicao(abaixo.getLinha(), abaixo.getColuna());
        }
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
