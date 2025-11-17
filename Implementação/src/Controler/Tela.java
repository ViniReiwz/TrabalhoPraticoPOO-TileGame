package Controler;

import Modelo.Coletavel;
import Auxiliar.GameUI;
import Modelo.Personagem;
import Modelo.ParedeRoda;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
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
import Modelo.ParedeRodaMeio;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private ArrayList<Fase> fases = new ArrayList<>();
    public Fase faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    public int fase_num = 0;
    private final Set<Integer> teclasPressionadas = new HashSet<>();
    private boolean cima, baixo, esquerda,direita;
    private GameUI gameUI = new GameUI();

    public Tela() {

        Desenho.setCenario(this);

        initComponents();

        new DropTarget(this,new ArrastaPersListener(this));

        // Adiciona um listener para os eventos do mouse neste objeto
        this.addMouseListener(this);
        
        // Adiciona um listener para os eventos do teclado neste objeto
        this.addKeyListener(this);
        
        // Não sei oq é isso
        cima=false;
        baixo=false;
        direita=false;
        esquerda=false;

        // Cria a janela do tamanho do tabuleiro + insets (bordas) da janela
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        // --- Definição das Imagens ---
        String imgPH = "ParedeHorizontal.png"; //
        String imgPV = "ParedeVertical.png";   //
        String imgPRV = "ParedeRodaVertical.png"; //
        String imgPRH = "paredeRodaHorizontal.png"; //

        //Criando uma fase -->
        /*  PS: Esse é somente um padrão pra organizar melhor o desenvolvimento
         *  Crie aFase fase_x()
         *  Crie o heroi,  e chame o método fase_x.addHero(Hero heroi) (Adiciona o heroi criado)
         *  Adicione os personagens com o método fase_x.addPers(Personagem p) [ Pode incluir o herói ou não ]
         *  Como os coletaveis e paredes são "iguais" para toda a fase (Paredes e Coletaveis sempre no mesmo formato e local)
         *      sua criação será feita posteriormente na classe Fase, aqui em tela criamos somente os objetos dos personagens
         */

        Fase fase_1 = new Fase();

        this.faseAtual = fase_1;
        this.addFase(fase_1);

        // Heroi
        Hero hero = new Hero("joaninha.png", 0, 7);
        
        // --- Inimigo ---

        Chaser chase = new Chaser("roboPink.png", 12, 12); // Posição inicial do inimigo

        ZigueZague zz = new ZigueZague("bomba.png", 5, 5);

        BichinhoVaiVemHorizontal bBichinhoH = new BichinhoVaiVemHorizontal("roboPink.png", 3, 3);

        BichinhoVaiVemHorizontal bBichinhoH2 = new BichinhoVaiVemHorizontal("roboPink.png", 6,6);

        BichinhoVaiVemVertical bVv = new BichinhoVaiVemVertical("Caveira.png", 10,10);

        Caveira bV = new Caveira("caveira.png", 9, 1);


        Esfera es = new Esfera("esfera.png", 10, 13);

        Chaser chase1 = new Chaser("chaser.png", 20, 8);    // centro do mapa (20, 8)
        

        fase_1.addHero(hero);
        fase_1.addPers(hero);
        fase_1.addPers(chase);
        fase_1.addPers(zz);
        fase_1.addPers(bBichinhoH);
        fase_1.addPers(bBichinhoH2);
        fase_1.addPers(bVv);
        fase_1.addPers(bV);
        fase_1.addPers(es);
        fase_1.addPers(chase1);

        // Criando fase 2 pra testar passar de fase ==>
        Fase fase_2 = new Fase();
        this.addFase(fase_2);

        // --- Inimigo Fase 2---

        Hero hero2 = new Hero("joaninha.png", 0, 7);

        ZigueZague zz2 = new ZigueZague("bomba.png", 5, 5);

        BichinhoVaiVemHorizontal bBichinhoH222 = new BichinhoVaiVemHorizontal("roboPink.png", 3, 3);

        BichinhoVaiVemVertical bVv2 = new BichinhoVaiVemVertical("Caveira.png", 10,10);

        Caveira bV2 = new Caveira("caveira.png", 9, 1);


        Esfera es2 = new Esfera("esfera.png", 10, 13);


        Chaser chase2 = new Chaser("chaser.png", 20, 8);    // centro do mapa (20, 8)

        fase_2.addHero(hero2);
        fase_2.addPers(hero2);
        fase_2.addPers(zz2);
        fase_2.addPers(bBichinhoH222);
        fase_2.addPers(bVv2);
        fase_2.addPers(bV2);
        fase_2.addPers(es2);
        fase_2.addPers(chase2);

        this.addFase(fase_2);

        // --- Bordas do Labirinto (14x14) ---
        for (int j = 1; j < Consts.MUNDO_LARGURA-1; j++) {
            this.addPersonagem(new ParedeH(imgPH, 0, j)); // Borda superior
            this.addPersonagem(new ParedeH(imgPH, Consts.MUNDO_ALTURA-1, j)); // Borda inferior
        }
        for (int i = 1; i < Consts.MUNDO_ALTURA-1; i++) {
            this.addPersonagem(new ParedeV(imgPV, i, 0)); // Borda esquerda
            this.addPersonagem(new ParedeV(imgPV, i, Consts.MUNDO_LARGURA-1)); // Borda direita
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

        // // --- Coletáveis ---
        // this.addPersonagem(new Coletavel(imgC, 1, 2));
        // this.addPersonagem(new Coletavel(imgC, 1, 12));
        // this.addPersonagem(new Coletavel(imgC, 5, 2));
        // this.addPersonagem(new Coletavel(imgC, 5, 10));
        // this.addPersonagem(new Coletavel(imgC, 8, 8));
        // this.addPersonagem(new Coletavel(imgC, 10, 1));
        // this.addPersonagem(new Coletavel(imgC, 12, 5));
        // this.addPersonagem(new Coletavel(imgC, 12, 11));

    }

    public void addFase(Fase fase)
    {
        this.fases.add(fase);
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
        ArrayList<Coletavel> removed = new ArrayList<>();
 
            
        for(int j = faseAtual.getColetaveis().size() - 1; j >= 0; j--)
        {
            Coletavel cIesimoColetavel = faseAtual.getColetaveis().get(j);
            if(umPersonagem.getPosicao().igual(cIesimoColetavel.getPosicao()))
            {
                removed.add(cIesimoColetavel);
            }
            
            if(umPersonagem instanceof ParedeRodaMeio){
                if(umPersonagem.getPosicao().ParedeVd(cIesimoColetavel.getPosicao())||
                   umPersonagem.getPosicao().ParedeVe(cIesimoColetavel.getPosicao())||
                   umPersonagem.getPosicao().ParedeHc(cIesimoColetavel.getPosicao())||
                   umPersonagem.getPosicao().ParedeHb(cIesimoColetavel.getPosicao()))
                {
                    removed.add(cIesimoColetavel);
                }
            }
            
        }
        faseAtual.getColetaveis().removeAll(removed);
        faseAtual.updatePoints();
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
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual,cima, baixo, esquerda, direita);
            cima=false;
            baixo=false;
            direita=false;
            esquerda=false;
        }

        // desnhar a borda cronometro POR CIMA de tudo
        this.cj.getBorda().desenhar(g2, this);
        // Desenha a UI (vidas, pontuação, fase)
        this.gameUI.desenhar(g2, this, this.faseAtual);
        //Verifica passagem de fase
        this.cj.passaFase(this);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }

    }

    private void atualizaCamera() {
        int linha = this.faseAtual.heroi.getPosicao().getLinha();
        int coluna = this.faseAtual.heroi.getPosicao().getColuna();

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
            
            // if (e.getKeyCode() == KeyEvent.VK_T) {
            //     this.faseAtual.getPersonagens().clear();
            //     Fase novaFase = new Fase(new ArrayList<Personagem>());

            //     /*Cria faseAtual adiciona personagens*/
            //     this.faseAtual.heroi = new this.faseAtual.heroi("Robbo.png", 10, 10);
            //     this.faseAtual.heroi.setPosicao(10, 10);
            //     novaFase.getPersonagens().add(this.faseAtual.heroi);
            //     this.atualizaCamera();

            //     ZigueZague zz = new ZigueZague("bomba.png", 0, 0);
            //     novaFase.getPersonagens().add(zz);

            //     Esfera es = new Esfera("esfera.png", 4, 4);
            //     novaFase.getPersonagens().add(es);

            //     faseAtual = novaFase;
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.faseAtual.heroi.moveUp();
                cima=true;
                
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.faseAtual.heroi.moveDown();
                baixo=true;
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.faseAtual.heroi.moveLeft();
                esquerda=true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.faseAtual.heroi.moveRight();
                direita=true;
            } else if (e.getKeyCode() == KeyEvent.VK_S) 
            {
                System.out.println("Salvando fase");
                File ultimafase = new File("UltimaFase.dat");
                ultimafase.createNewFile();
                FileOutputStream fos = new FileOutputStream(ultimafase);
                ObjectOutputStream serializador = new ObjectOutputStream(fos);
                serializador.writeObject(this.faseAtual);
                serializador.close();
            } 
            else if (e.getKeyCode() == KeyEvent.VK_L) 
            {   
                System.out.println("Carregando fase");
                File ultimafase = new File("UltimaFase.dat");
                FileInputStream fis = new FileInputStream(ultimafase);
                ObjectInputStream serializador = new ObjectInputStream(fis);
                Fase fase_carregada = (Fase)serializador.readObject();
                this.faseAtual = fase_carregada;
                int pos = this.fases.indexOf(fase_carregada);
                this.fase_num = pos;
                serializador.close();
                repaint();
            }
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
                Personagem p = this.faseAtual.getPersonagens().get(0);                oos.writeObject(p);
                System.out.println("Arquivo salvo em --> " + pers.getAbsolutePath());
                oos.close();
            }

            this.atualizaCamera();
            this.setTitle("-> Cell: " + (this.faseAtual.heroi.getPosicao().getLinha()) + ", " + (this.faseAtual.heroi.getPosicao().getColuna()));

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
        this.faseAtual.heroi.getPosicao().setPosicao((y / Consts.CELL_SIDE) - 1 + this.cameraLinha, (x / Consts.CELL_SIDE) + this.cameraColuna);

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
