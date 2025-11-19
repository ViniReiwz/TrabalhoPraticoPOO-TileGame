package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;
import Auxiliar.Consts;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Chaser extends Personagem {

    //Variáveis onde o heroi esta
    private boolean heroEstaAEsquerda;
    private boolean heroEstaAcima;

    //Variavel de estado (para onde esta indo agora)
    //0 = Direita, 1 = Esquerda, 2 = Cima, 3 = Baixo
    private int direcaoAtual; 

    private int counter;
    
    //Animação dos vilões
    private ImageIcon[] framesAnimacaoCima;
    private ImageIcon[] framesAnimacaoBaixo;
    private ImageIcon[] framesAnimacaoEsquerda;
    private ImageIcon[] framesAnimacaoDireita;
    
    private int frameAtual = 0;
    private int contadorAnimacao = 0;
    private double velocidadeAnimacao = 4;
    private int totalFrames = 3;
    private int move_delay;
    
    private boolean animacaoIndo = true;
    
    // Prefixo do nome das imagens (no nosso caso serão vilao1,
    //vilao2, vilao3, vilao4 e vilao5)
    private String prefixoImagem;

    public Chaser(String sNomeImagePNG, int linha, int coluna, int move_delay) {
        super(sNomeImagePNG, linha, coluna);
        this.move_delay = move_delay;
        //Inimigos são transponíveis mas mortais >:D
        this.bTransponivel = true; 
        this.bMortal = true;

        counter = 0;
        
        //Direção inicial aleatória (pode mudar se necessário)
        direcaoAtual = 0; //inicialmente para a direita
        
        // Extrai o prefixo do nome da imagem (remove .png)
        this.prefixoImagem = sNomeImagePNG.replace(".png", "");
        
        // Inicializa os arrays de frames
        framesAnimacaoCima = new ImageIcon[totalFrames];
        framesAnimacaoBaixo = new ImageIcon[totalFrames];
        framesAnimacaoEsquerda = new ImageIcon[totalFrames];
        framesAnimacaoDireita = new ImageIcon[totalFrames];
        
        // Carrega todos os frames de cada direção
        carregarFramesAnimacao();
        
        // Define a imagem inicial (direita, frame 0)
        this.iImage = framesAnimacaoDireita[0];
    }

    private void carregarFramesAnimacao() {
        //Carrega frames para cada direção
        //Formato esperado: vilao1cima1.png, vilao1cima2.png, vilao1cima3.png
        
        framesAnimacaoCima[0] = carregarImagem(prefixoImagem + "cima1.png");
        framesAnimacaoCima[1] = carregarImagem(prefixoImagem + "cima2.png");
        framesAnimacaoCima[2] = carregarImagem(prefixoImagem + "cima3.png");
        
        framesAnimacaoBaixo[0] = carregarImagem(prefixoImagem + "baixo1.png");
        framesAnimacaoBaixo[1] = carregarImagem(prefixoImagem + "baixo2.png");
        framesAnimacaoBaixo[2] = carregarImagem(prefixoImagem + "baixo3.png");
        
        framesAnimacaoEsquerda[0] = carregarImagem(prefixoImagem + "esquerda1.png");
        framesAnimacaoEsquerda[1] = carregarImagem(prefixoImagem + "esquerda2.png");
        framesAnimacaoEsquerda[2] = carregarImagem(prefixoImagem + "esquerda3.png");
        
        framesAnimacaoDireita[0] = carregarImagem(prefixoImagem + "direita1.png");
        framesAnimacaoDireita[1] = carregarImagem(prefixoImagem + "direita2.png");
        framesAnimacaoDireita[2] = carregarImagem(prefixoImagem + "direita3.png");
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
            System.out.println("Erro ao carregar imagem: " + sNomeImagePNG + " - " + ex.getMessage());
            // Retorna a imagem padrão se houver erro
            return this.iImage;
        }
    }
    
    private void atualizarAnimacao() {
        contadorAnimacao++;
        
        if(contadorAnimacao >= velocidadeAnimacao) {
            contadorAnimacao = 0;

            if(animacaoIndo) {
                frameAtual++;
                if(frameAtual >= totalFrames - 1) {
                    animacaoIndo = false;
                }
            } else {
                frameAtual--;
                if(frameAtual <= 0) {
                    animacaoIndo = true;
                }
            }

            atualizarImagemPorDirecao();
        }
    }
    
    private void atualizarImagemPorDirecao() {
        switch(direcaoAtual) {
            case 0: // Direita
                this.iImage = framesAnimacaoDireita[frameAtual];
                break;
            case 1: // Esquerda
                this.iImage = framesAnimacaoEsquerda[frameAtual];
                break;
            case 2: // Cima
                this.iImage = framesAnimacaoCima[frameAtual];
                break;
            case 3: // Baixo
                this.iImage = framesAnimacaoBaixo[frameAtual];
                break;
        }
    }

    //Analisa a posição do heroi em relação ao perseguidor
    public void computeDirection(Posicao heroPos) {
        if (heroPos.getColuna() < this.getPosicao().getColuna()) {
            heroEstaAEsquerda = true;
        } else if (heroPos.getColuna() > this.getPosicao().getColuna()) {
            heroEstaAEsquerda = false;
        }
        
        if (heroPos.getLinha() < this.getPosicao().getLinha()) {
            heroEstaAcima = true;
        } else if (heroPos.getLinha() > this.getPosicao().getLinha()) {
            heroEstaAcima = false;
        }
    }

   
    public void autoDesenho() {
        if (counter == this.move_delay) {
            counter = 0;

            Posicao proximaPosicao = new Posicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
            
            //Tenta se mover na direção atual
            switch (direcaoAtual) {
                case 0: // Direita
                    proximaPosicao.moveRight();
                    break;
                case 1: // Esquerda
                    proximaPosicao.moveLeft();
                    break;
                case 2: // Cima
                    proximaPosicao.moveUp();
                    break;
                case 3: // Baixo
                    proximaPosicao.moveDown();
                    break;
            }

            if (Desenho.acessoATelaDoJogo().ehPosicaoValida(proximaPosicao)) {
                // Caminho livre, mover
                this.setPosicao(proximaPosicao.getLinha(), proximaPosicao.getColuna());
            } else {
                //se ta aqui, bateu na parede
                //hora de tomar uma nova direção
                escolherNovaDirecao();
            }

        } else {
            counter++;
        }
        
        // Atualiza a animação antes de desenhar
        atualizarAnimacao();
        
        super.autoDesenho();
    }
    
    //para decidir para onde ir quando encontrar uma parede
    private void escolherNovaDirecao() {
        //0=Dir, 1=Esq, 2=Cima, 3=Baixo
        boolean[] movimentosValidos = new boolean[4];

        // 1. Verificar todas as 4 direções
        movimentosValidos[0] = ehMovimentoValido(this.getPosicao().getLinha(), this.getPosicao().getColuna() + 1); // Direita
        movimentosValidos[1] = ehMovimentoValido(this.getPosicao().getLinha(), this.getPosicao().getColuna() - 1); // Esquerda
        movimentosValidos[2] = ehMovimentoValido(this.getPosicao().getLinha() - 1, this.getPosicao().getColuna()); // Cima
        movimentosValidos[3] = ehMovimentoValido(this.getPosicao().getLinha() + 1, this.getPosicao().getColuna()); // Baixo

        //Tenta escolher a melhor direção (que não seja a oposta)
        
        // Tentar virar na horizontal (Esquerda/Direita)
        if (heroEstaAEsquerda && movimentosValidos[1] && direcaoAtual != 0) { // Tentar ir Esquerda
            direcaoAtual = 1;
            atualizarImagemPorDirecao();
            return;
        }
        if (!heroEstaAEsquerda && movimentosValidos[0] && direcaoAtual != 1) { // Tentar ir Direita
            direcaoAtual = 0;
            atualizarImagemPorDirecao();
            return;
        }

        // Tentar virar na vertical (Cima/Baixo)
        if (heroEstaAcima && movimentosValidos[2] && direcaoAtual != 3) { // Tentar ir Cima
            direcaoAtual = 2;
            atualizarImagemPorDirecao();
            return;
        }
        if (!heroEstaAcima && movimentosValidos[3] && direcaoAtual != 2) { // Tentar ir Baixo
            direcaoAtual = 3;
            atualizarImagemPorDirecao();
            return;
        }

        //se as "melhores" opções estiverem bloqueadas, pegar qualquer uma válida
        for (int i = 0; i < 4; i++) {
            if (movimentosValidos[i]) {
                if (i == 0 && direcaoAtual != 1) { direcaoAtual = i; atualizarImagemPorDirecao(); return; }
                if (i == 1 && direcaoAtual != 0) { direcaoAtual = i; atualizarImagemPorDirecao(); return; }
                if (i == 2 && direcaoAtual != 3) { direcaoAtual = i; atualizarImagemPorDirecao(); return; }
                if (i == 3 && direcaoAtual != 2) { direcaoAtual = i; atualizarImagemPorDirecao(); return; }
            }
        }
        
        //Se estiver preso em um beco sem saída da meia volta
        if (movimentosValidos[0]) { direcaoAtual = 0; atualizarImagemPorDirecao(); return; }
        if (movimentosValidos[1]) { direcaoAtual = 1; atualizarImagemPorDirecao(); return; }
        if (movimentosValidos[2]) { direcaoAtual = 2; atualizarImagemPorDirecao(); return; }
        if (movimentosValidos[3]) { direcaoAtual = 3; atualizarImagemPorDirecao(); return; }
    }

    //Método auxiliar para checar se uma célula é válida.
    private boolean ehMovimentoValido(int linha, int coluna) {
        Posicao p = new Posicao(linha, coluna);
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p);
    }
}