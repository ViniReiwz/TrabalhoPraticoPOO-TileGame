package Controler;

import Modelo.Chaser;
import Modelo.ParedeRoda;
import Modelo.Personagem;
import Modelo.Hero;
import Auxiliar.Posicao;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ControleDeJogo {
    
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++)
            e.get(i).autoDesenho();
    }
    
    public void processaTudo(ArrayList<Personagem> umaFase, boolean cima, boolean baixo, boolean esquerda, boolean direita) {
        // Pega o herói (assumindo que ele é sempre o índice 0)
        Hero hero = (Hero) umaFase.get(0);
        Personagem pIesimoPersonagem;
        
        // --- Loop 1: Processar IA dos Inimigos ---
        // (Esta parte estava correta)
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
                        
            if (pIesimoPersonagem instanceof Chaser) {
                ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
            }
        }
        
        // --- Loop 2: Processar Colisões (Coletáveis e Morte) ---
        // IMPORTANTE: Iteramos de trás para frente (do fim para o começo).
        // Isso evita erros (ConcurrentModificationException) ao remover 
        // um item da lista enquanto ainda estamos percorrendo ela.
        for (int i = umaFase.size() - 1; i > 0; i--) { 
            pIesimoPersonagem = umaFase.get(i);
            
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
                        umaFase.remove(hero); 
                        break; // Sai do loop 'for'
                    } 
                    // 4. Se é transponível E NÃO-mortal, é um COLETÁVEL!
                    else {
                        // Lógica de coleta:
                        umaFase.remove(pIesimoPersonagem); // Remove o coletável da fase
                    }
                }
            }
        }
   
    }

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
}
