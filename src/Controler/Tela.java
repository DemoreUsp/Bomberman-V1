package Controler;

import Modelos.Personagem;
import Modelos.Fase;
import Modelos.Bloco;
import Modelos.CanoBillbala;
import Modelos.Heroi;
import Modelos.Koopa;
import Modelos.Goomba;
import Modelos.Bowser;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
//import Modelos.BichinhoVaiVemVertical;
//import Modelos.ZigueZague;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {
    private ArrayList<Fase> fases;
    private int faseAtualIndex;
    private Fase faseAtual;
    private Heroi Mario;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;

    public Tela() {
    Desenho.setCenario(this);
    initComponents();
    this.addMouseListener(this);
    this.addKeyListener(this);

    this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
            Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

    // Cria o herói primeiro
    Mario = new Heroi("mario.png");
    Mario.setMortal(true);
    
    // Inicializa o sistema de fases
    this.fases = new ArrayList<>();
    this.faseAtualIndex = 0;
    inicializarFases();
    carregarFase(0);
 
    this.atualizaCamera();

        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        /*mouse*/

        this.addKeyListener(this);
        /*teclado*/
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().remove(umPersonagem);
    }
    
    public boolean estaNoChao() {
        int lin = Mario.getPosicao().getLinha();
        int col = Mario.getPosicao().getColuna();
        Personagem b;
        for(int i = 0; i < faseAtual.getMapStuff().size(); i++) {
            b = faseAtual.getMapStuff().get(i);
            if(b.getPosicao().equals()) {
                return false;
            }
        }
        return true;
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /**
         * ***********Desenha cenário de fundo*************
         */
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (!this.faseAtual.getPersonagens().isEmpty()) {
            this.cj.desenhaTudo(faseAtual, faseAtual.getHeroi());
            this.cj.processaTudo(faseAtual, faseAtual.getHeroi());
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        Mario.atualizarFisica();
    }

    private void atualizaCamera() {
        int linha = faseAtual.getHeroi().getPosicao().getLinha();
        int coluna = faseAtual.getHeroi().getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
        
        if(faseAtual.getPosicaoFinal().getLinha() == faseAtual.getHeroi().getPosicao().getLinha() &&
                faseAtual.getPosicaoFinal().getColuna() == faseAtual.getHeroi().getPosicao().getColuna()) {
            proximaFase();
        }
    }
    
    private void inicializarFases() {
        Posicao inicioFase1 = new Posicao(9, 3);
        Posicao finalFase1 = new Posicao(9, 55);
        Fase fase1 = new Fase(1, inicioFase1, finalFase1);
        
        for(int col = 0; col < 65; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(10, col);
            fase1.adicionarMapStuff(blococol);
            for(int lin = 10; lin < 18; lin++) {
                Bloco blocolin = new Bloco("bloco.png");
                blocolin.setPosicao(lin, col);
                fase1.adicionarMapStuff(blocolin);
            }
        }
        
        // Adicionar inimigos
        CanoBillbala inimigo1 = new CanoBillbala("");
        inimigo1.setPosicao(9, 65);
        fase1.adicionarPersonagem(inimigo1);
        
        //Teste, mudança de fase funcionando legal, tudo certinho
        Fase fase2 = new Fase(2, new Posicao(5, 5), new Posicao(10,10));
        for(int col = 0; col < 25; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(0, col);
            fase2.adicionarMapStuff(blococol);
            for(int lin = 10; lin < 18; lin++) {
                Bloco blocolin = new Bloco("bloco.png");
                blocolin.setPosicao(lin, col);
                fase2.adicionarMapStuff(blocolin);
            }
        }
        
        Bowser inimigo2 = new Bowser("bowser.png");
        inimigo2.attVidas(5);
        inimigo2.setMortal(true);
        inimigo2.setPosicao(7, 22);
        fase2.adicionarPersonagem(inimigo2);

        fases.add(fase1);
        fases.add(fase2);
    }
    
    private void carregarFase(int indice) {
    faseAtualIndex = indice;
    faseAtual = fases.get(indice);
    
    //Limpa personagens da tela
    //faseAtual.getPersonagens().clear();
    
    // Posiciona Mario na posição inicial da fase
    Mario.setPosicao(
        faseAtual.getPosicaoInicialHeroi().getLinha(),
        faseAtual.getPosicaoInicialHeroi().getColuna()
    );
    
    // Adiciona Mario e os elementos da fase
    faseAtual.adicionarHeroi(Mario);
    // Adiciona outros elementos da fase (plataformas, inimigos, etc.)
    //faseAtual.getPersonagens().addAll(fases.get(indice).getPersonagens()); //Por que isso esta aqui? by Julio
}

    public void proximaFase() {
        if(faseAtualIndex < fases.size() - 1) {
            carregarFase(++faseAtualIndex);
        } else {
            System.out.println("Parabens");
        }
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            carregarFase(faseAtualIndex); 
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            Mario.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Mario.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Mario.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Mario.moveRight();
        }
        this.atualizaCamera();
        for(int i = 0; i < faseAtual.getPersonagens().size(); i++) {
            if(faseAtual.getPersonagens().get(i).getPosicao().getLinha() == faseAtual.getHeroi().getPosicao().getLinha() &&
                    faseAtual.getPersonagens().get(i).getPosicao().getColuna() == faseAtual.getHeroi().getPosicao().getColuna()) {
                faseAtual.getHeroi().attVidas(-1);
                if(faseAtual.getHeroi().getVidas() < 1)
                    System.out.println("Game over");
            }
        }
        this.setTitle("-> Cell: " + (Mario.getPosicao().getColuna()) + ", "
                + (Mario.getPosicao().getLinha()));
        
        repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.Mario.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        repaint();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
