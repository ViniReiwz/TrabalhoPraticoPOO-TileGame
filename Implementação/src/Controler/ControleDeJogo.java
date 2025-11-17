package Controler;

import Modelo.Chaser;
import Modelo.Coletavel;
import Modelo.ParedeRoda;
import Modelo.Personagem;
import Modelo.Hero;
import Auxiliar.Posicao;
import java.awt.event.KeyEvent;
import Modelo.Fase;
import Modelo.Personagem;
import Modelo.Hero;
import Auxiliar.Posicao;
import Auxiliar.Consts;
import Auxiliar.BordaCronometro;
import java.util.ArrayList;

public class ControleDeJogo {
    
    private int contadorSpawn;
    private int tempoEntreSpawns;
    private int maxInimigos;
    private Posicao posicaoSpawnCentral;            // para spawnar no centro do game
    private BordaCronometro borda;                  // lógica da borda cronometrada


    public ControleDeJogo(){
        this.contadorSpawn = 0;
        this.tempoEntreSpawns = 150;               // frames (ajustável conforme se desejar)
        this.maxInimigos = 4;                      // podemos mudar isso também
        this.posicaoSpawnCentral = new Posicao(Consts.MUNDO_ALTURA / 2, Consts.MUNDO_LARGURA / 2);      // centro
        this.borda = new BordaCronometro();
    }

    public BordaCronometro getBorda(){
        return borda;
    }

    public void desenhaTudo(Fase fase) {

        // Spawna todos os personagens da fase (heroi + Inimigos + qualquer coisa)
        fase.spawnAllPers();

        // Spawna todos os coletáveis da fase (Atualmente enche a tela com imagem de explosao)
        fase.spawnAllColl();
    }
    
    public void processaTudo(Fase fase, boolean cima, boolean baixo, boolean esquerda, boolean direita) {
        // Pega o herói (assumindo que ele é sempre o índice 0)
        Hero hero = fase.heroi;
        Personagem pIesimoPersonagem;
        Coletavel cIesimoColetavel;

        ArrayList<Coletavel> removed = new ArrayList<>();
        
        // --- Loop 1: Processar IA dos Inimigos ---
        // (Esta parte estava correta)
        for (int i = 1; i < fase.getPersonagens().size(); i++) {
            pIesimoPersonagem = fase.getPersonagens().get(i);
                        
            if (pIesimoPersonagem instanceof Chaser) {
                ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
            }
        }
        
        // --- Loop 2: Processar Colisões (Coletáveis e Morte) ---
        // IMPORTANTE: Iteramos de trás para frente (do fim para o começo).
        // Isso evita erros (ConcurrentModificationException) ao remover 
        // um item da lista enquanto ainda estamos percorrendo ela.
        for (int i = fase.getPersonagens().size() - 1; i > 0; i--) { 
            pIesimoPersonagem = fase.getPersonagens().get(i);
            
            if (pIesimoPersonagem instanceof ParedeRoda) {
                if (hero.getPosicao().ParedeVe(pIesimoPersonagem.getPosicao())&&direita){
                ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                hero.moveRight();
                }
                else if (hero.getPosicao().ParedeVd(pIesimoPersonagem.getPosicao())&&esquerda){
                ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                hero.moveLeft();
                }
                else if (hero.getPosicao().ParedeHc(pIesimoPersonagem.getPosicao())&&baixo){
                ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                hero.moveDown();
                }
                else if (hero.getPosicao().ParedeHb(pIesimoPersonagem.getPosicao())&&cima){
                ((ParedeRoda) pIesimoPersonagem).roda(0,hero.getPosicao());
                hero.moveUp();
                }
            }
    
            // 1. O Herói está na mesma posição do personagem 'i'?
            else if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                
                // 2. O personagem 'i' é transponível?
                // (Itens não-transponíveis são tratados pelo 'validaPosicao' do Hero)
                if (pIesimoPersonagem.isbTransponivel()) { 
                    
                    // 3. O personagem 'i' é mortal? (Inimigo, armadilha)
                    if (pIesimoPersonagem.isbMortal()) {
                        System.out.println("GAME OVER!");
                        // Lógica de morte: remove o herói e para de checar colisões
                        fase.getPersonagens().remove(hero); 
                        break; // Sai do loop 'for'
                    } 
                }
            }
        }

        for(int j = fase.getColetaveis().size() - 1; j >= 0; j--)
        {
            cIesimoColetavel = fase.getColetaveis().get(j);
            if(hero.getPosicao().igual(cIesimoColetavel.getPosicao()))
            {
                removed.add(cIesimoColetavel);
            }
        }

        fase.getColetaveis().removeAll(removed);
        fase.updatePoints();
    // --- NOVO! ---
    // --- Loop 3: Gerenciamento do spawn dos inimigos ---
        spawnarInimigos(fase.getPersonagens());

    }    


    // --- NOVO MÉTODO ---

    private void spawnarInimigos(ArrayList<Personagem> umaFase){
        // contagem de quantos chaser há atualmente
        int numChasers = 0;
        for(Personagem p : umaFase){
            if(p instanceof Chaser){
                numChasers++;
            }
        }

        // se houver menos que o máximo estabelecido, incrementa o contador
        if(numChasers < maxInimigos){
            contadorSpawn++;

            double progresso = (double) contadorSpawn / tempoEntreSpawns;
            borda.atualizarProgresso(progresso);

            // quando dá o tempo, spawna um novo
            if(contadorSpawn >= tempoEntreSpawns){
                Chaser novoInimgo = new Chaser("chaser.png", posicaoSpawnCentral.getLinha(), posicaoSpawnCentral.getColuna());
                umaFase.add(novoInimgo);
                contadorSpawn = 0;              // reseta o contador
                borda.resetar();                // reseta a borda


                if(Consts.DEBUG){
                    System.out.println("Novo inimigo spawnado no centro! Borda resetada.");
                }
            }
        }
        else{
            // se já tem o máximo de inimigos, reseta a borda
            borda.resetar();
        }
    }

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(Fase fase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < fase.getPersonagens().size(); i++) {
            pIesimoPersonagem = fase.getPersonagens().get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void passaFase(Tela tela)
    {
        if (tela.faseAtual.getNum_to_collect() == 0)
        {
            tela.fase_num++;
            tela.getFases().remove(tela.faseAtual);
            tela.faseAtual=tela.getFases().get(tela.fase_num);
            tela.repaint();
        }
        else
        {
            System.out.println("Tem item a coletar ainda --> " + tela.faseAtual.getNum_to_collect());
        }
    }
}
