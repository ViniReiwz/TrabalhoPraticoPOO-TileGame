package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Consts;
import Auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.IOException; 
import javax.swing.ImageIcon;

public class Hero extends Personagem {
    
    private ImageIcon[] framesAnimacaoCima;
    private ImageIcon[] framesAnimacaoBaixo;
    private ImageIcon[] framesAnimacaoEsquerda;
    private ImageIcon[] framesAnimacaoDireita;
    
    private int frameAtual = 0;
    private int contadorAnimacao = 0;
    private int velocidadeAnimacao = 1;
    private int totalFrames = 3;
    private int vida = 3;
    
    private boolean animacaoIndo = true;
    
    private String spriteDirecao = "cima"; 
    
    // -1=Parado, 0=Cima, 1=Baixo, 2=Esquerda, 3=Direita
    private int direcaoMovimento = -1; 
    
    private int contadorMovimento = 0;
    private final int VELOCIDADE_MOVIMENTO = 2; //quanto maior mais lento
    
    private Posicao pPosicaoInicial; 

    public Hero(String sNomeImagePNG, int linha, int coluna){
        super(sNomeImagePNG, linha, coluna);
        this.pPosicaoInicial = new Posicao(linha, coluna);
        
        framesAnimacaoCima = new ImageIcon[totalFrames];
        framesAnimacaoBaixo = new ImageIcon[totalFrames];
        framesAnimacaoEsquerda = new ImageIcon[totalFrames];
        framesAnimacaoDireita = new ImageIcon[totalFrames];
        
        carregarFramesAnimacao();
        this.iImage = framesAnimacaoCima[0];
    }
    
   
    private void carregarFramesAnimacao(){
        framesAnimacaoCima[0] = carregarImagem("joaninhaCima1.png");
        framesAnimacaoCima[1] = carregarImagem("joaninhaCima2.png");
        framesAnimacaoCima[2] = carregarImagem("joaninhaCima3.png");
        framesAnimacaoBaixo[0] = carregarImagem("joaninhaBaixo1.png");
        framesAnimacaoBaixo[1] = carregarImagem("joaninhaBaixo2.png");
        framesAnimacaoBaixo[2] = carregarImagem("joaninhaBaixo3.png");
        framesAnimacaoEsquerda[0] = carregarImagem("joaninhaEsquerda1.png");
        framesAnimacaoEsquerda[1] = carregarImagem("joaninhaEsquerda2.png");
        framesAnimacaoEsquerda[2] = carregarImagem("joaninhaEsquerda3.png");
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
                if(frameAtual >= totalFrames - 1) animacaoIndo = false;
            } else{
                frameAtual--;
                if(frameAtual <= 0) animacaoIndo = true;
            }
            atualizarImagemPorDirecao();
        }
    }
    
    private void atualizarImagemPorDirecao(){
        switch(spriteDirecao){
            case "cima": this.iImage = framesAnimacaoCima[frameAtual]; break;
            case "baixo": this.iImage = framesAnimacaoBaixo[frameAtual]; break;
            case "esquerda": this.iImage = framesAnimacaoEsquerda[frameAtual]; break;
            case "direita": this.iImage = framesAnimacaoDireita[frameAtual]; break;
        }
    }

    // ============================================================
    // === MÉTODO CORRIGIDO PARA INTERAGIR COM A PAREDE RODA ===
    // ============================================================
    @Override
    public void autoDesenho(){
        atualizarAnimacao();
        
        contadorMovimento++;
        // Verifica se chegou a hora de se mover
        if(contadorMovimento >= VELOCIDADE_MOVIMENTO && direcaoMovimento != -1) {
            contadorMovimento = 0;
            
            Posicao proximaPosicao = new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna());
            
            switch(direcaoMovimento) {
                case 0: proximaPosicao.moveUp(); break;    
                case 1: proximaPosicao.moveDown(); break;  
                case 2: proximaPosicao.moveLeft(); break;  
                case 3: proximaPosicao.moveRight(); break; 
            }
            
            // 1. Tenta se mover normalmente
            if(Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicao)) {
                this.pPosicao.setPosicao(proximaPosicao.getLinha(), proximaPosicao.getColuna());
            } else {
                // 2. Bateu em algo! Verifique se é uma ParedeRoda
                boolean interagiuComParede = false;
                
                Fase fase = Desenho.acessoATelaDoJogo().faseAtual;
                
                for(Personagem p : fase.getPersonagens()){
                    if(p instanceof ParedeRoda && p.getPosicao().igual(proximaPosicao)){
                        
                        // A. Gira a parede
                        ((ParedeRoda) p).roda(0, this.pPosicao);
                        
                        // B. CORREÇÃO: Tenta mover IMEDIATAMENTE após girar
                        // Verifica se agora (após girar) a posição ficou livre
                        if(Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicao)){
                             this.pPosicao.setPosicao(proximaPosicao.getLinha(), proximaPosicao.getColuna());
                        }
                        
                        interagiuComParede = true;
                        break; 
                    }
                }
                
                // Só para se NÃO interagiu com nada (ex: bateu em parede fixa)
                if(!interagiuComParede){
                    direcaoMovimento = -1; 
                }
            }
        }

        super.autoDesenho();
    }
    
    // --- MÉTODOS DE CONTROLE ---
    public boolean moveUp(){
        this.direcaoMovimento = 0;   
        this.spriteDirecao = "cima"; 
        atualizarImagemPorDirecao();
        return true;
    }
    public boolean moveDown(){
        this.direcaoMovimento = 1;
        this.spriteDirecao = "baixo";
        atualizarImagemPorDirecao();
        return true;
    }
    public boolean moveRight(){
        this.direcaoMovimento = 3;
        this.spriteDirecao = "direita";
        atualizarImagemPorDirecao();
        return true;
    }
    public boolean moveLeft(){
        this.direcaoMovimento = 2;
        this.spriteDirecao = "esquerda";
        atualizarImagemPorDirecao();
        return true;
    }    
    public void parar() {
        this.direcaoMovimento = -1;
    }

    // --- MÉTODOS DE VIDA ---
    public int getVida() { return this.vida; }
    public boolean perderVida() {
        if (this.vida > 0) {
            this.vida--;
            System.out.println("Vida perdida! Vidas restantes: " + this.vida);
            return this.vida > 0;
        }
        return false;
    }
    public void ganharVida() { if (this.vida < 3) this.vida++; }
    public void resetarVida() { this.vida = 3; }
    public boolean estaVivo() { return this.vida > 0; }
    
    public void resetarPosicao() {
        this.pPosicao.setPosicao(this.pPosicaoInicial.getLinha(), this.pPosicaoInicial.getColuna());
        this.direcaoMovimento = -1; 
        this.spriteDirecao = "cima"; 
        this.iImage = framesAnimacaoCima[0];
        atualizarImagemPorDirecao();
    }
}