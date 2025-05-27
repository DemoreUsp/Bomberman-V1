package Controler;

import Modelos.Personagem;
import Modelos.MapStuff.Nuvem;
import Controler.Fases.FasesHandler;
import Controler.Fases.Fase;
import Modelos.MapStuff.Bloco;
import Modelos.Entities.Villains.CanoBillbala;
import Modelos.Entities.Heroes.Heroi;
import Modelos.Entities.Villains.Koopa;
import Modelos.Entities.Villains.Goomba;
import Modelos.Entities.Villains.Bowser;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.ZipStore;
import Modelos.Cano;
import java.awt.Color;
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
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;
import java.awt.Rectangle;
import java. util.Random;
import java.io.Serializable;
import java.awt.dnd.*;
import java.util.List;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.List;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener, DropTargetListener {
    //private ArrayList<Fase> fases;
    private FasesHandler fases = new FasesHandler();
    private Fase faseAtual;
    private long TEMPO_TIMEOUT = 150;
    private long ultimoMovimentoHorizontal = 0;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private List<Nuvem> nuvens;
    private static final int NUM_NUVENS = 200; // Quantidade de nuvens
    private static final Random random = new Random();
    
    public Tela() {
    Desenho.setCenario(this);
    initComponents();
    this.addMouseListener(this);
    this.addKeyListener(this);
    new DropTarget(this, this);

    this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
            Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
    
    Heroi Mario = new Heroi("marioR.png");
    
    // Inicializa o sistema de fases
    File arquivoZip = new File("saves/save.zip");
    if(arquivoZip.exists()) {
        try {
            Fase loadedFase = (Fase) ZipStore.deserializeFromZip("saves/save.zip", "save.zip");
            System.out.println(loadedFase.getHeroi().getVidas());
            fases.carregarFaseLoaded(loadedFase);
            Mario = loadedFase.getHeroi();
            faseAtual = fases.getFaseAtual();
            System.out.println("Fase loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load Fase: " + e.getMessage());
        }
    }
    else {
        fases.inicializarFaseIndex(0, Mario);
        faseAtual = fases.getFaseAtual();
    }
    this.atualizaCamera();
    inicializarNuvens();
    Desenho.setCenario(this);
    initComponents();
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
    this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
    }
    
    public Fase getFaseAtual() {
        return faseAtual;
    }

    public ControleDeJogo getCj() {
        return cj;
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(fases.getFaseAtual(), p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.getPersonagens().remove(umPersonagem);
    }
    
    public boolean estaNoChao() {
        int lin = faseAtual.getHeroi().getPosicao().getLinha();
        int col = faseAtual.getHeroi().getPosicao().getColuna();
        Personagem b;
        for(int i = 0; i < faseAtual.getMapStuff().size(); i++) {
            b = faseAtual.getMapStuff().get(i);
            if(b.getPosicao().getLinha() == lin + 1 && b
                    .getPosicao().getColuna() == col) {
                return true;
            }
        }
        return false;
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }
    
    public void inicializarNuvens() {
        nuvens = new ArrayList<>();
        int tentativas = 0;
        int maxTentativas = 200; // Limite de tentativas para evitar loop infinito
        int distanciaMinima = Consts.CELL_SIDE * 2; // Distância mínima entre nuvens
    
        while (nuvens.size() < NUM_NUVENS && tentativas < maxTentativas) {
            // Gera posições aleatórias dentro dos limites do mundo
            int x = random.nextInt(Consts.MUNDO_LARGURA * Consts.CELL_SIDE);
            int y = random.nextInt(Consts.MUNDO_ALTURA * Consts.CELL_SIDE / 3); // Mantém nuvens no terço superior
        
            boolean posicaoValida = true;
        
            // Verifica se a nova posição está longe o suficiente das nuvens existentes
            for (Nuvem nuvemExistente : nuvens) {
                double distancia = calcularDistancia(x, y, nuvemExistente.getX(), nuvemExistente.getY());
            
                if (distancia < distanciaMinima) {
                    posicaoValida = false;
                    break;
                }
            }
        
            if (posicaoValida) {
                nuvens.add(new Nuvem(x, y));
            }
        
            tentativas++;
        }
    
        // Se não conseguimos colocar todas as nuvens, ajustamos a quantidade
        if (nuvens.size() < NUM_NUVENS) {
            System.out.println("Aviso: Foram geradas apenas " + nuvens.size() + " nuvens de " + NUM_NUVENS + " desejadas");
        }
    }

    // Método auxiliar para calcular a distância entre dois pontos
    private double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
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
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "ceu.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        desenharNuvens(g2);
        cj.processaTudo(fases.getFaseAtual(), fases.getFaseAtual().getHeroi());
        if(faseAtual.getHeroi().getVidas() <= 0) {
            System.out.println("Game Over\n");
            fases.inicializarFaseIndex(fases.getFaseAtualIndex(), fases.getFaseAtual().getHeroi());
            faseAtual.getHeroi().setVidas(1);
            System.out.println("Vidas: " + faseAtual.getHeroi().getVidas());
        }
        
        if(faseAtual.getPosicaoFinal().getLinha() == faseAtual.getHeroi().getPosicao().getLinha() &&
                faseAtual.getPosicaoFinal().getColuna() == faseAtual.getHeroi().getPosicao().getColuna()) {
            fases.proximaFase();
            faseAtual = fases.getFaseAtual();
        }
        
        int cameraOffsetX = cameraColuna * Consts.CELL_SIDE;
        int cameraOffsetY = cameraLinha * Consts.CELL_SIDE;
        this.cj.desenhaTudo(fases.getFaseAtual(), fases.getFaseAtual().getHeroi(), cameraOffsetX, cameraOffsetY, g2);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        
        for(Personagem p : faseAtual.getPersonagens()) {
            if(p instanceof Bowser) continue;
            p.atualizarFisica();
        }
        
        faseAtual.getHeroi().atualizarFisica();
    }

    private void atualizaCamera() {
        faseAtual = fases.getFaseAtual();
        int linha = faseAtual.getHeroi().getPosicao().getLinha();
        int coluna = faseAtual.getHeroi().getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }
    
    private void desenharNuvens(Graphics g) {
        try {
            for (Nuvem nuvem : nuvens) {
                // Calcula a posição ajustada para a câmera
                int posX = nuvem.getX() - (cameraColuna * Consts.CELL_SIDE);
                int posY = nuvem.getY() - (cameraLinha * Consts.CELL_SIDE);
            
                // Verifica se a nuvem está visível na tela
                if (posX > -Consts.CELL_SIDE && posX < Consts.RES * Consts.CELL_SIDE &&
                    posY > -Consts.CELL_SIDE && posY < Consts.RES * Consts.CELL_SIDE) {
                
                    Image nuvemImg = Toolkit.getDefaultToolkit().getImage(
                        new java.io.File(".").getCanonicalPath() + Consts.PATH + nuvem.getImagePath());
                
                    g.drawImage(nuvemImg, posX, posY, Consts.CELL_SIDE * 2, Consts.CELL_SIDE, null);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
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
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if(estaNoChao())
                faseAtual.getHeroi().moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            faseAtual.getHeroi().moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            long comparador = System.currentTimeMillis();
            if(!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                faseAtual.getHeroi().moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            long comparador = System.currentTimeMillis();
            if(!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                faseAtual.getHeroi().moveRight();
            }
        }
        this.atualizaCamera();
        this.setTitle("-> Cell: " + (faseAtual.getHeroi().getPosicao().getColuna()) + ", "
                + (faseAtual.getHeroi().getPosicao().getLinha()));
        repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if(e.getID() == KeyEvent.KEY_RELEASED) {
                try {
                    System.out.println("Salvando...");
                    ZipStore.serializeToZip("saves/save.zip", "save.zip", this.faseAtual);
                    System.out.println("Jogo salvo como sucesso!");
                } catch(IOException ex) {
                    System.out.println(ex);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            ZipStore.testZip();
        }
    }

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.faseAtual.getHeroi().getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        repaint();
    }
    
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable t = dtde.getTransferable();

            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List<File> arquivos = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                for (File arquivo : arquivos) {
                    if (arquivo.getName().endsWith(".zip")) {
                        // Supondo que o zip tenha apenas 1 entry: "inimigo.zip"
                        Object obj = ZipStore.deserializeFromZip(arquivo.getAbsolutePath(), "GoombaDrop.zip");

                        if (obj instanceof Goomba) {
                            Goomba inimigo = (Goomba) obj;

                            // Pega a posição do mouse no drop
                            int x = dtde.getLocation().x;
                            int y = dtde.getLocation().y;

                            // Converte para coordenadas de grade (ex: 32px por tile)
                            int linha = y / 32;
                            int coluna = x / 32;

                            inimigo.setPosicao(linha, coluna);

                            // Adiciona à fase atual
                            faseAtual.adicionarPersonagem(inimigo);

                            System.out.println("Inimigo inserido em: " + linha + ", " + coluna);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
 
    public void dragEnter(DropTargetDragEvent dtde) {
    }
    
    public void dragOver(DropTargetDragEvent dtde) {
    }
    
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }
    
    public void dragExit(DropTargetEvent dte) {
    }
    
}
