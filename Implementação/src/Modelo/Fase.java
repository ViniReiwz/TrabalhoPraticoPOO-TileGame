package Modelo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import Auxiliar.Consts;
import java.io.Serializable;
import Modelo.Parede;

// Classe onde tudo relacionado à uma fase deve ser feito
// A ideia é em Tela.java, chamar o construtor e adicionar os personagens
// Todo o manuseio interno (deletar personagem, adicionar, mudar estado das paredes, posição das coisas, etc é pra ser feito nessa classe)
public class Fase implements Serializable
{
    // ArrayList com todos os personagens não-coletaveis
    private ArrayList<Personagem> personagens = new ArrayList<>();

    // ArrayList com os coletáveis
    private ArrayList<Coletavel> coletaveis = new ArrayList<>();

    private ArrayList<Personagem> paredes = new ArrayList<>();

    // Número de coletaveis (ao chegar em 0 a fase deve acabar - Não implementado ainda)
    private int num_to_collect = -1;

    private int num_specials;

    private int num_skull;

    public int enemy_move_delay;

    // Heroi da fase
    public Hero heroi;

    public int multiplier = 1;

    // Sistema de pontuação
    private int pontos = 0;

    // Tempo base de spawn para uma fase
    private int tempoSpawnBase = 150;

    public Fase(int num_specials, int num_skull, int enemy_move_delay)
    {   

        this.num_specials = num_specials;
        this.num_skull = num_skull;
        this.enemy_move_delay = enemy_move_delay;


        // --- Paredes Internas (Estáticas) ---
        this.addParede(new ParedeV(Consts.imgPV, 3, 2));
        this.addParede(new ParedeH(Consts.imgPH, 4, 3));
        this.addParede(new ParedeV(Consts.imgPV, 2, 6));
        this.addParede(new ParedeV(Consts.imgPV, 2, 8));
        this.addParede(new ParedeV(Consts.imgPV, 3, 12));
        this.addParede(new ParedeH(Consts.imgPH, 4, 11));
        this.addParede(new ParedeV(Consts.imgPV, 2, 2));
        this.addParede(new ParedeV(Consts.imgPV, 2, 12));

        //cantos
        this.addParede(new ParedeV(Consts.imgC1, 4, 2));
        this.addParede(new ParedeV(Consts.imgC2, 4, 12));
        this.addParede(new ParedeV(Consts.imgC3, 6, 12));
        this.addParede(new ParedeV(Consts.imgC4, 6, 2));
        this.addParede(new ParedeV(Consts.imgC2, 11, 4));
        this.addParede(new ParedeV(Consts.imgC1, 11, 10));
        this.addParede(new ParedeV(Consts.imgC1, 8, 6));
        this.addParede(new ParedeV(Consts.imgC2, 8, 8));

        this.addParede(new ParedeV(Consts.imgPV, 7, 6));
        this.addParede(new ParedeV(Consts.imgPV, 7, 8));
        this.addParede(new ParedeH(Consts.imgPH, 8, 7));

        this.addParede(new ParedeH(Consts.imgPH, 6, 3));
        this.addParede(new ParedeV(Consts.imgPV, 7, 2));

        this.addParede(new ParedeH(Consts.imgPH, 6, 11));
        this.addParede(new ParedeV(Consts.imgPV, 7, 12));

        this.addParede(new ParedeH(Consts.imgPH, 11, 3));
        this.addParede(new ParedeV(Consts.imgPV, 9, 4));
        this.addParede(new ParedeV(Consts.imgPV, 10, 4));

        this.addParede(new ParedeH(Consts.imgPH, 11, 11));
        this.addParede(new ParedeV(Consts.imgPV, 9, 10));
        this.addParede(new ParedeV(Consts.imgPV, 10, 10));

        this.addParede(new ParedeH(Consts.imgPH, 12, 7));

        // --- Paredes Internas (Giratórias) ---
        // Lembrete: A sua ParedeRoda cuida de criar o 'prox' e o 'meio'
        
        // Portão 1 (Horizontal) na posição (2, 4)
        //new ParedeRoda(Imagem, linha, coluna, éMetade, ant, éVertical, meio)
        ParedeRoda pr1 = new ParedeRoda(Consts.imgPRH, 2, 3, true, null, false, null);
        this.addParede(pr1);
        this.addParede(pr1.prox);
        this.addParede(pr1.meio);

        // Portão 2 (Horizontal) na posição (2, 10)
        ParedeRoda pr2 = new ParedeRoda(Consts.imgPRH, 2, 9, true, null, false, null);
        this.addParede(pr2);
        this.addParede(pr2.prox);
        this.addParede(pr2.meio);
        
        // Portão 3 (Horizontal) na posição (5, 6)
        ParedeRoda pr3 = new ParedeRoda(Consts.imgPRH, 4, 6, true, null, false, null);
        this.addParede(pr3);
        this.addParede(pr3.prox);
        this.addParede(pr3.meio);

        // Portão 4 (Vertical) na posição (5, 6)
        ParedeRoda pr4 = new ParedeRoda(Consts.imgPRV, 8, 2, true, null, true, null);
        this.addParede(pr4);
        this.addParede(pr4.prox);
        this.addParede(pr4.meio);

        // Portão 5 (Vertical) na posição (5, 6)
        ParedeRoda pr5 = new ParedeRoda(Consts.imgPRV, 8, 12, true, null, true, null);
        this.addParede(pr5);
        this.addParede(pr5.prox);
        this.addParede(pr5.meio);

        // Portão 6 (Horizontal) na posição (5, 6)
        ParedeRoda pr6 = new ParedeRoda(Consts.imgPRH, 12, 8, true, null, false, null);
        this.addParede(pr6);
        this.addParede(pr6.prox);
        this.addParede(pr6.meio);

        // Portão 7 (Horizontal) na posição (5, 6)
        ParedeRoda pr7 = new ParedeRoda(Consts.imgPRH, 12, 4, true, null, false, null);
        this.addParede(pr7);
        this.addParede(pr7.prox);
        this.addParede(pr7.meio);

        // Portão 8 (Vertical) na posição (5, 6)
        ParedeRoda pr8 = new ParedeRoda(Consts.imgPRV, 9, 7, true, null, true, null);
        this.addParede(pr8);
        this.addParede(pr8.prox);
        this.addParede(pr8.meio);

        // --- Bordas do Labirinto (14x14) ---
        for (int j = 1; j < Consts.MUNDO_LARGURA-1; j++) {
            this.addParede(new ParedeH(Consts.imgPH, 0, j)); // Borda superior
            this.addParede(new ParedeH(Consts.imgPH, Consts.MUNDO_ALTURA-1, j)); // Borda inferior
        }
        for (int i = 1; i < Consts.MUNDO_ALTURA-1; i++) {
            this.addParede(new ParedeV(Consts.imgPV, i, 0)); // Borda esquerda
            this.addParede(new ParedeV(Consts.imgPV, i, Consts.MUNDO_LARGURA-1)); // Borda direita
        }

        boolean can_put;
        for(int i = 0; i < ((Consts.MUNDO_ALTURA) - 1); i++)
        {
            for(int j = 0; j < ((Consts.MUNDO_LARGURA) - 1); j++)
            {
                can_put = true;
                Coletavel c = new Coletavel("explosao.png", i, j);
                for(Personagem p : this.paredes)
                {
                    if(p.getPosicao().igual(c.getPosicao())){can_put = false;}
                }
                if(can_put)
                {this.coletaveis.add(c);}
            }
        }

        this.addSpecialColls();
    }

