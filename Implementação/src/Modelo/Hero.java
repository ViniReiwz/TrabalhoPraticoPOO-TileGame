package Modelo;


import Auxiliar.Desenho;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Image;
import Auxiliar.Consts;

public class Hero extends Personagem{

    private ImageIcon ImagemCima;
    private ImageIcon ImagemBaixo;
    private ImageIcon ImagemEsquerda;
    private ImageIcon ImagemDireita;
    private int vida = 3;

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

    // ===== NOVOS MÉTODOS PARA O SISTEMA DE this.VIDA =====
    
    /**
     * Retorna o número de this.vida restantes
     */
    public int getVida() {
        return this.vida;
    }
    
    /**
     * Remove uma vida do jogador
     * @return true se ainda tem vida, false se game over
     */
    public boolean perderVida() {
        if (this.vida > 0) {
            this.vida--;
            System.out.println("Vida perdida! this.Vida restantes: " + this.vida);
            return this.vida > 0;
        }
        return false;
    }
    
    /**
     * Adiciona uma vida (para power-ups futuros)
     */
    public void ganharVida() {
        if (this.vida < 3) {
            this.vida++;
            System.out.println("Vida recuperada! this.Vida: " + this.vida);
        }
    }
    
    /**
     * Reseta as this.vida para o valor inicial
     */
    public void resetarVida() {
        this.vida = 3;
    }
    
    /**
     * Verifica se o jogador ainda está vivo
     */
    public boolean estaVivo() {
        return this.vida > 0;
    }
}
