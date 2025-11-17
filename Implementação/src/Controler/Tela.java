package Controler;

import Modelo.Personagem;
import Modelo.ParedeRoda;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.Coletavel;
import Modelo.BichinhoVaiVemHorizontal;
import Auxiliar.ArrastaPersListener;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.BichinhoVaiVemVertical;
import Modelo.Esfera;
import Modelo.ZigueZague;
import Auxiliar.Posicao;
import Modelo.ParedeH;
import Modelo.ParedeV;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
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
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import Modelo.Fase;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Fase> fases;
    public Fase faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    public int fase_num = 0;
    private final Set<Integer> teclasPressionadas = new HashSet<>();
    private boolean cima, baixo, esquerda,direita;
    
    public Tela() {
        Desenho.setCenario(this);
        initComponents();

        new DropTarget(this,new ArrastaPersListener(this));
        this.addMouseListener(this);
        /*mouse*/
        this.addKeyListener(this);
        /*teclado*/
        cima=false;
        baixo=false;
        direita=false;
        esquerda=false;
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        /*Cria faseAtual adiciona personagens*/
        ArrayList<Personagem> pers = new ArrayList<>();

        this.faseAtual = new Fase(pers);

        hero = new Hero("joaninha.png", 0, 7);
        // --- Definição das Imagens ---
        String imgPH = "ParedeHorizontal.png"; //
        String imgPV = "ParedeVertical.png";   //
        String imgC = "coracaoo.png";        //
        String imgPRV = "ParedeRodaVertical.png"; //
        String imgPRH = "paredeRodaHorizontal.png"; //

        // --- Inimigo ---
        ZigueZague zz = new ZigueZague("bomba.png", 5, 5);

        BichinhoVaiVemHorizontal bBichinhoH = new BichinhoVaiVemHorizontal("roboPink.png", 3, 3);

        BichinhoVaiVemHorizontal bBichinhoH2 = new BichinhoVaiVemHorizontal("roboPink.png", 6,6);

        BichinhoVaiVemVertical bVv = new BichinhoVaiVemVertical("Caveira.png", 10,10);

        Caveira bV = new Caveira("caveira.png", 9, 1);

        Chaser chase = new Chaser("chaser.png", 12, 12);

        Esfera es = new Esfera("esfera.png", 10, 13);

        pers.add(hero);
        pers.add(zz);
        pers.add(bBichinhoH);
        pers.add(bBichinhoH2);
        pers.add(bVv);
        pers.add(bV);
        pers.add(chase);
        pers.add(es);


        Chaser chase1 = new Chaser("chaser.png", 20, 8);    // centro do mapa (20, 8)
        this.addPersonagem(chase1);

        // --- Bordas do Labirinto (14x14) ---
        for (int j = 0; j < 14; j++) {
            this.addPersonagem(new ParedeH(imgPH, 0, j)); // Borda superior
            this.addPersonagem(new ParedeH(imgPH, 13, j)); // Borda inferior
        }
        for (int i = 1; i < 13; i++) {
            this.addPersonagem(new ParedeV(imgPV, i, 0)); // Borda esquerda
            this.addPersonagem(new ParedeV(imgPV, i, 13)); // Borda direita
        }

        // --- Paredes Internas (Estáticas) ---
        this.addPersonagem(new ParedeH(imgPH, 3, 1));
        this.addPersonagem(new ParedeH(imgPH, 3, 2));
        this.addPersonagem(new ParedeV(imgPV, 1, 5));
        this.addPersonagem(new ParedeV(imgPV, 2, 5));
        this.addPersonagem(new ParedeV(imgPV, 3, 5));
        this.addPersonagem(new ParedeV(imgPV, 4, 5));
        this.addPersonagem(new ParedeV(imgPV, 5, 5));

        this.addPersonagem(new ParedeH(imgPH, 7, 5));
        this.addPersonagem(new ParedeH(imgPH, 7, 6));
        this.addPersonagem(new ParedeH(imgPH, 7, 7));
        this.addPersonagem(new ParedeH(imgPH, 7, 8));

        this.addPersonagem(new ParedeV(imgPV, 9, 9));
        this.addPersonagem(new ParedeV(imgPV, 10, 9));
        this.addPersonagem(new ParedeV(imgPV, 11, 9));
        this.addPersonagem(new ParedeV(imgPV, 12, 9));

        // --- Paredes Internas (Giratórias) ---
        // Lembrete: A sua ParedeRoda cuida de criar o 'prox' e o 'meio'
        
        // Portão 1 (Vertical) na posição (4, 8)
        // new ParedeRoda(Imagem, linha, coluna, éMetade, ant, éVertical, meio)
        ParedeRoda pr1 = new ParedeRoda(imgPRV, 4, 8, true, null, true, null);
        this.addPersonagem(pr1);
        this.addPersonagem(pr1.prox);
        this.addPersonagem(pr1.meio);

        // Portão 2 (Horizontal) na posição (10, 3)
        ParedeRoda pr2 = new ParedeRoda(imgPRH, 10, 3, true, null, false, null);
        this.addPersonagem(pr2);
        this.addPersonagem(pr2.prox);
        this.addPersonagem(pr2.meio);

        // --- Coletáveis ---
        this.addPersonagem(new Coletavel(imgC, 1, 2));
        this.addPersonagem(new Coletavel(imgC, 1, 12));
        this.addPersonagem(new Coletavel(imgC, 5, 2));
        this.addPersonagem(new Coletavel(imgC, 5, 10));
        this.addPersonagem(new Coletavel(imgC, 8, 8));
        this.addPersonagem(new Coletavel(imgC, 10, 1));
        this.addPersonagem(new Coletavel(imgC, 12, 5));
        this.addPersonagem(new Coletavel(imgC, 12, 11));

    }

    public ArrayList<Fase> getFases() {
        return fases;
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
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "gramaFundo.jpg");
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
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual,cima, baixo, esquerda, direita);
            cima=false;
            baixo=false;
            direita=false;
            esquerda=false;

            this.cj.desenhaTudo(faseAtual);
        }

        // desnhar a borda cronometro POR CIMA de tudo
        this.cj.getBorda().desenhar(g2, this);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void atualizaCamera() {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
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
        try {
            if (teclasPressionadas.contains(e.getKeyCode()))
                    return;

            teclasPressionadas.add(e.getKeyCode());
            
            if (e.getKeyCode() == KeyEvent.VK_T) {
                this.faseAtual.getPersonagens().clear();
                Fase novaFase = new Fase(new ArrayList<Personagem>());

                /*Cria faseAtual adiciona personagens*/
                hero = new Hero("Robbo.png", 10, 10);
                hero.setPosicao(10, 10);
                novaFase.getPersonagens().add(hero);
                this.atualizaCamera();

                ZigueZague zz = new ZigueZague("bomba.png", 0, 0);
                novaFase.getPersonagens().add(zz);

                Esfera es = new Esfera("esfera.png", 4, 4);
                novaFase.getPersonagens().add(es);

                faseAtual = novaFase;
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                hero.moveUp();
                cima=true;
                
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                hero.moveDown();
                baixo=true;
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hero.moveLeft();
                esquerda=true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hero.moveRight();
                direita=true;
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                File tanque = new File("POO.dat");
                tanque.createNewFile();
                FileOutputStream canoOut = new FileOutputStream(tanque);
                ObjectOutputStream serializador = new ObjectOutputStream(canoOut);
                serializador.writeObject(faseAtual);
                serializador.close();
             } 
            // else if (e.getKeyCode() == KeyEvent.VK_L) {
            //     File tanque = new File("POO.dat");
            //     FileInputStream canoOut = new FileInputStream(tanque);
            //     ObjectInputStream serializador = new ObjectInputStream(canoOut);         SERÁ IMPLEMENTADA O LOAD AINDA
            //     faseAtual = (ArrayList<Personagem>)serializador.readObject();
            //     serializador.close();
            // }
            else if (e.getKeyCode() == KeyEvent.VK_M)
            {
                File tanque = new File("POO.zip");
                FileOutputStream canOut = new FileOutputStream(tanque);
                GZIPOutputStream compact = new GZIPOutputStream(canOut);
                ObjectOutputStream serialize = new ObjectOutputStream(compact);
                serialize.writeObject(serialize);
                serialize.close();
            }
            
            else if (e.getKeyCode() == KeyEvent.VK_N)
            {
                File tanque = new File("POO.zip");
                FileInputStream canOut = new FileInputStream(tanque);
                GZIPInputStream compact = new GZIPInputStream(canOut);
                ObjectInputStream serialize = new ObjectInputStream(compact);
                Personagem p1 = (ZigueZague) serialize.readObject();
                serialize.close();
            }

            else if (e.getKeyCode() == KeyEvent.VK_W)
            {
                File pers = new File("pers.zip");
                FileOutputStream fos = new FileOutputStream(pers);
                GZIPOutputStream gzo = new GZIPOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(gzo);
                Personagem p = this.faseAtual.getPersonagens().get(0);
                oos.writeObject(p);
                System.out.println("Arquivo salvo em --> " + pers.getAbsolutePath());
                oos.close();
            }

            this.atualizaCamera();
            this.setTitle("-> Cell: " + (hero.getPosicao().getLinha()) + ", " + (hero.getPosicao().getColuna()));

            //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
        } catch (Exception ee) {

        }
    }
    public void keyReleased(KeyEvent e) {
        teclasPressionadas.remove(e.getKeyCode());        
    }    

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        // Alterações feitas: Normalização da posição para funcionar "longe da área inicial" (Segue as coordenadas da camêra)
        this.hero.getPosicao().setPosicao((y / Consts.CELL_SIDE) - 1 + this.cameraLinha, (x / Consts.CELL_SIDE) + this.cameraColuna);

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
            .addGap(0, 600, Short.MAX_VALUE)
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


}
