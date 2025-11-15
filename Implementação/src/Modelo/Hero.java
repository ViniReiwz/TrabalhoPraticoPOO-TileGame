package Modelo;


import Auxiliar.Desenho;
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

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public boolean moveUp() {
        if(super.moveUp()) {
            this.iImage = this.ImagemCima; //MUDA A IMAGEM
            return validaPosicao();
        }
        return false;
    }

    public boolean moveDown() {
        if(super.moveDown()) {
            this.iImage = this.ImagemBaixo; //MUDA A IMAGEM
            return validaPosicao();
        }
        return false;
    }

    public boolean moveRight() {
        if(super.moveRight()) {
            this.iImage = this.ImagemDireita; //MUDA A IMAGEM
            return validaPosicao();
        }
        return false;
    }

    public boolean moveLeft() {
        if(super.moveLeft()) {
            this.iImage = this.ImagemEsquerda; //MUDA A IMAGEM
            return validaPosicao();
        }
        return false;
    }    
}
