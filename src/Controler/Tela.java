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
import javax.swing.JOptionPane;

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
    
    // Em Tela.java
    public boolean estaNoChao(Personagem personagem) {
        int lin = personagem.getPosicao().getLinha();
        int col = personagem.getPosicao().getColuna();
    
        // Verifica se há bloco sólido abaixo
        for (Personagem bloco : faseAtual.getMapStuff()) {
        if (bloco.getPosicao().getLinha() == lin + 1 && 
                bloco.getPosicao().getColuna() == col && 
                !bloco.isbTransponivel()) {
                return true;
            }
        }
        return false;
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
    // Fase 1 - Tutorial de Pulo
    Fase fase1 = new Fase(1, new Posicao(12, 1), new Posicao(10, 15));
    // Plataforma principal
    for(int col = 0; col < 20; col++) {
        fase1.adicionarMapStuff(new Bloco("bloco.png").setPosicao(13, col));
    }
    // Plataforma elevada (obriga o pulo)
    criarPlataformaHorizontal(fase1, 10, 5, 8);
    fase1.adicionarPersonagem(new Goomba("goomba.png").setPosicao(10, 12));

    // Fase 2 - Duas Plataformas
    Fase fase2 = new Fase(2, new Posicao(12, 1), new Posicao(10, 15));
    for(int col = 0; col < 20; col++) {
        fase2.adicionarMapStuff(new Bloco("bloco.png").setPosicao(13, col));
    }
    criarPlataformaHorizontal(fase2, 10, 3, 5);
    criarPlataformaHorizontal(fase2, 8, 10, 12);
    fase2.adicionarPersonagem(new Goomba("goomba.png").setPosicao(10, 7));
    fase2.adicionarPersonagem(new Koopa("koopa.png").setPosicao(8, 14));

    // Fase 3 - Plataformas em Escada
    Fase fase3 = new Fase(3, new Posicao(12, 1), new Posicao(6, 18));
    for(int col = 0; col < 20; col++) {
        fase3.adicionarMapStuff(new Bloco("bloco.png").setPosicao(13, col));
    }
    criarEscada(fase3, 2, 10, 12, true); // Escada para direita
    criarEscada(fase3, 15, 8, 10, false); // Escada para esquerda
    fase3.adicionarPersonagem(new Goomba("goomba.png").setPosicao(10, 5));
    fase3.adicionarPersonagem(new Koopa("koopa.png").setPosicao(8, 12));
    fase3.adicionarPersonagem(new Goomba("goomba.png").setPosicao(6, 17));

    // Fase 4 - Plataformas Quebradas
    Fase fase4 = new Fase(4, new Posicao(12, 1), new Posicao(7, 19));
    for(int col = 0; col < 20; col++) {
        if(col % 3 != 0) { // Cria buracos regulares
            fase4.adicionarMapStuff(new Bloco("bloco.png").setPosicao(13, col));
        }
    }
    criarPlataformaHorizontal(fase4, 9, 4, 6);
    criarPlataformaHorizontal(fase4, 7, 12, 14);
    fase4.adicionarPersonagem(new Goomba("goomba.png").setPosicao(9, 5));
    fase4.adicionarPersonagem(new Koopa("koopa.png").setPosicao(7, 13));
    fase4.adicionarPersonagem(new Goomba("goomba.png").setPosicao(13, 18));

    // Fase 5 - Desafio Final
    Fase fase5 = new Fase(5, new Posicao(12, 1), new Posicao(5, 19));
    for(int col = 0; col < 20; col++) {
        fase5.adicionarMapStuff(new Bloco("bloco.png").setPosicao(13, col));
    }
    criarPlataformaVertical(fase5, 3, 9, 11);
    criarPlataformaVertical(fase5, 15, 6, 8);
    fase5.adicionarPersonagem(new Goomba("goomba.png").setPosicao(9, 4));
    fase5.adicionarPersonagem(new Koopa("koopa.png").setPosicao(6, 15));
    fase5.adicionarPersonagem(new Goomba("goomba.png").setPosicao(11, 9));
    fase5.adicionarPersonagem(new Koopa("koopa.png").setPosicao(5, 18));
    fase5.adicionarPersonagem(new Bowser("bowser.png").setPosicao(5, 19));

    fases.add(fase1);
    fases.add(fase2);
    fases.add(fase3);
    fases.add(fase4);
    fases.add(fase5);
}

// Métodos auxiliares para construção
private void criarPlataformaHorizontal(Fase fase, int linha, int colInicio, int colFim) {
    for(int col = colInicio; col <= colFim; col++) {
        fase.adicionarMapStuff(new Bloco("bloco.png").setPosicao(linha, col));
    }
}

private void criarEscada(Fase fase, int colBase, int linhaInicio, int linhaFim, boolean direita) {
    for(int lin = linhaInicio; lin <= linhaFim; lin++) {
        int col = colBase + (direita ? (lin - linhaInicio) : -(lin - linhaInicio));
        fase.adicionarMapStuff(new Bloco("bloco.png").setPosicao(lin, col));
    }
}

private void criarPlataformaVertical(Fase fase, int coluna, int linhaInicio, int linhaFim) {
    for(int lin = linhaInicio; lin <= linhaFim; lin++) {
        fase.adicionarMapStuff(new Bloco("bloco.png").setPosicao(lin, coluna));
    }
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
        faseAtual.marcarComoCompleta();
        carregarFase(++faseAtualIndex);
        mostrarMensagemFase();
    } else {
        JOptionPane.showMessageDialog(this, "Você venceu o jogo!");
    }
}

    private void mostrarMensagemFase() {
    String[] mensagens = {
        "Fase 1: Aprenda a pular!",
        "Fase 2: Cuidado com os Koopas!",
        "Fase 3: Use as escadas!",
        "Fase 4: Atenção aos buracos!",
        "Fase Final: Enfrente Bowser!"
    };
    JOptionPane.showMessageDialog(this, mensagens[faseAtualIndex]);
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
            if(estaNoChao(Mario))
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
