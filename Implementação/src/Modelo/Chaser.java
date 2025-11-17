package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

public class Chaser extends Personagem {

    //Variáveis onde o heroi esta
    //Isso e o computeDirection q vai definir.
    private boolean heroEstaAEsquerda;
    private boolean heroEstaAcima;

    //Variavel de estado (para onde esta indo agora)
    //0 = Direita, 1 = Esquerda, 2 = Cima, 3 = Baixo
    private int direcaoAtual; 

    private int counter;

    public Chaser(String sNomeImagePNG, int linha, int coluna) {
        super(sNomeImagePNG, linha, coluna);
        
        //Inimigos são transponíveis mas mortais >:D
        this.bTransponivel = true; 
        this.bMortal = true;

        counter = 0;
        
        //Direção inicial aleatória (pode mudar se necessário)
        direcaoAtual = 0; //inicialmente para a direita 
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
        if (counter == 5) { //Controla a velocidade do inimigo (pode alterar se precisar, quanto maior mais lento)
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

            // O Chaser usa a mesma validação de posição que o heroi
            // Isso garante que ele vai parar em ParedeV, ParedeH e ParedeRoda.
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
            return;
        }
        if (!heroEstaAEsquerda && movimentosValidos[0] && direcaoAtual != 1) { // Tentar ir Direita
            direcaoAtual = 0;
            return;
        }

        // Tentar virar na vertical (Cima/Baixo)
        if (heroEstaAcima && movimentosValidos[2] && direcaoAtual != 3) { // Tentar ir Cima
            direcaoAtual = 2;
            return;
        }
        if (!heroEstaAcima && movimentosValidos[3] && direcaoAtual != 2) { // Tentar ir Baixo
            direcaoAtual = 3;
            return;
        }

        //se as "melhores" opções estiverem bloqueadas, pegar qualquer uma válida
        //(exceto a oposta, se possível)
        for (int i = 0; i < 4; i++) {
            if (movimentosValidos[i]) {
                if (i == 0 && direcaoAtual != 1) { direcaoAtual = i; return; } // Direita (não oposto de Esq)
                if (i == 1 && direcaoAtual != 0) { direcaoAtual = i; return; } // Esquerda (não oposto de Dir)
                if (i == 2 && direcaoAtual != 3) { direcaoAtual = i; return; } // Cima (não oposto de Baixo)
                if (i == 3 && direcaoAtual != 2) { direcaoAtual = i; return; } // Baixo (não oposto de Cima)
            }
        }
        
        //Se estiver preso em um beco sem saída da meia volta
        if (movimentosValidos[0]) { direcaoAtual = 0; return; }
        if (movimentosValidos[1]) { direcaoAtual = 1; return; }
        if (movimentosValidos[2]) { direcaoAtual = 2; return; }
        if (movimentosValidos[3]) { direcaoAtual = 3; return; }

    }

    //Método auxiliar para checar se uma célula é válida.
    private boolean ehMovimentoValido(int linha, int coluna) {
        Posicao p = new Posicao(linha, coluna);
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p);
    }

    
}