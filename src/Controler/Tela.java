package Controler;

import Modelos.Personagem;
import Modelos.Nuvem;
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

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {
    private ArrayList<Fase> fases;
    private int faseAtualIndex;
    private long TEMPO_TIMEOUT = 150;
    private long ultimoMovimentoHorizontal = 0;
    private Fase faseAtual;
    private Heroi Mario;
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
    inicializarNuvens();

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
        cj.processaTudo(faseAtual, faseAtual.getHeroi());
        if(Mario.getVidas() <= 0) {
            System.out.println("Game Over\n");
            carregarFase(faseAtualIndex);
            Mario.setVidas(1);
            System.out.println("Vidas: " + Mario.getVidas());
        }
        
        if(faseAtual.getPosicaoFinal().getLinha() == faseAtual.getHeroi().getPosicao().getLinha() &&
                faseAtual.getPosicaoFinal().getColuna() == faseAtual.getHeroi().getPosicao().getColuna()) {
            proximaFase();
        }
        
        int cameraOffsetX = cameraColuna * Consts.CELL_SIDE;
        int cameraOffsetY = cameraLinha * Consts.CELL_SIDE;
        this.cj.desenhaTudo(faseAtual, faseAtual.getHeroi(), cameraOffsetX, cameraOffsetY, g2);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        
        for(Personagem p : faseAtual.getPersonagens()) {
            if(p instanceof Bowser) continue;
            p.atualizarFisica();
        }
        
        Mario.atualizarFisica();
    }

    private void atualizaCamera() {
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
    
    private void inicializarFases() {
        Posicao inicioFase1 = new Posicao(9, 3);
        Posicao finalFase1 = new Posicao(9, 55);
        Fase fase1 = new Fase(1, inicioFase1, finalFase1);
        
        for(int col = 0; col < 65; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(10, col);
            fase1.adicionarMapStuff(blococol);
            Bloco grama = new Bloco("terraComGrama.png");
            grama.setPosicao(10, col);
            fase1.adicionarMapStuff(grama);
            for(int lin = 11; lin < 18; lin++) {
                Bloco blocolin = new Bloco("terraMario.png");
                blocolin.setPosicao(lin, col);
                fase1.adicionarMapStuff(blocolin);
            }
        }
        
        // Adicionar inimigos
        
        // Plataforma elevada para Goomba (colunas 5-10, linha 8)
        for(int col = 5; col <= 10; col++) {
            Bloco plataforma = new Bloco("bloco.png");
            plataforma.setPosicao(8, col);
            fase1.adicionarMapStuff(plataforma);
        }
        Bloco parede1 = new Bloco("bloco.png");
        parede1.setPosicao(7, 5);
        fase1.adicionarMapStuff(parede1);
        Bloco parede2 = new Bloco("bloco.png");
        parede2.setPosicao(7, 10);
        fase1.adicionarMapStuff(parede2);
    
        // Goomba na plataforma
        Goomba goomba1 = new Goomba("goomba.png");
        goomba1.setPosicao(7, 7); // Acima da plataforma
        fase1.adicionarPersonagem(goomba1);
    
        // Plataforma elevada para Koopa (colunas 15-20, linha 8)
        for(int col = 15; col <= 20; col++) {
            Bloco plataforma = new Bloco("bloco.png");
            plataforma.setPosicao(8, col);
            fase1.adicionarMapStuff(plataforma);
        }
        
        Bloco parede3 = new Bloco("bloco.png");
        parede3.setPosicao(7, 15);
        fase1.adicionarMapStuff(parede3);
        
        Bloco parede4 = new Bloco("bloco.png");
        parede4.setPosicao(7, 20);
        fase1.adicionarMapStuff(parede4);
    
        // Koopa na plataforma
        Koopa koopa1 = new Koopa("koopa.png");
        koopa1.setPosicao(7, 18); // Acima da plataforma
        fase1.adicionarPersonagem(koopa1);
        
        CanoBillbala inimigo1 = new CanoBillbala("");
        inimigo1.setPosicao(9, 22);
        fase1.adicionarPersonagem(inimigo1);
        
        //Teste, mudança de fase funcionando legal, tudo certinho
        Fase fase2 = new Fase(2, new Posicao(5, 5), new Posicao(10,10));
        for(int col = 0; col < 50; col++) {
            Bloco blococol = new Bloco("bloco.png");
            blococol.setPosicao(0, col);
            fase2.adicionarMapStuff(blococol);
            Bloco grama = new Bloco("terraComGrama.png");
            grama.setPosicao(10, col);
            fase2.adicionarMapStuff(grama);
            for(int lin = 11; lin < 18; lin++) {
                Bloco blocolin = new Bloco("terraMario.png");
                blocolin.setPosicao(lin, col);
                fase2.adicionarMapStuff(blocolin);
            }
        }
        
        Bowser inimigo2 = new Bowser("bowser.png");
        inimigo2.setVidas(5);
        inimigo2.setMortal(true);
        inimigo2.setbTransponivel(true);
        inimigo2.setPosicao(8, 20);
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
    
    Mario.setVidas(2);
    // Adiciona Mario e os elementos da fase
    faseAtual.adicionarHeroi(Mario);
    // Adiciona outros elementos da fase (plataformas, inimigos, etc.)
    //faseAtual.getPersonagens().addAll(fases.get(indice).getPersonagens()); //Por que isso esta aqui? by Julio
}

    public void proximaFase() {
        if(faseAtualIndex < fases.size() - 1) {
            carregarFase(++faseAtualIndex);
            atualizaCamera();
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
            if(estaNoChao())
                Mario.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Mario.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            long comparador = System.currentTimeMillis();
            if(!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                Mario.moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            long comparador = System.currentTimeMillis();
            if(!(comparador - ultimoMovimentoHorizontal < TEMPO_TIMEOUT)) {
                ultimoMovimentoHorizontal = comparador;
                Mario.moveRight();
            }
        }
        this.atualizaCamera();
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
