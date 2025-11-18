package Modelo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Auxiliar.Consts;

import java.io.Serializable;

public class Fase1 extends Fase {
    
    private final String imgPH = "ParedeHorizontal.png";
    private final String imgPV = "ParedeVertical.png";
    private final String imgPRV = "ParedeRodaVertical.png";
    private final String imgPRH = "ParedeRodaHorizontal.png";
    
    
    public Fase1() {
        
        for(int i = 0; i < Consts.MUNDO_ALTURA; i++)
        {
            for(int j = 0; j < Consts.MUNDO_LARGURA; j++)
            {
                if(i==0&&(j==0||j==Consts.MUNDO_LARGURA-1)){continue;}
                else if(i==Consts.MUNDO_ALTURA-1&&(j==0||j==Consts.MUNDO_LARGURA-1)){continue;}
                Coletavel c = new Coletavel("explosao.png", i, j);
                this.coletaveis.add(c);
            }
        }
        super.addSpecial();
        
        // tempo de spawn pra fase 1
        this.setTempoSpawnBase(150);
        
                // Heroi
        Hero hero = new Hero("joaninha.png", 0, 7);
        
        // --- Inimigo ---


     // Posição inicial do inimigo

        Chaser chase1 = new Chaser("vilao1direita1.png", 20, 8);    // centro do mapa (20, 8)
      
        this.addHero(hero);
        this.addPers(hero);
        this.addPers(chase1);
        
        
        
         for (int j = 1; j < Consts.MUNDO_LARGURA-1; j++) {
            this.getPersonagens().add(new ParedeH(imgPH, 0, j)); // Borda superior
            this.getPersonagens().add(new ParedeH(imgPH, Consts.MUNDO_ALTURA-1, j)); // Borda inferior
        }
        for (int i = 1; i < Consts.MUNDO_ALTURA-1; i++) {
            this.getPersonagens().add(new ParedeV(imgPV, i, 0)); // Borda esquerda
            this.getPersonagens().add(new ParedeV(imgPV, i, Consts.MUNDO_LARGURA-1)); // Borda direita
        }
        
        this.getPersonagens().add(new ParedeH(imgPH, 3, 1));
        this.getPersonagens().add(new ParedeH(imgPH, 3, 2));
        this.getPersonagens().add(new ParedeV(imgPV, 1, 5));
        this.getPersonagens().add(new ParedeV(imgPV, 2, 5));
        this.getPersonagens().add(new ParedeV(imgPV, 3, 5));
        this.getPersonagens().add(new ParedeV(imgPV, 4, 5));
        this.getPersonagens().add(new ParedeV(imgPV, 5, 5));

        this.getPersonagens().add(new ParedeH(imgPH, 7, 5));
        this.getPersonagens().add(new ParedeH(imgPH, 7, 6));
        this.getPersonagens().add(new ParedeH(imgPH, 7, 7));
        this.getPersonagens().add(new ParedeH(imgPH, 7, 8));

        this.getPersonagens().add(new ParedeV(imgPV, 9, 9));
        this.getPersonagens().add(new ParedeV(imgPV, 10, 9));
        this.getPersonagens().add(new ParedeV(imgPV, 11, 9));
        this.getPersonagens().add(new ParedeV(imgPV, 12, 9));

        // --- Paredes Internas (Giratórias) ---
        // Lembrete: A sua ParedeRoda cuida de criar o 'prox' e o 'meio'
        
        // Portão 1 (Vertical) na posição (4, 8)
        // new ParedeRoda(Imagem, linha, coluna, éMetade, ant, éVertical, meio)
        ParedeRoda pr1 = new ParedeRoda(imgPRV, 4, 8, true, null, true, null);
        this.getPersonagens().add(pr1);
        this.getPersonagens().add(pr1.prox);
        this.getPersonagens().add(pr1.meio);

        // Portão 2 (Horizontal) na posição (10, 3)
        ParedeRoda pr2 = new ParedeRoda(imgPRH, 10, 3, true, null, false, null);
        this.getPersonagens().add(pr2);
        this.getPersonagens().add(pr2.prox);
        this.getPersonagens().add(pr2.meio);
        
        
        ArrayList<Coletavel> removed = new ArrayList<>();
 
        for(Personagem umPersonagem : this.getPersonagens()){
            for(int j = this.getColetaveis().size() - 1; j >= 0; j--)
            {
                Coletavel cIesimoColetavel = this.getColetaveis().get(j);
                if(umPersonagem.getPosicao().igual(cIesimoColetavel.getPosicao()))
                {
                    removed.add(cIesimoColetavel);
                }
                if(umPersonagem.getPosicao().igual(cIesimoColetavel.getPosicao()))
                {
                    removed.add(cIesimoColetavel);
                }
                if(umPersonagem instanceof ParedeRodaMeio){
                    if(umPersonagem.getPosicao().ParedeVd(cIesimoColetavel.getPosicao())||
                       umPersonagem.getPosicao().ParedeVe(cIesimoColetavel.getPosicao())||
                       umPersonagem.getPosicao().ParedeHc(cIesimoColetavel.getPosicao())||
                       umPersonagem.getPosicao().ParedeHb(cIesimoColetavel.getPosicao()))
                    {
                        removed.add(cIesimoColetavel);
                    }
                }
            }
            this.getColetaveis().removeAll(removed);
            for(Coletavel c : removed)
            {
                this.updatePoints(c);
            }
        }
    }



}
