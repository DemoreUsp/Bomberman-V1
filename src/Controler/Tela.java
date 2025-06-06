package Controler;

import Modelos.Personagem;
import Modelos.MapStuff.Nuvem;
import Controler.Fases.FasesHandler;
import Controler.Fases.Fase;
import Modelos.Entities.Heroes.Heroi;
import Modelos.Entities.Villains.Goomba;
import Modelos.Entities.Villains.Bowser;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Modelos.Entities.Villains.AuxiliarVillains.Billbala;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.Font;

// Classe que de fato roda o jogo
public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener, DropTargetListener {
    private FasesHandler fases = new FasesHandler();
    private Fase faseAtual;
    private long TEMPO_TIMEOUT = 150;
    private long ultimoMovimentoHorizontal = 0;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private List<Nuvem> nuvens;
    private static final int NUM_NUVENS = 200; 
    private static final Random random = new Random();

    public Tela(boolean carregarSave) {
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
        if (carregarSave && arquivoZip.exists()) {
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
        } else {
            fases.inicializarFaseIndex(0, Mario);
            faseAtual = fases.getFaseAtual();
        }
        this.atualizaCamera();
        inicializarNuvens();
        Desenho.setCenario(this);
        initComponents();
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
        for (int i = 0; i < faseAtual.getMapStuff().size(); i++) {
            b = faseAtual.getMapStuff().get(i);
            if (b.getPosicao().getLinha() == lin + 1 && b
                    .getPosicao().getColuna() == col) {
                return true;
            }
        }
        return false;
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    // Função que gera aleatóriamente nuvens a cada run
    public void inicializarNuvens() {
        nuvens = new ArrayList<>();
        int tentativas = 0;
        int maxTentativas = 200; 
        int distanciaMinima = Consts.CELL_SIDE * 2; 

        while (nuvens.size() < NUM_NUVENS && tentativas < maxTentativas) {
            int x = random.nextInt(Consts.MUNDO_LARGURA * Consts.CELL_SIDE);
            int y = random.nextInt(Consts.MUNDO_ALTURA * Consts.CELL_SIDE / 3); 

            boolean posicaoValida = true;

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
    }

    // Método para calcular a distância entre dois pontos
    private double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        
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
        String textoCeu = "FASE " + (fases.getFaseAtualIndex() + 1);
        int xi = 270;
        int yi = 50;

        g2.setFont(new Font("SansSerif", Font.BOLD, 50));

        g2.setColor(Color.BLACK);
        g2.drawString(textoCeu, xi - 2, yi - 2);
        g2.drawString(textoCeu, xi - 2, yi + 2);
        g2.drawString(textoCeu, xi + 2, yi - 2);
        g2.drawString(textoCeu, xi + 2, yi + 2);

        g2.setColor(Color.WHITE);
        g2.drawString(textoCeu, xi, yi);

        cj.processaTudo(fases.getFaseAtual(), fases.getFaseAtual().getHeroi());
        if (faseAtual.getHeroi().getVidas() <= 0) {
            System.out.println("Game Over\n");
            fases.inicializarFaseIndex(fases.getFaseAtualIndex(), fases.getFaseAtual().getHeroi());
            faseAtual.getHeroi().setVidas(1);
            System.out.println("Vidas: " + faseAtual.getHeroi().getVidas());
        }

        if (faseAtual.getPosicaoFinal().getLinha() == faseAtual.getHeroi().getPosicao().getLinha() &&
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

        for (Personagem p : faseAtual.getPersonagens()) {
            if (p instanceof Bowser || p instanceof Billbala)
                continue;
            p.atualizarFisica();
        }

        faseAtual.getHeroi().atualizarFisica();
    }

    private void atualizaCamera() {
        faseAtual = fases.getFaseAtual();
        int linha = faseAtual.getHeroi().getPosicao().getLinha();
        int coluna = faseAtual.getHeroi().getPosicao().getColuna();

        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }

    private void desenharNuvens(Graphics g) {
        try {
            for (Nuvem nuvem : nuvens) {
                int posX = nuvem.getX() - (cameraColuna * Consts.CELL_SIDE);
                int posY = nuvem.getY() - (cameraLinha * Consts.CELL_SIDE);

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
            if (estaNoChao())
                faseAtual.getHeroi().moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            faseAtual.getHeroi().moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            long comparador = System.currentTimeMillis();
            if (!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                faseAtual.getHeroi().moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            long comparador = System.currentTimeMillis();
            if (!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                faseAtual.getHeroi().moveRight();
            }
        }
        this.atualizaCamera();
        this.setTitle("-> Cell: " + (faseAtual.getHeroi().getPosicao().getColuna()) + ", "
                + (faseAtual.getHeroi().getPosicao().getLinha()));
        repaint(); 
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                try {
                    System.out.println("Salvando...");
                    ZipStore.serializeToZip("saves/save.zip", "save.zip", this.faseAtual);
                    System.out.println("Jogo salvo como sucesso!");
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            ZipStore.testZip();
        }
    }

    public void mousePressed(MouseEvent e) {
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
                        Object obj = ZipStore.deserializeFromZip(arquivo.getAbsolutePath(), "GoombaDrop.zip");

                        if (obj instanceof Goomba) {
                            Goomba inimigo = (Goomba) obj;

                            int x = dtde.getLocation().x;
                            int y = dtde.getLocation().y;

                            int linha = y / 32;
                            int coluna = x / 32;

                            inimigo.setPosicao(linha, coluna);

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
                        .addGap(0, 561, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE));

        pack();
    }

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
