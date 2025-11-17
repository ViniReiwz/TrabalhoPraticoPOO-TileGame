package Modelo;


import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Controler.ControleDeJogo;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics;
import java.io.IOException;
import Auxiliar.Consts;

public class Hero extends Personagem{

    private ImageIcon ImagemCima;
    private ImageIcon ImagemBaixo;
    private ImageIcon ImagemEsquerda;
    private ImageIcon ImagemDireita;

    private int direcaoAtual = -1; 
    private int vidas = 3;
    private Auxiliar.Posicao pPosicaoInicial;
    private int contadorMovimento;
    private final int VELOCIDADE_ATRASO = 3; //Controle de movimento do heroi


    public Hero(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG,linha, coluna);

        this.ImagemBaixo = this.iImage;

        this.ImagemCima = carregarImagem("joaninhaCima.png");
        this.ImagemEsquerda = carregarImagem("joaninhaEsquerda.png");
        this.ImagemDireita = carregarImagem("joaninhaDireita.png");
        this.ImagemBaixo = carregarImagem("joaninhaBaixo.png");
    
        this.iImage = ImagemCima;
    }

    private ImageIcon carregarImagem(String sNomeImagePNG) {
        try {
            ImageIcon iNewImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
            Image img = iNewImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            return new ImageIcon(bi);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    @Override
    public void autoDesenho() {
        // Incrementa o contador de frames
        contadorMovimento++;

        // 1. O contador atingiu o valor de atraso?
        if (contadorMovimento >= VELOCIDADE_ATRASO) {
            
            // 2. Reseta o contador
            contadorMovimento = 0;

            // 3. Executa a lógica de movimento (seu código antigo)
            if (direcaoAtual != -1) {
                Posicao proximaPosicao = new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna());

                switch (direcaoAtual) {
                    case 0: proximaPosicao.moveUp(); break;
                    case 1: proximaPosicao.moveDown(); break;
                    case 2: proximaPosicao.moveLeft(); break;
                    case 3: proximaPosicao.moveRight(); break;
                }

                if (Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicao)) {
                    this.pPosicao.setPosicao(proximaPosicao.getLinha(), proximaPosicao.getColuna());
                } else {
                    this.direcaoAtual = -1; // Bateu na parede
                }
            }
        }

        // 4. Desenha o herói na tela (isso acontece todo frame)
        super.autoDesenho();
    }

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public boolean moveUp() {
        this.direcaoAtual = 0;
        this.iImage = this.ImagemCima; //
        return true;
    }

    public boolean moveDown() {
        this.direcaoAtual = 1;
        this.iImage = this.ImagemBaixo; //
        return true;
    }

    public boolean moveRight() {
        this.direcaoAtual = 3;
        this.iImage = this.ImagemDireita; //
        return true;
    }

    public boolean moveLeft() {
        this.direcaoAtual = 2;
        this.iImage = this.ImagemEsquerda; //
        return true;
    }

}
