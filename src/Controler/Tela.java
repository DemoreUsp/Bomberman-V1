package Controler;

import Modelos.Personagem;
import Modelos.Fase;
import Modelos.Bloco;
import Modelos.CanoBillbala;
import Modelos.Heroi;
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
        return cj.ehPosicaoValida(this.faseAtual.getPersonagens(), p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().remove(umPersonagem);
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
            this.cj.desenhaTudo(faseAtual.getPersonagens());
            this.cj.processaTudo(faseAtual.getPersonagens());
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void atualizaCamera() {
        int linha = Mario.getPosicao().getLinha();
        int coluna = Mario.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }
    
    private void inicializarFases() {
        Posicao inicioFase1 = new Posicao(0, 7);
        Fase fase1 = new Fase(1, inicioFase1);
        
        for(int col = 3; col < 8; col++) {
            Bloco bloco = new Bloco("bloco.png");
            bloco.setPosicao(10, col);
            fase1.adicionarPersonagem(bloco);
        }
        
        // Adicionar inimigos
        CanoBillbala inimigo1 = new CanoBillbala("");
        inimigo1.setPosicao(11, 12);
        fase1.adicionarPersonagem(inimigo1);
        
        
        // Fase 2 (exemplo)
        Fase fase2 = new Fase(2, new Posicao(5, 5));
        // ... configurar fase 2 ...

        fases.add(fase1);
        fases.add(fase2);
    }
    
    private void carregarFase(int indice) {
    faseAtualIndex = indice;
    faseAtual = fases.get(indice);
    
    // Limpa personagens da tela
    faseAtual.getPersonagens().clear();
    
    // Posiciona Mario na posição inicial da fase
    Mario.setPosicao(
        faseAtual.getPosicaoInicialHeroi().getLinha(),
        faseAtual.getPosicaoInicialHeroi().getColuna()
    );
    
    // Adiciona Mario e os elementos da fase
    faseAtual.getPersonagens().add(Mario);
    // Adiciona outros elementos da fase (plataformas, inimigos, etc.)
    faseAtual.getPersonagens().addAll(fases.get(indice).getPersonagens());
}

    public void proximaFase() {
        if(faseAtualIndex < fases.size() - 1) {
            carregarFase(++faseAtualIndex);
        } else {
            // Jogo completado
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
        this.setTitle("-> Cell: " + (Mario.getPosicao().getColuna()) + ", "
                + (Mario.getPosicao().getLinha()));

        //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
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
