package Controler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;

import Auxiliar.GameUI;
import Auxiliar.ArrastaPersListener;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;

import Modelo.Personagem;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.Fase;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private ArrayList<Fase> fases = new ArrayList<>();
    public Fase faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    public int fase_num = 0;
    public int x_offset;
    public int y_offset;
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

        this.cj.setGameUI(this.gameUI);
        
        // Não sei oq é isso
        cima=false;
        baixo=false;
        direita=false;
        esquerda=false;

        // Cria a janela do tamanho do tabuleiro + insets (bordas) da janela
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        this.setLocationRelativeTo(null);
        setResizable(true);

        //Criando uma fase -->
        /*  PS: Esse é somente um padrão pra organizar melhor o desenvolvimento
         *  Crie aFase fase_x()
         *  Crie o heroi,  e chame o método fase_x.addHero(Hero heroi) (Adiciona o heroi criado)
         *  Adicione os personagens com o método fase_x.addPers(Personagem p) [ Pode incluir o herói ou não ]
         *  Como os coletaveis e paredes são "iguais" para toda a fase (Paredes e Coletaveis sempre no mesmo formato e local)
         *      sua criação será feita posteriormente na classe Fase, aqui em tela criamos somente os objetos dos personagens
         */

        Fase fase_1 = new Fase(4,3,5);

        // tempo de spawn pra fase 1
        fase_1.setTempoSpawnBase(150);

        this.faseAtual = fase_1;
        this.addFase(fase_1);

        // Heroi
        Hero hero = new Hero("joaninha.png", 0, 7);
                
        fase_1.addHero(hero);
        fase_1.addPers(hero);

        // Criando fase 2 pra testar passar de fase ==>
        
        Fase fase_2 = new Fase(4, 4,4);
        
        fase_2.setTempoSpawnBase(100);
        this.addFase(fase_2);

        Hero hero2 = new Hero("joaninha.png", 0, 7);

        fase_2.addHero(hero2);
        fase_2.addPers(hero2);

        this.addFase(fase_2);
    }

    public void addFase(Fase fase)
    {
        this.fases.add(fase);
    }

    public ArrayList<Fase> getFases() {
        return fases;
    }

    public Fase getFaseAtual()
    {
        return this.faseAtual;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {

        

        // CENTRALIZAÇÃO DO MAPA
        int mapaW = Consts.RES * Consts.CELL_SIDE;
        int mapaH = Consts.RES* Consts.CELL_SIDE;

        int offsetX = (getWidth() - mapaW) / 2;
        int offsetY = (getHeight() - mapaH) / 2;

        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g2 = g.create(offsetX,offsetY,mapaW,mapaH);

        Graphics gJogo = g2.create();
        
        /**
         * ***********Desenha cenário de fundo*************
         */
        if (!this.faseAtual.getPersonagens().isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual,cima, baixo, esquerda, direita);
            cima=false;
            baixo=false;
            direita=false;
            esquerda=false;
        }

        // desnhar a borda cronometro POR CIMA de tudo
        this.cj.getBorda().desenhar(gJogo, this);
        // Desenha a UI (vidas, pontuação, fase)
        this.gameUI.desenhar(gJogo,g2, this, this.faseAtual);
        //Verifica passagem de fase
        this.cj.passaFase(this);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }

    }

    public void go() 
    {
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
            if (e.getKeyCode() == KeyEvent.VK_UP) 
            {
                this.faseAtual.heroi.moveUp();
                cima=true;
                
            } 
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) 
            {
                this.faseAtual.heroi.moveDown();
                baixo=true;
            } 
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) 
            {
                this.faseAtual.heroi.moveLeft();
                esquerda=true;
            } 
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
            {
                this.faseAtual.heroi.moveRight();
                direita=true;
            } 
            else if (e.getKeyCode() == KeyEvent.VK_S) 
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

            this.setTitle("-> Cell: " + (this.faseAtual.heroi.getPosicao().getLinha()) + ", " + (this.faseAtual.heroi.getPosicao().getColuna()));

            //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
        } 
        catch (Exception ee) 
        {
            System.out.println(ee);
        }
    }
    public void keyReleased(KeyEvent e) 
    {
        teclasPressionadas.remove(e.getKeyCode());        
    }    

    public void mousePressed(MouseEvent e){}


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() 
    {

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