    // Retorna o array list com os personagens
    public ArrayList<Personagem> getPersonagens() 
    {
        return personagens;
    }

    public ArrayList<Coletavel> getColetaveis() 
    {
        return coletaveis;
    }
    
    public int getNum_to_collect() 
    {
        return num_to_collect;
    }

    public int getTempoSpawnBase(){
        return tempoSpawnBase;
    }

    public void setTempoSpawnBase(int tempoSpawnBase){
        this.tempoSpawnBase = tempoSpawnBase;
    }

    // Spawna todos os personagens
    public void spawnAllPers()
    {
        for(Personagem p : new ArrayList<>(this.personagens))
        {
            p.autoDesenho();
        }
    }

    // Spawna todos os coletaveis
    public void spawnAllColl()
    {
        for (Coletavel c : new ArrayList<>(this.coletaveis))
        {
            c.autoDesenho();
        }
    }

    public void spawnAllParedes()
    {
        for(Personagem par : new ArrayList<>(this.paredes))
        {
            par.autoDesenho();
        }
    }

    public ArrayList<Personagem> getParedes()
    {
        return this.paredes;
    }

    public void updatePoints(Coletavel c)
    {
        int before = this.num_to_collect;
        this.num_to_collect = this.coletaveis.size();
        if(before != this.num_to_collect)
        {
            this.pontos += (c.val * this.multiplier);
        }
    }

    public void addHero(Hero hero)
    {   
        hero.setPosicao(5, 7);
        this.heroi = hero;
    }

    public void addPers(Personagem pers)
    {
        this.personagens.add(pers);
    }
    
    public void addParede(Personagem parede)
    {
        this.paredes.add(parede);
    }

    // ===== MÉTODOS DE PONTUAÇÃO =====
    
    /**
     * Retorna a pontuação atual
     */
    public int getPontos() {
        return pontos;
    }
    
    /**
     * Reseta a pontuação
     */
    public void resetarPontos() {
        pontos = 0;
    }

    private void addSpecialColls()
    {
        for(int i = 0; i < this.num_specials; i ++)
        {
            int special_pos = ThreadLocalRandom.current().nextInt(0,this.coletaveis.size());
            Coletavel c = this.coletaveis.get(special_pos);
            ColetavelMult cesp = new ColetavelMult("bricks.png", c.getPosicao().getLinha(), c.getPosicao().getColuna());
            this.coletaveis.remove(c);
            this.coletaveis.add(cesp);
        }

        for(int i = 0; i < this.num_skull; i ++)
        {
            int special_pos = ThreadLocalRandom.current().nextInt(0,this.coletaveis.size());
            Coletavel c = this.coletaveis.get(special_pos);
            if(c.val >= 100)
            {
                i--;
            }
            else
            {
                Caveira skull = new Caveira(c.getPosicao().getLinha(), c.getPosicao().getColuna());
                this.coletaveis.remove(c);
                this.coletaveis.add(skull);
            }
        }
    }
}
