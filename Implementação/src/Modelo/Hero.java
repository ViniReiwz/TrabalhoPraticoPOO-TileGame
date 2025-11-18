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
import java.io.IOException;
import Auxiliar.Consts;

public class Hero extends Personagem {
    
    private ImageIcon[] framesAnimacaoCima;
    private ImageIcon[] framesAnimacaoBaixo;
    private ImageIcon[] framesAnimacaoEsquerda;
    private ImageIcon[] framesAnimacaoDireita;
    
    private int frameAtual = 0;
    private int contadorAnimacao = 0;
    private double velocidadeAnimacao = 0.25;
    private int totalFrames = 3;
    
    private boolean animacaoIndo = true;
    
    private String direcaoAtual = "cima";
    
    public Hero(String sNomeImagePNG, int linha, int coluna){
        super(sNomeImagePNG, linha, coluna);
        
        // Inicializa os arrays de frames
        framesAnimacaoCima = new ImageIcon[totalFrames];
        framesAnimacaoBaixo = new ImageIcon[totalFrames];
        framesAnimacaoEsquerda = new ImageIcon[totalFrames];
        framesAnimacaoDireita = new ImageIcon[totalFrames];
        
        // Carrega todos os frames de cada direção
        carregarFramesAnimacao();
        
        // Define a imagem inicial
        this.iImage = framesAnimacaoCima[0];
    }
    
    private void carregarFramesAnimacao(){
        // Carrega frames para CIMA
        framesAnimacaoCima[0] = carregarImagem("joaninhaCima1.png");
        framesAnimacaoCima[1] = carregarImagem("joaninhaCima2.png");
        framesAnimacaoCima[2] = carregarImagem("joaninhaCima3.png");
  
        
        // Carrega frames para BAIXO
        framesAnimacaoBaixo[0] = carregarImagem("joaninhaBaixo1.png");
        framesAnimacaoBaixo[1] = carregarImagem("joaninhaBaixo2.png");
        framesAnimacaoBaixo[2] = carregarImagem("joaninhaBaixo3.png");
        
        // Carrega frames para ESQUERDA
        framesAnimacaoEsquerda[0] = carregarImagem("joaninhaEsquerda1.png");
        framesAnimacaoEsquerda[1] = carregarImagem("joaninhaEsquerda2.png");
        framesAnimacaoEsquerda[2] = carregarImagem("joaninhaEsquerda3.png");
        
        
        // Carrega frames para DIREITA
        framesAnimacaoDireita[0] = carregarImagem("joaninhaDireita1.png");
        framesAnimacaoDireita[1] = carregarImagem("joaninhaDireita2.png");
        framesAnimacaoDireita[2] = carregarImagem("joaninhaDireita3.png");
        
    }
    
    private ImageIcon carregarImagem(String sNomeImagePNG) {
        try{
            ImageIcon iNewImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
            Image img = iNewImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            return new ImageIcon(bi);
        } catch (IOException ex){
            System.out.println("Erro ao carregar imagem: " + sNomeImagePNG + " - " + ex.getMessage());
            return null;
        }
    }
    
    private void atualizarAnimacao(){
        contadorAnimacao++;
        
        if(contadorAnimacao >= velocidadeAnimacao){
            contadorAnimacao = 0;

            if(animacaoIndo){
                frameAtual++;
                if(frameAtual >= totalFrames - 1){
                    animacaoIndo = false;
                }
            } else{
                frameAtual--;
                if(frameAtual <= 0){
                    animacaoIndo = true;
                }
            }

            atualizarImagemPorDirecao();
        }
    }
    
    private void atualizarImagemPorDirecao(){
        switch(direcaoAtual){
            case "cima":
                this.iImage = framesAnimacaoCima[frameAtual];
            break;
            case "baixo":
                this.iImage = framesAnimacaoBaixo[frameAtual];
            break;
            case "esquerda":
                this.iImage = framesAnimacaoEsquerda[frameAtual];
            break;
            case "direita":
                this.iImage = framesAnimacaoDireita[frameAtual];
            break;
        }
    }

    public void autoDesenho(){
        atualizarAnimacao();
        super.autoDesenho();
    }
    
    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if(!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())){
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;
    }
    
    private boolean validaPosicao(){
        if(!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())){
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;
    }
    
    public boolean moveUp(){
        if(super.moveUp()){
            direcaoAtual = "cima";
            atualizarImagemPorDirecao();
            return validaPosicao();
        }
        return false;
    }
    
    public boolean moveDown(){
        if(super.moveDown()){
            direcaoAtual = "baixo";
            atualizarImagemPorDirecao();
            return validaPosicao();
        }
        return false;
    }
    
    public boolean moveRight(){
        if(super.moveRight()){
            direcaoAtual = "direita";
            atualizarImagemPorDirecao();
            return validaPosicao();
        }
        return false;
    }
    
    public boolean moveLeft(){
        if(super.moveLeft()){
            direcaoAtual = "esquerda";
            atualizarImagemPorDirecao();
            return validaPosicao();
        }
        return false;
    }
}