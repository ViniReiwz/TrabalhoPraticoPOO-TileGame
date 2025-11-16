package Controler;

import Modelo.Chaser;
import Modelo.Coletavel;
import Modelo.Fase;
import Modelo.Personagem;
import Modelo.Hero;
import Auxiliar.Posicao;
import java.util.ArrayList;

public class ControleDeJogo {
    
   synchronized public void desenhaTudo(Fase fase) {

        // Spawna todos os personagens da fase (heroi + Inimigos + qualquer coisa)
        fase.spawnAllPers();

        // Spawna todos os coletáveis da fase (Atualmente enche a tela com imagem de explosao)
        fase.spawnAllColl();
    }
    
    synchronized public void processaTudo(Fase fase) {
        // Pega o herói
        Hero hero = (Hero)fase.getPersonagens().get(0);
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
            
            // 1. O Herói está na mesma posição do personagem 'i'?
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                
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
